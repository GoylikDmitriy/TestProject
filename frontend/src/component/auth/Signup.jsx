import {useNavigate, useParams} from "react-router-dom";
import React, {useState} from "react";
import axios from "axios";
import InfoModal from "../modal/InfoModal";

export default function Signup() {
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

    const [token, setToken] = useState('');


    const handleInputChange = (event) => {
        setUser({
            ...user,
            [event.target.name]: event.target.value
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/signup', user);
            if (response.status === 200) {
                setToken(response.data);
                setMessageInfo("Check out your email, verification token has been sent.");
                setIsInfoModalOpen(true);
            }
        }
        catch (error) {
            if (error.response) {
                handleErrors(error.response.data);
            }

            console.log(error);
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

    return (
        <div className="container" style={{ width: 500 }}>
            <h2 className="text-center">Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3 form-floating">
                    <input
                        type="text"
                        className={`form-control ${firstNameError ? 'is-invalid' : ''}`}
                        id="firstName"
                        name="firstName"
                        value={firstName}
                        placeholder="First name"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="firstName" className="form-label">First name</label>
                    {firstNameError && <div className="invalid-feedback">{firstNameError}</div>}
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="text"
                        className={`form-control ${lastNameError ? 'is-invalid' : ''}`}
                        id="lastName"
                        name="lastName"
                        value={lastName}
                        placeholder="Last name"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="lastName" className="form-label">Last name</label>
                    {lastNameError && <div className="invalid-feedback">{lastNameError}</div>}
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="email"
                        className={`form-control ${emailError ? 'is-invalid' : ''}`}
                        id="email"
                        name="email"
                        value={email}
                        placeholder="Email"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="email" className="form-label">Email</label>
                    {emailError && <div className="invalid-feedback">{emailError}</div>}
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="tel"
                        className={`form-control ${phoneNumberError ? 'is-invalid' : ''}`}
                        id="phoneNumber"
                        name="phoneNumber"
                        value={phoneNumber}
                        placeholder="Phone number"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="phoneNumber" className="form-label">Phone number</label>
                    {phoneNumberError && <div className="invalid-feedback">{phoneNumberError}</div>}
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="password"
                        className={`form-control ${passwordError ? 'is-invalid' : ''}`}
                        id="password"
                        name="password"
                        value={password}
                        placeholder="Password"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="password" className="form-label">Password</label>
                    {passwordError && <div className="invalid-feedback">{passwordError}</div>}
                </div>
                <div className="mb-3 form-floating">
                    <input
                        type="password"
                        className={`form-control ${confirmedPasswordError ? 'is-invalid' : ''}`}
                        id="confirmedPassword"
                        name="confirmedPassword"
                        value={confirmedPassword}
                        placeholder="Confirm password"
                        onChange={handleInputChange}
                    />
                    <label htmlFor="confirmedPassword" className="form-label">Confirm password</label>
                    {confirmedPasswordError && <div className="invalid-feedback">{confirmedPasswordError}</div>}
                </div>
                <div className="text-center">
                    <button type="submit" className="btn btn-primary w-100">Sign up</button>
                </div>
                <div className="text-center mt-3">
                    <a href="/login">Already have an account</a>
                </div>
            </form>
            {isInfoModalOpen && (
                <div>
                    <InfoModal
                        message={messageInfo}
                        onClose={() => {
                            setIsInfoModalOpen(false);
                            navigate(`/token/${token}`, { state: { allowed: true } });
                        }}
                    />
                </div>
            )}
        </div>
    );
}