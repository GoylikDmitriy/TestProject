import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import axios from "axios";
import { useNavigate } from "react-router-dom";
import {useState, useEffect, Component} from "react";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        if (name === "username") {
            setUsername(value);
        } else if (name === "password") {
            setPassword(value);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const requestData = new URLSearchParams();
            requestData.append("username", username);
            requestData.append("password", password);

            const response = await axios.post(
                '/login',
                requestData.toString(),
                {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                }
            );

            if (response.status === 200) {
                localStorage.setItem('token', response.data.token);
                localStorage.setItem('username', response.data.username);
                navigate('/');
            }
        } catch (error) {
            if (error.response && error.response.status === 401) {
                setError("Invalid username or password");
            }
            else {
                setError("Authentication failed");
            }
        }
    };

    return (
        <div className="container border shadow p-3 mb-3 rounded mx-auto mt-5" style={{ width: 350 }}>
            <h2 className="text-center">Login</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3 form-floating">
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        name="username"
                        value={username}
                        placeholder="email"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="username" className="form-label">email</label>
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        value={password}
                        placeholder="password"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="password" className="form-label">password</label>
                </div>
                <div className="text-danger">{error}</div>
                <div className="text-center">
                    <button type="submit" className="w-100 btn btn-primary">Sign in</button>
                </div>
                <div className="text-center mt-3">
                    <a href="/signup" className="text-decoration-none">Create account</a>
                    <span className="mx-2 text-muted">|</span>
                    <a href="/forgot-password" className="text-decoration-none">Forgot password</a>
                </div>
            </form>
        </div>

    );
}

export default Login;