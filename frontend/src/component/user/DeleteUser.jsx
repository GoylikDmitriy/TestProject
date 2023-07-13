import axios from "axios";
import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import ConfirmationModal from "../modal/ConfirmationModal";
import InfoModal from "../modal/InfoModal";
import Header from "../common/Header";


export default function DeleteUser() {
    const navigate = useNavigate();

    const token = localStorage.getItem('token');
    const [password, setPassword] = useState("");
    const [passwordError, setPasswordError] = useState("");

    const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');


    const deleteHandler = async (event) => {
        event.preventDefault();

        try {
            const response = await axios.post(
                `/user/delete`,
                {
                    password: password,
                },
                {
                    headers: {
                        "Authorization": token,
                    }
                });

            if (response.status === 200) {
                localStorage.clear();
                setMessageInfo(response.data);
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response) {
                setPasswordError(error.response.data);
            }

            console.log(error);
        }

        setIsConfirmModalOpen(false);
    }

    const handleInputChange = async (event) => {
        setPassword(event.target.value);
    }

    return (
        <div>
            <div>
                <Header/>
            </div>
            <div className={"container mt-5"}>
                <div className={"mb-3 needs-validation"}>
                    <input
                        id={"password"}
                        name={"password"}
                        type={"password"}
                        placeholder={"password"}
                        className={`form-control ${passwordError ? 'is-invalid' : ''}`}
                        value={password}
                        onChange={handleInputChange}
                    />
                    <div className="invalid-feedback">{passwordError}</div>
                </div>
                <div>
                    <button type={"button"}
                            className={"btn btn-primary form-control"}
                            onClick={() => setIsConfirmModalOpen(true)}
                    >
                        Delete
                    </button>
                    {isConfirmModalOpen && (
                        <ConfirmationModal message={'Are you sure you want to delete account?'}
                                           onYes={deleteHandler}
                                           onNo={() => setIsConfirmModalOpen(false)}
                        />
                    )}
                </div>
                {isInfoModalOpen && (
                    <div>
                        <InfoModal message={messageInfo}
                                   onClose={() => {
                                       setIsInfoModalOpen(false);
                                       navigate('/login');
                                   }}/>
                    </div>
                )}
            </div>
        </div>
    );
}