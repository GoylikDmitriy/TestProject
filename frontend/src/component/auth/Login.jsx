import 'bootstrap/dist/css/bootstrap.min.css';
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
        <div className="container">
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="username" className="form-label">Email</label>
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        name="username"
                        value={username}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        value={password}
                        onChange={handleInputChange}
                    />
                </div>
                <div>{error && <p className="text-danger">{error}</p>}</div>
                <div>
                    <button type="submit" className="btn btn-primary">Sign in</button>
                </div>
                <div>
                    <a href={'/signup'}>Create account</a>
                    <a className={"m-3"} href={'/forgot-password'}>Forgot password</a>
                </div>
            </form>
        </div>
    );
}

export default Login;