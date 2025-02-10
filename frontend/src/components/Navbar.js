import React from 'react';
import { NavLink } from 'react-router-dom';
import '../styles/styles.css';

function Navbar() {
    return (
        <nav className="navbar">
            <h2>🏢 Product Management</h2>
            <div className="nav-links">
                <NavLink exact to="/" className={({ isActive }) => isActive ? "active-link" : ""}>Home</NavLink>
                <NavLink to="/products" className={({ isActive }) => isActive ? "active-link" : ""}>View Products</NavLink>
                <NavLink to="/add-product" className={({ isActive }) => isActive ? "active-link" : ""}>Add Product</NavLink>
                <NavLink to="/colours" className={({ isActive }) => isActive ? "active-link" : ""}>Manage Colours</NavLink>
                <NavLink to="/product-types" className={({ isActive }) => isActive ? "active-link" : ""}>Manage Product Types</NavLink>
            </div>
        </nav>
    );
}

export default Navbar;
