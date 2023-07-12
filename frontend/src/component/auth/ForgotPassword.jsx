import axios from "axios";
import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import InfoModal from "../modal/InfoModal";


export default function ForgotPassword() {
    const [email, setEmail] = useState("");
    const [emailError, setEmailError] = useState("");

    const navigate = useNavigate();

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');


    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            axios.get('/forgot-password', {params: {email: email}})
                .then((res) => {
                    if (res.status === 200) {
                        localStorage.setItem('email', email);
                        setMessageInfo("The link to reset password has been sent to your email.");
                        setIsInfoModalOpen(true);
                    }
                })
                .catch((error) => {
                    if (error.response && error.response.status === 400) {
                        setEmailError(error.response.data);
                    } else {
                        console.log(error);
                    }
                });
        } catch (error) {
            console.log(error);
        }
    }

    const handleInputChange = async (event) => {
        setEmail(event.target.value);
    }

    return (
        <div className={"container mt-5"}>
            <div className="col-md-6 offset-md-3">
                <div className={"mb-3 needs-validation"}>
                    <input
                        onChange={handleInputChange}
                        id={"email"}
                        name={"email"}
                        type={"text"}
                        className={`form-control ${emailError ? 'is-invalid' : ''}`}
                        placeholder={"email"}
                        value={email}
                    />
                    <div className="invalid-feedback">{emailError}</div>
                </div>
                <div>
                    <button
                        className={"btn btn-primary form-control"}
                        type={"button"}
                        onClick={handleSubmit}>
                        Reset password
                    </button>
                </div>
                {isInfoModalOpen && (
                    <div>
                        <InfoModal message={messageInfo}
                                   onClose={() => {
                                       navigate('/login');
                                       setIsInfoModalOpen(false)
                                   }}
                        />
                    </div>
                )}
            </div>
        </div>
);
}