import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './styles/styles.css';

/**
 * Renders the main App component into the root element.
 */
const root = createRoot(document.getElementById('root'));
root.render(<App />);

