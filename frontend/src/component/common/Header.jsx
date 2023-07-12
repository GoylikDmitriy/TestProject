import React from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";

export default function Header() {
    const username = localStorage.getItem('username');
    const navigate = useNavigate();

    const handleLogout = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/logout');
        } catch (error) {
            if (error.response && error.response.status === 401) {
                localStorage.clear();
                navigate('/login');
            }
            else {
                console.log(error);
            }
        }
    }

    return (
        <nav className="navbar navbar-expand-lg bg-body-tertiary">
            <div className="container-fluid">
                <a className="navbar-brand" href="/">Questions portal</a>
                <div className="d-flex justify-content-end w-100">
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNavDropdown">
                        <ul className="navbar-nav ms-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/questions/outgoing/">Your questions</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/questions/incoming/">Answer the questions</a>
                            </li>
                            <li className="nav-item dropdown">
                                <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                                   aria-expanded="false">
                                    {username}
                                </a>
                                <ul className="dropdown-menu">
                                    <li><a className="dropdown-item" href="/user/edit">Edit</a></li>
                                    <li><a className="dropdown-item" href="/user/delete">Delete</a></li>
                                    <li><a className="dropdown-item" href="#" onClick={handleLogout}>Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
    );
}