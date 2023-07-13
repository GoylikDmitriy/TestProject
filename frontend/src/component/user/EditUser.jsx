import 'bootstrap/dist/css/bootstrap.min.css';
import {useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import InfoModal from "../modal/InfoModal";
import Header from "../common/Header";

export default function EditUser() {
    const token = localStorage.getItem('token');

    let navigate = useNavigate();

    const [firstNameError, setFirstNameError] = useState("");
    const [lastNameError, setLastNameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [confirmedPasswordError, setConfirmedPasswordError] = useState("");
    const [phoneNumberError, setPhoneNumberError] = useState("");

    const [user, setUser] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmedPassword: "",
        phoneNumber: "",
    });

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');


    const handleInputChange = (event) => {
        setUser({
            ...user,
            [event.target.name]: event.target.value
        });
    }

    useEffect(() => {
        loadUser();
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(
                `/user/edit`,
                user,
                {
                    headers: {
                        "Authorization": token,
                    }
                });

            if (response.status === 200) {
                setMessageInfo("Your profile has been updated.");
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response) {
                handleErrors(error.response.data);
            }
        }
    }

    const handleErrors = (errors) => {
        setFirstNameError("");
        setLastNameError("");
        setEmailError("");
        setPasswordError("");
        setPhoneNumberError("");
        setConfirmedPasswordError("");
        for (let i = 0; i < errors.length; i++) {
            const {defaultMessage, field} = errors[i];
            switch (field) {
                case "firstName":
                    setFirstNameError(defaultMessage);
                    break;
                case "lastName":
                    setLastNameError(defaultMessage);
                    break;
                case "email":
                    setEmailError(defaultMessage);
                    break;
                case "password":
                    setPasswordError(defaultMessage);
                    break;
                case "phoneNumber":
                    setPhoneNumberError(defaultMessage);
                    break;
                case "confirmedPassword":
                    setConfirmedPasswordError(defaultMessage);
                    break;
                default:
                    break;
            }
        }
    }

    const {firstName, lastName, email, password, confirmedPassword, phoneNumber} = user;

    const loadUser = async () => {
        const response = await axios.get(`/user`,
            {
                headers: {
                    "Authorization": token,
                }
            });

        setUser(response.data);
    };

    return (
        <div>
            <div>
                <Header/>
            </div>
            <div className="container">
                <h2>Edit User</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="firstName" className="form-label">First name</label>
                        <input
                            type="text"
                            className="form-control"
                            id="firstName"
                            name="firstName"
                            value={firstName}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{firstNameError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="lastName" className="form-label">Last name</label>
                        <input
                            type="text"
                            className="form-control"
                            id="lastName"
                            name="lastName"
                            value={lastName}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{lastNameError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            className="form-control"
                            id="email"
                            name="email"
                            value={email}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{emailError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="phoneNumber" className="form-label">Phone number</label>
                        <input
                            type="text"
                            className="form-control"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={phoneNumber}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{phoneNumberError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Current password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            name="password"
                            value={password}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{passwordError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="confirmedPassword" className="form-label">New password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="confirmedPassword"
                            name="confirmedPassword"
                            value={confirmedPassword}
                            onChange={handleInputChange}
                        />
                        <div className="text-danger">{confirmedPasswordError}</div>
                    </div>
                    <button type="submit" className="btn btn-primary">Edit</button>
                </form>
                {isInfoModalOpen && (
                    <div>
                        <InfoModal message={messageInfo}
                                   onClose={() => {
                                       setIsInfoModalOpen(false);
                                       navigate('/');
                                   }}/>
                    </div>
                )}
            </div>
        </div>
    );
}