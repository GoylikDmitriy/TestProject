import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import InfoModal from "../modal/InfoModal";


export default function ResetPassword() {
    const [password, setPassword] = useState("");
    const [confirmedPassword, setConfirmedPassword] = useState("");

    const [passwordError, setPasswordError] = useState("");

    const params = useParams();
    const token = params.token;

    const navigate = useNavigate();

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');
    const [navigatePage, setNavigatePage] = useState('');


    useEffect(() => {
       axios.get('/isConfirmed', {params:{token:token}})
           .then((res) => {
               if (!res?.data) {
                   setMessageInfo("Token is not valid. " +
                       "Check whether you confirmed your email");
                   setIsInfoModalOpen(true);
                   setNavigatePage('-1');
               }
           })
    });

    const handleInputChange = (event) => {
        const {name, value} = event.target;
        if (name === "password") {
            setPassword(value);
        } else if (name === "confirmedPassword") {
            setConfirmedPassword(value);
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(
                '/reset-password',
                {
                    email: localStorage.getItem('email'),
                    password: password,
                    confirmedPassword: confirmedPassword,
                }
            );

            if (response.status === 200) {
                localStorage.clear();
                setNavigatePage('/login');
                setMessageInfo("Your password has been updated");
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response?.status === 400) {
                setPasswordError(error.response.data);
            }

            console.log(error);
        }
    }

    return (
        <div className="container mt-5" style={{width:500}}>
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h3 className="title text-center">Change Password</h3>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3 needs-validation form-floating">
                            <input
                                id="password"
                                name="password"
                                type="password"
                                className="form-control"
                                placeholder="Password"
                                value={password}
                                onChange={handleInputChange}
                            />
                            <label htmlFor={"password"} className="form-label">Password</label>
                        </div>
                        <div className="mb-3 needs-validation form-floating">
                            <input
                                id="confirmedPassword"
                                name="confirmedPassword"
                                type="password"
                                className="form-control"
                                placeholder="Confirm password"
                                value={confirmedPassword}
                                onChange={handleInputChange}
                            />
                            <label htmlFor={"confirmedPassword"} className="form-label">Confirm password</label>
                        </div>
                        <div className="text-danger">{passwordError}</div>
                        <div>
                            <button type="submit" className="btn btn-primary w-100">
                                Change password
                            </button>
                        </div>
                    </form>
                    {isInfoModalOpen && (
                        <div>
                            <InfoModal
                                message={messageInfo}
                                onClose={() => {
                                    setIsInfoModalOpen(false);
                                    navigate(navigatePage);
                                }}
                            />
                        </div>
                    )}
                </div>
            </div>
        </div>

    );
}