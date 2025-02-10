import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../styles/styles.css";

function ManageProductTypes() {
    const [productTypes, setProductTypes] = useState([]);
    const [newProductType, setNewProductType] = useState("");
    const [editingId, setEditingId] = useState(null);
    const [editedName, setEditedName] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        fetchProductTypes();
    }, []);

    const fetchProductTypes = async () => {
        try {
            const response = await axios.get("/product-types");
            setProductTypes(response.data);
            setLoading(false);
        } catch (error) {
            setError("Failed to fetch product types");
            setLoading(false);
        }
    };

    const handleAddProductType = async () => {
        if (!newProductType.trim()) return;

        try {
            const response = await axios.post("/product-types", { name: newProductType });
            setProductTypes([...productTypes, response.data]);
            setNewProductType("");
            setSuccessMessage("Product Type added successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert("Failed to add product type.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this product type?")) return;

        try {
            await axios.delete(`/product-types/${id}`);
            setProductTypes(productTypes.filter((type) => type.id !== id));
            setSuccessMessage("Product Type deleted successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert(error.response?.data?.message || "Cannot delete product type.");
        }
    };

    const handleEdit = (id, name) => {
        setEditingId(id);
        setEditedName(name);
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        setEditedName("");
    };

    const handleUpdate = async (id) => {
        if(!editedName || editedName.trim === ""){
                    alert("❌ Please add a valid product type name.");
                    return;
                }
        try {
            await axios.put(`/product-types/${id}`, { name: editedName });
            fetchProductTypes();
            setEditingId(null);
            setSuccessMessage("Product Type updated successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert("Failed to update product type.");
        }
    };

    if (loading) return <div className="loader">Loading product types...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="container manage-container">
            <h2>Manage Product Types</h2>

            {successMessage && <div className="success-message">{successMessage}</div>}

            <div className="manage-card">
                <h3>Add New Product Type</h3>
                <div className="manage-input-group">
                    <input
                        type="text"
                        className="manage-input"
                        placeholder="Enter new product type"
                        value={newProductType}
                        onChange={(e) => setNewProductType(e.target.value)}
                    />
                    <button className="add-btn" onClick={handleAddProductType}>➕ Add</button>
                </div>
            </div>

            <div className="manage-card">
                <h3>All Product Types</h3>
                <table className="styled-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {productTypes.map((type) => (
                            <tr key={type.id}>
                                <td>{type.id}</td>
                                <td>
                                    {editingId === type.id ? (
                                        <input
                                            type="text"
                                            value={editedName}
                                            onChange={(e) => setEditedName(e.target.value)}
                                        />
                                    ) : (
                                        type.name
                                    )}
                                </td>
                                <td className="actions-cell">
                                    {editingId === type.id ? (
                                        <>
                                            <button className="save-btn" onClick={() => handleUpdate(type.id)}>✅ Save</button>
                                            <button className="cancel-btn" onClick={handleCancelEdit}>❌ Cancel</button>
                                        </>
                                    ) : (
                                        <>
                                            <div className="tooltip">
                                                <button className="edit-btn" onClick={() => handleEdit(type.id, type.name)}>✏️</button>
                                                <span className="tooltip-text">Edit</span>
                                            </div>
                                            <div className="tooltip">
                                                <button className="delete-btn" onClick={() => handleDelete(type.id)}>🗑️</button>
                                                <span className="tooltip-text">Delete</span>
                                            </div>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default ManageProductTypes;
