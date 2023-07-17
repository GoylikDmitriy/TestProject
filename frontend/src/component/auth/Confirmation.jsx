import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import axios from "axios";
import React, {useEffect, useState} from "react";
import InfoModal from "../modal/InfoModal";

export default function Confirmation() {
    const navigate = useNavigate();
    const params = useParams();
    const token = params.token;

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');

    useEffect(() => {
        axios.get('/registrationConfirm', { params: { token: token } })
            .then((res) => {
                if (res.status === 200) {
                    navigate('/login');
                }
            })
            .catch((error) => {
                if (error.response && error.response.status === 400) {
                    alert(error.response.data);
                    setMessageInfo(error.response.data);
                    setIsInfoModalOpen(true);
                } else if (error.response.status === 403) {
                    navigate(`/token/${token}`, {state: {allowed: true, expired: true}});
                }
            });
    });

    if (isInfoModalOpen) {
        return (
            <div>
                <InfoModal message={messageInfo}
                           onClose={() => setIsInfoModalOpen(false)}/>
            </div>
        );
    }
}