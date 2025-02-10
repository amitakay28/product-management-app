import axios from 'axios';

/**
 * Axios instance for API communication.
 */
const API_BASE_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080/api';

export default axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});
