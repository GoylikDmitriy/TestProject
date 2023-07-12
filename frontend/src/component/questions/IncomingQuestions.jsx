import React, {useEffect, useState} from 'react';
import axios from "axios";
import AnswerQuestionModal from "./modal/AnswerQuestionModal";
import InfoModal from "../modal/InfoModal";
import SockJS from 'sockjs-client';
import Stomp, {overWS} from 'stompjs';
import Header from "../common/Header";

var stompClient;
export default function IncomingQuestions() {
    const username = localStorage.getItem('username');
    const token = localStorage.getItem('token');

    const [page, setPage] = useState(1);

    const [questions, setQuestions] = useState([]);
    const [totalPages, setTotalPages] = useState('');
    const [pageSize, setPageSize] = useState(0);
    const [openConnect, setOpenConnect] = useState(false);

    const [answerError, setAnswerError] = useState('');

    const [isAnswerModalOpen, setIsAnswerModalOpen] = useState(Array(questions.length).fill(false));

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');

    useEffect(() => {
        axios.get(`/questions/incoming/${page}`,
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

    const clickPrevHandler = () => {
        const prevPage = page - 1;
        if (prevPage > 0) {
            setPage(prevPage);
        }
    }

    const clickNextHandler = () => {
        const nextPage = page + 1;
        if (nextPage <= totalPages) {
            setPage(nextPage);
        }
    }

    const clickPageHandler = (event, page) => {
        setPage(page);
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
        stompClient.subscribe('/user/' + username + '/question-add', onAddMessageReceived);
        stompClient.subscribe('/user/' + username + '/question-edit', onEditMessageReceived);
        stompClient.subscribe('/user/' + username + '/question-delete', onDeleteMessageReceived);
    }

    const onAddMessageReceived = (payload) => {
        const questionToAdd = JSON.parse(payload.body);
        if (questions.length < pageSize) {
            setQuestions(prevQuestions => [...prevQuestions, questionToAdd]);
        } else {
            setTotalPages(totalPages + 1);
        }
    }

    const onEditMessageReceived = (payload) => {
        const questionToEdit = JSON.parse(payload.body);
        setQuestions(prevQuestions => prevQuestions.map(question =>
            question.id === questionToEdit.id ? questionToEdit : question
        ));
    }

    const onDeleteMessageReceived = (payload) => {
        const questionToDelete = JSON.parse(payload.body);
        setQuestions(prevQuestions =>
            prevQuestions.filter(question => question.id !== questionToDelete.id)
        );
    }

    const onError = (error) => {
        console.log(error);
    }

    const answerQuestionSubmit = async (isEditMode, answer) => {
        let url = '/answer';
        let message;
        if (isEditMode) {
            url += '/edit';
            message = 'Answer has been updated.';
        } else {
            url += '/add';
            message = 'Answer has been added';
        }

        try {
            const response = await sendAnswerRequest(url, answer);
            if (response.status === 200) {
                const answer = response.data;
                if (stompClient) {
                    stompClient.send('/app/answer', {}, JSON.stringify(answer.id))
                }

                const questionId = answer.question.id;
                setQuestions(prevQuestions => prevQuestions.map(question =>
                    question.id === questionId ? {...question, answer: answer} : question
                ));

                setMessageInfo(message)
                setIsInfoModalOpen(true);
            }
        } catch (error) {
            if (error.response?.status === 400) {
                setAnswerError(error.response.data);
            }

            console.log(error);
        }

        setIsAnswerModalOpen(Array(questions.length).fill(false));
    }

    const sendAnswerRequest = async (url, answer) => {
        return await axios.post(
            url,
            {
                id: answer.id,
                answer: answer.answer,
                question: {
                    id: answer.questionId,
                },
            },
            {
                headers: {
                    "Authorization": `username:${username};token:${token}`,
                }
            }
        );
    }

    const deleteHandler = async (answerId, questionId) => {
        try {
            if (stompClient) {
                stompClient.send('/app/answer-delete', {}, JSON.stringify(answerId))
            }

            const response = await axios.post(
                '/answer/delete',
                {
                    id: answerId,
                },
                {
                    headers: {
                        "Authorization": `username:${username};token:${token}`,
                    }
                }
            );

            if (response.status === 200) {
                setQuestions(prevQuestions => prevQuestions.map(question =>
                    question.id === questionId ? {...question, answer: null} : question
                ));

                setMessageInfo("Answer has been deleted.")
                setIsInfoModalOpen(true);
                setIsAnswerModalOpen(Array(questions.length).fill(false));
            }
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div>
            <div>
                <Header/>
            </div>
            <div className="container mt-5">
                <div>
                    <div className="row">
                        <div className="col">
                            <h4>Answer the question</h4>
                        </div>
                    </div>
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th>Sender</th>
                            <th>Question</th>
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
                                    <td>{q.fromUser.email}</td>
                                    <td>{q.question}</td>
                                    <td>{q.answer ? q.answer.answer : ""}</td>
                                    <td>
                                        <div className="btn-group">
                                            <button className="btn btn-primary"
                                                    onClick={() => {
                                                        const newIsAnswerModalOpen = Array(questions.length).fill(false);
                                                        newIsAnswerModalOpen[index] = true;
                                                        setIsAnswerModalOpen(newIsAnswerModalOpen);
                                                    }}
                                            >
                                                Answer
                                            </button>
                                            {isAnswerModalOpen[index] && (
                                                <div>
                                                    <AnswerQuestionModal answerError={answerError}
                                                                         questionId={q.id}
                                                                         onSubmit={answerQuestionSubmit}
                                                                         onClose={() =>
                                                                             setIsAnswerModalOpen(Array(questions.length)
                                                                                 .fill(false)
                                                                             )}
                                                                         onDelete={deleteHandler}
                                                    />
                                                </div>)}
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