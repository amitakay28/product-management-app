import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../styles/styles.css";

function ManageColours() {
    const [colours, setColours] = useState([]);
    const [newColour, setNewColour] = useState("");
    const [editingId, setEditingId] = useState(null);
    const [editedName, setEditedName] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        fetchColours();
    }, []);

    const fetchColours = async () => {
        try {
            const response = await axios.get("/colours");
            setColours(response.data);
            setLoading(false);
        } catch (error) {
            setError("Failed to fetch colours");
            setLoading(false);
        }
    };

    const handleAddColour = async () => {
        if (!newColour.trim()) return;

        try {
            const response = await axios.post("/colours", { name: newColour });
            setColours([...colours, response.data]);
            setNewColour("");
            setSuccessMessage("Colour added successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert("Failed to add colour.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this colour?")) return;

        try {
            await axios.delete(`/colours/${id}`);
            setColours(colours.filter((colour) => colour.id !== id));
            setSuccessMessage("Colour deleted successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert(error.response?.data?.message || "Cannot delete colour.");
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

        if(!editedName || editedName.trim() === ""){
            alert("❌ Please add a valid colour name.");
            return;
        }
        try {
            await axios.put(`/colours/${id}`, { name: editedName });
            fetchColours();
            setEditingId(null);
            setSuccessMessage("Colour updated successfully!");
            setTimeout(() => setSuccessMessage(""), 2000);
        } catch (error) {
            alert("Failed to update colour.");
        }
    };

    if (loading) return <div className="loader">Loading colours...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="container manage-container">
            <h2>Manage Colours</h2>

            {successMessage && <div className="success-message">{successMessage}</div>}

            <div className="manage-card">
                <h3>Add New Colour</h3>
                <div className="manage-input-group">
                    <input
                        type="text"
                        className="manage-input"
                        placeholder="Enter new colour"
                        value={newColour}
                        onChange={(e) => setNewColour(e.target.value)}
                    />
                    <button className="add-btn" onClick={handleAddColour}>➕ Add</button>
                </div>
            </div>

            <div className="manage-card">
                <h3>All Colours</h3>
                <table className="styled-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {colours.map((colour) => (
                            <tr key={colour.id}>
                                <td>{colour.id}</td>
                                <td>
                                    {editingId === colour.id ? (
                                        <input
                                            type="text"
                                            value={editedName}
                                            onChange={(e) => setEditedName(e.target.value)}
                                        />
                                    ) : (
                                        colour.name
                                    )}
                                </td>
                                <td className="actions-cell">
                                    {editingId === colour.id ? (
                                        <>
                                            <button className="save-btn" onClick={() => handleUpdate(colour.id)}>✅ Save</button>
                                            <button className="cancel-btn" onClick={handleCancelEdit}>❌ Cancel</button>
                                        </>
                                    ) : (
                                        <>
                                            <div className="tooltip">
                                                <button className="edit-btn" onClick={() => handleEdit(colour.id, colour.name)}>✏️</button>
                                                <span className="tooltip-text">Edit</span>
                                            </div>
                                            <div className="tooltip">
                                                <button className="delete-btn" onClick={() => handleDelete(colour.id)}>🗑️</button>
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

export default ManageColours;
