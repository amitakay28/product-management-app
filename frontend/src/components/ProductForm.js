import React, { useState, useEffect } from 'react';
import axios from '../services/api';
import '../styles/styles.css';

function ProductForm() {
    const [name, setName] = useState('');
    const [productTypeId, setProductTypeId] = useState('');
    const [selectedColours, setSelectedColours] = useState([]);
    const [productTypes, setProductTypes] = useState([]);
    const [availableColours, setAvailableColours] = useState([]);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        axios.get('/product-types').then(response => setProductTypes(response.data));
        axios.get('/colours').then(response => setAvailableColours(response.data));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (selectedColours.length === 0) {
            setError('Please select at least one colour.');
            return;
        }

        const productData = {
            name,
            productTypeId: parseInt(productTypeId),
            colourIds: selectedColours
        };

        try {
            await axios.post('/products', productData);
            setSuccessMessage('✅ Product added successfully!');
            setTimeout(() => setSuccessMessage(''), 2000); // Hide after 2 seconds
            setName('');
            setProductTypeId('');
            setSelectedColours([]);
            setError('');
        } catch (error) {
            console.error('Error adding product:', error.response?.data || error.message);
        }
    };

    const toggleColourSelection = (id) => {
        if (selectedColours.includes(id)) {
            setSelectedColours(selectedColours.filter(colourId => colourId !== id));
        } else {
            setSelectedColours([...selectedColours, id]);
        }
    };

    return (
        <div className="add-product-container">
            <h2 className="add-product-heading">Add a New Product</h2>

            <form onSubmit={handleSubmit} className="add-product-form">
                {successMessage && <div className="success-message">{successMessage}</div>}

                <div className="form-group">
                    <label>Product Name</label>
                    <input
                        type="text"
                        placeholder="Enter Product Name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Select Product Type</label>
                    <select value={productTypeId} onChange={(e) => setProductTypeId(e.target.value)} required>
                        <option value="">Choose...</option>
                        {productTypes.map(pt => (
                            <option key={pt.id} value={pt.id}>{pt.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Select Colours</label>
                    <div className="colour-selection">
                        {availableColours.map(colour => (
                            <div
                                key={colour.id}
                                className={`colour-box ${selectedColours.includes(colour.id) ? 'selected' : ''}`}
                                onClick={() => toggleColourSelection(colour.id)}
                            >
                                {colour.name}
                            </div>
                        ))}
                    </div>
                </div>

                {error && <div className="error-message">{error}</div>}

                <button type="submit" className="primary-btn">Add Product</button>
            </form>
        </div>
    );
}

export default ProductForm;
