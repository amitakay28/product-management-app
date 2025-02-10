import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './components/Home';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import ManageColours from './components/ManageColours';
import ManageProductTypes from './components/ManageProductTypes';

function App() {
    return (
        <Router>
            <div>
                <Navbar />
                <div>
                    <Routes> {}
                        <Route path="/" element={<Home />} />
                        <Route path="/products" element={<ProductList />} />
                        <Route path="/add-product" element={<ProductForm />} />
                        <Route path="/colours" element={<ManageColours />} />
                        <Route path="/product-types" element={<ManageProductTypes />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
