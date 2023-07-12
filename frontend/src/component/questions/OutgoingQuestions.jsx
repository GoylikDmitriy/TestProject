import React, {useEffect, useRef, useState} from 'react';
import axios from "axios";
import AddQuestionModal from "./modal/AddQuestionModal";
import EditQuestionModal from "./modal/EditQuestionModal";
import ConfirmationModal from "../modal/ConfirmationModal";
import InfoModal from "../modal/InfoModal";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Header from "../common/Header";

var stompClient;
export default function OutgoingQuestions() {
    const username = localStorage.getItem('username');
    const token = localStorage.getItem('token');

    const [page, setPage] = useState(1);

    const [questions, setQuestions] = useState([]);
    const [totalPages, setTotalPages] = useState('');
    const [pageSize, setPageSize] = useState(0);
    const [openConnect, setOpenConnect] = useState(false);

    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(Array(questions.length).fill(false));
    const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(Array(questions.length).fill(false));

    const [toUserError, setToUserError] = useState('');
    const [questionError, setQuestionError] = useState('');
    const [optionsError, setOptionsError] = useState('');

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');

    useEffect(() => {
        axios.get(`/questions/outgoing/${page}`,
            {
                headers: {
                    "Authorization": `username:${username};token:${token}`,
                }
            })
            .then((res) => {
                setQuestions(res.data.content);
                setTotalPages(res.data.totalPages);
                setPageSize(res.data.size);
                if (!openConnect) {
                    setOpenConnect(true);
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }, [page]);

    const clickPrevHandler = (event) => {
        const prevPage = page - 1;
        if (prevPage > 0) {
            setPage(prevPage);
        }
    }

    const clickNextHandler = (event) => {
        const nextPage = page + 1;
        if (nextPage <= totalPages) {
            setPage(nextPage);
        }
    }

    const clickPageHandler = (event, page) => {
        setPage(page);
    }

    const getOptions = (question) => {
        let options = null;
        if (question.options) {
            options = [];
            for (let i = 0; i < question.options.length; i++) {
                options.push({option: question.options[i]});
            }
        }

        return options;
    }

    useEffect(() => {
        if (openConnect) {
            setConnection();
        }
    }, [openConnect]);

    const setConnection = () => {
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(Sock);
        stompClient.connect({}, onConnected, onError);
    }

    const onConnected = () => {
        stompClient.subscribe('/user/' + username + '/answer', onAddEditMessageReceived);
        stompClient.subscribe('/user/' + username + '/answer-delete', onDeleteMessageReceived);
    }

    const onAddEditMessageReceived = (payload) => {
        const payloadData = JSON.parse(payload.body);
        const questionId = payloadData.question.id;
        setQuestions(prevQuestions => prevQuestions.map(question =>
            question.id === questionId ? {...question, answer: payloadData} : question
        ));
    }

    const onDeleteMessageReceived = (payload) => {
        const payloadData = JSON.parse(payload.body);
        const questionId = payloadData.question.id;
        setQuestions(prevQuestions => prevQuestions.map(question =>
            question.id === questionId ? {...question, answer: null} : question
        ));
    }

    const onError = (error) => {
        console.log(error);
    }

    const addQuestionSubmit = async (question, clearData) => {
        try {
            const response =
                await sendQuestionRequest('/questions/add', question);
            if (response.status === 200) {
                const newQuestion = response.data;
                if (stompClient) {
                    stompClient.send('/app/question-add', {}, JSON.stringify(newQuestion.id))
                }

                if (questions.length < pageSize) {
                    setQuestions([...questions, newQuestion]);
                } else {
                    setTotalPages(totalPages + 1);
                }

                clearData();
                setIsAddModalOpen(false);
                setMessageInfo("Question was added successfully.")
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response?.status === 400) {
                handleErrors(error.response.data);
            }

            console.log(error);
        }
    }

    const editQuestionSubmit = async (question) => {
        try {
            const response =
                await sendQuestionRequest('/questions/edit', question);
            if (response.status === 200) {
                const newQuestion = response.data;
                if (stompClient) {
                    stompClient.send('/app/question-edit', {}, JSON.stringify(newQuestion.id))
                }

                setQuestions(prevQuestions => prevQuestions.map(question =>
                    question.id === newQuestion.id ? newQuestion : question
                ));

                setIsEditModalOpen(Array(questions.length).fill(false));
                setMessageInfo("Question was updated successfully.")
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response?.status === 400) {
                handleErrors(error.response.data);
            }

            console.log(error);
        }
    }

    const sendQuestionRequest = async (url, question) => {
        return await axios.post(
            url,
            {
                id: question.id ? question.id : null,
                question: question.question,
                answerType: question.answerType,
                options: getOptions(question),
                answer: question.answer ? question.answer : null,
                toUser: {
                    email: question.email,
                },
            },
            {
                headers: {
                    "Authorization": `username:${username};token:${token}`,
                }
            });
    }

    const handleErrors = (errors) => {
        setToUserError('');
        setQuestionError('');
        setOptionsError('');
        for (let i = 0; i < errors.length; i++) {
            const {defaultMessage, field} = errors[i];
            switch (field) {
                case "toUser":
                    setToUserError(defaultMessage);
                    break;
                case "question":
                    setQuestionError(defaultMessage);
                    break;
                case "options":
                    setOptionsError(defaultMessage);
                    break;
                default:
                    break;
            }
        }
    }

    const deleteHandler = async (questionId) => {
        try {
            if (stompClient) {
                stompClient.send('/app/question-delete', {}, JSON.stringify(questionId))
            }

            const response = await axios.post(
                '/questions/delete',
                {
                    id: questionId,
                },
                {
                    headers: {
                        "Authorization": `username:${username};token:${token}`,
                    }
                }
            );

            if (response.status === 200) {
                setQuestions(prevQuestions => prevQuestions.filter(question => question.id !== questionId));
                setMessageInfo("Question has been deleted.");
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            console.log(error);
        }

        setIsConfirmModalOpen(Array(questions.length).fill(false));
    }

    return (
        <div>
            <div>
                <Header/>
            </div>
            <div className="container mt-5">
                <div>
                    <div className="row">
                        <div className="col d-flex justify-content-between align-items-center">
                            <h4>Your questions</h4>
                            <button type="button" className="btn btn-primary"
                                    onClick={() => setIsAddModalOpen(true)}>
                                Add question
                            </button>
                            {isAddModalOpen && (
                                <div>
                                    <AddQuestionModal toUserError={toUserError} questionError={questionError}
                                                      optionsError={optionsError} onSubmit={addQuestionSubmit}
                                                      onClose={() => setIsAddModalOpen(false)}
                                    />
                                </div>)}
                        </div>
                    </div>
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th>Receiver</th>
                            <th>Question</th>
                            <th>Answer Type</th>
                            <th>Answer</th>
                        </tr>
                        </thead>
                        <tbody>
                        {questions.length === 0 ? (
                            <div>
                                <label>No questions found</label>
                            </div>
                        ) : (
                            questions.map((q, index) => (
                                <tr key={q.id}>
                                    <td>{q.toUser.email}</td>
                                    <td>{q.question}</td>
                                    <td>{q.answerType}</td>
                                    <td>{q.answer ? q.answer.answer : ""}</td>
                                    <td>
                                        <div className="btn-group">
                                            <button className="btn btn-primary"
                                                    onClick={() => {
                                                        const newIsEditModalOpen = Array(questions.length).fill(false);
                                                        newIsEditModalOpen[index] = true;
                                                        setIsEditModalOpen(newIsEditModalOpen);
                                                    }}>
                                                Edit
                                            </button>
                                            {isEditModalOpen[index] && (
                                                <div>
                                                    <EditQuestionModal questionId={q.id} questionError={questionError}
                                                                       optionsError={optionsError}
                                                                       onSubmit={editQuestionSubmit}
                                                                       onClose={() => {
                                                                           setIsEditModalOpen(Array(questions.length).fill(false));
                                                                       }}
                                                    />
                                                </div>)}
                                            <button className="btn btn-danger"
                                                    onClick={() => {
                                                        const newIsConfirmModal = Array(questions.length).fill(false);
                                                        newIsConfirmModal[index] = true;
                                                        setIsConfirmModalOpen(newIsConfirmModal);
                                                    }}
                                            >
                                                Delete
                                            </button>
                                            {isConfirmModalOpen[index] && (
                                                <ConfirmationModal message={'Are you sure you want to delete question?'}
                                                                   onYes={() => deleteHandler(q.id)}
                                                                   onNo={() => {
                                                                       setIsConfirmModalOpen(Array(questions.length).fill(false));
                                                                   }}
                                                />
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            )))}
                        </tbody>
                    </table>
                    {isInfoModalOpen && (
                        <div>
                            <InfoModal message={messageInfo}
                                       onClose={() => setIsInfoModalOpen(false)}/>
                        </div>
                    )}
                    {questions.length !== 0 && (
                        <nav aria-label="Page navigation">
                            <ul className="pagination">
                                <li className="page-item">
                                    <a className="page-link" href="#" onClick={clickPrevHandler}>Previous</a>
                                </li>
                                {[...Array(totalPages)].map((_, index) => (
                                    <li key={index + 1} className={`page-item ${page === index + 1 ? 'active' : ''}`}>
                                        <a className="page-link" href="#" onClick={
                                            (event) => clickPageHandler(event, index + 1)
                                        }>{index + 1}</a>
                                    </li>
                                ))}
                                <li className="page-item">
                                    <a className="page-link" href="#" onClick={clickNextHandler}>Next</a>
                                </li>
                            </ul>
                        </nav>
                    )}
                </div>
            </div>
        </div>
    );
}