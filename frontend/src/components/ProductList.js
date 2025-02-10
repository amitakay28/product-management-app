import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../styles/styles.css";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedProduct, setEditedProduct] = useState({});
    const [productTypes, setProductTypes] = useState([]);
    const [colours, setColours] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState("");

    useEffect(() => {
        fetchProducts();
        fetchProductTypes();
        fetchColours();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await axios.get("/products");
            setProducts(response.data);
            setLoading(false);
        } catch (error) {
            setError("Failed to fetch products");
            setLoading(false);
        }
    };

    const fetchProductTypes = async () => {
        try {
            const response = await axios.get("/product-types");
            setProductTypes(response.data);
        } catch (error) {
            console.error("Failed to fetch product types");
        }
    };

    const fetchColours = async () => {
        try {
            const response = await axios.get("/colours");
            setColours(response.data);
        } catch (error) {
            console.error("Failed to fetch colours");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this product?")) return;

        try {
            await axios.delete(`/products/${id}`);
            setProducts(products.filter((product) => product.id !== id));
            setMessage("✅ Product deleted successfully!");
            setTimeout(() => setMessage(""), 3000);
        } catch (error) {
            alert(error.response?.data?.message || "❌ Cannot delete product.");
        }
    };

    const handleEdit = (product) => {
    console.log(product);
        setEditingId(product.id);
        setEditedProduct({
            name: product.name,
            productTypeId: productTypes.find(pt => pt.name === product.productType)?.id || "",
            selectedColourNames: [...(product.colours ?? [])]
        });
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        setEditedProduct({});
    };

    const handleUpdate = async (id) => {
        if (!editedProduct.name || editedProduct.name.trim() === "") {
                alert("❌ Please add a valid product name.");
                return;
            }
        if (!editedProduct.productTypeId) {
                alert("❌ Please select a product type.");
                return;
            }
        if (editedProduct.selectedColourNames.length === 0) {
                alert("❌ Please select at least one color.");
                return;
            }
        try {
            const updatedProduct = {
                name: editedProduct.name,
                productTypeId: parseInt(editedProduct.productTypeId),
                colourIds: colours
                    .filter(c => editedProduct.selectedColourNames.includes(c.name))
                    .map(c => c.id),
            };

            await axios.put(`/products/${id}`, updatedProduct);
            fetchProducts();
            setEditingId(null);
            setMessage("✅ Product updated successfully!");
            setTimeout(() => setMessage(""), 3000);
        } catch (error) {
            alert("❌ Failed to update product.");
        }
    };

    const toggleColourSelection = (colourName) => {
        setEditedProduct((prev) => ({
            ...prev,
            selectedColourNames: prev.selectedColourNames.includes(colourName)
                ? prev.selectedColourNames.filter((c) => c !== colourName)
                : [...prev.selectedColourNames, colourName],
        }));
    };

    if (loading) return <div className="loader">Loading products...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="container">
            <h2>All Products</h2>

            {message && <div className="success-message">{message}</div>}

            <table className="styled-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Product Type</th>
                        <th>Colours</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.length === 0 ? (
                        <tr>
                            <td colSpan="5" className="no-products-message">No products to show. Please add a product!</td>
                        </tr>
                    ) : (
                        products.map((product) => (
                            <tr key={product.id}>
                                <td>{product.id}</td>
                                <td>
                                    {editingId === product.id ? (
                                        <input
                                            type="text"
                                            value={editedProduct.name}
                                            onChange={(e) =>
                                                setEditedProduct({ ...editedProduct, name: e.target.value })
                                            }
                                        />
                                    ) : (
                                        product.name
                                    )}
                                </td>
                                <td>
                                    {editingId === product.id ? (
                                        <select
                                            value={editedProduct.productTypeId}
                                            onChange={(e) =>
                                                setEditedProduct({ ...editedProduct, productTypeId: e.target.value })
                                            }
                                        >
                                            <option value="">Choose...</option>
                                            {productTypes.map(pt => (
                                                <option key={pt.id} value={pt.id}>{pt.name}</option>
                                            ))}
                                        </select>
                                    ) : (
                                        product.productType
                                    )}
                                </td>
                                <td>
                                    {editingId === product.id ? (
                                        <div className="colour-selection">
                                            {colours.map((colour) => (
                                                <div
                                                    key={colour.id}
                                                    className={`colour-box ${
                                                        editedProduct.selectedColourNames.includes(colour.name) ? "selected" : ""
                                                    }`}
                                                    onClick={() => toggleColourSelection(colour.name)}
                                                >
                                                    {colour.name}
                                                </div>
                                            ))}
                                        </div>
                                    ) : (
                                        (Array.isArray(product.colours) ? product.colours.join(", ") : product.colours) || "None"
                                    )}
                                </td>
                                <td className="actions-cell">
                                    {editingId === product.id ? (
                                        <>
                                            <button className="save-btn" onClick={() => handleUpdate(product.id)} title="Save">
                                                ✅ Save
                                            </button>
                                            <button className="cancel-btn" onClick={handleCancelEdit} title="Cancel">
                                                ❌ Cancel
                                            </button>
                                        </>
                                    ) : (
                                        <>
                                            <button className="edit-btn tooltip" onClick={() => handleEdit(product)}>
                                                ✏️
                                                <span className="tooltip-text">Edit</span>
                                            </button>
                                            <button className="delete-btn tooltip" onClick={() => handleDelete(product.id)}>
                                                🗑️
                                                <span className="tooltip-text">Delete</span>
                                            </button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    );
}

export default ProductList;
