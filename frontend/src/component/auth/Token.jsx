import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useLocation, useNavigate, useParams} from "react-router-dom";

export default function Token() {
    const [showResendButton, setShowResendButton] = useState(false);
    const [timerValue, setTimerValue] = useState(30);

    const navigate = useNavigate();
    const params = useParams();

    const location = useLocation();
    const [expired, setExpired] = useState(false);

    const token = params.token;

    useEffect(() => {
        if (!location.state?.allowed) {
            navigate(-1);
        }

        if (location.state?.expired) {
            setExpired(true);
            setTimerValue(0);
        }
    });

    useEffect(() => {
        const timer = setInterval(() => {
            setTimerValue((prevValue) => prevValue - 1);
        }, 1000);

        return () => clearInterval(timer);
    }, []);

    useEffect(() => {
        const timer = setInterval(() => {
            axios.get('/isConfirmed', {params: {token: token}})
                .then((res) => {
                    if (res?.data) {
                        navigate('/login');
                    }
                })
        }, 1000);

        return () => clearInterval(timer);
    }, []);

    useEffect(() => {
        if (timerValue === 0) {
            setShowResendButton(true);
        }
    }, [timerValue]);

    const handleResendToken = async (event) => {
        event.preventDefault();
        await axios.get('/resendVerificationToken', {params: {token: token}})
            .then((res) => {
                if (res.status === 200) {
                    localStorage.setItem("token", res.data);
                }
            });
    }

    const formatTime = (seconds) => {
        return seconds % 60;
    }

    return (
        <div className="container">
            <h1>Resend token</h1>
            <h2>Dont leave this page until confirmation.</h2>
            {expired && (
                <p>Your token is expired. Click the button below to resend.</p>
            )}
            {!showResendButton && (
                <p>Resend button will appear in {formatTime(timerValue)} seconds.</p>
            )}
            {showResendButton && (
                <div>
                    <button className="btn btn-primary" type="button" onClick={handleResendToken}>Resend token</button>
                </div>
            )}
        </div>

    );
}
