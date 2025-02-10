import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/styles.css';

function Home() {
    return (
        <div className="home-container">
            <div className="home-content">
                <h1 className="home-title">Welcome to Product Management</h1>
                <p className="home-subtext">
                    A seamless way to manage products, categories, and colors effortlessly.
                </p>

                <div className="home-buttons">
                    <Link to="/products" className="home-btn">🛒 View Products</Link>
                    <Link to="/add-product" className="home-btn">➕ Add Product</Link>
                    <Link to="/colours" className="home-btn">🎨 Manage Colours</Link>
                    <Link to="/product-types" className="home-btn">📦 Manage Categories</Link>
                </div>
            </div>
        </div>
    );
}

export default Home;
