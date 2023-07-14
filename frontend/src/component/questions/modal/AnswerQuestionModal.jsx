import React, {useEffect, useState} from 'react';
import axios from "axios";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ConfirmationModal from "../../modal/ConfirmationModal";
import InfoModal from "../../modal/InfoModal";

const AnswerQuestionModal = ({answerError, questionId, onSubmit, onClose, onDelete}) => {
    const token = localStorage.getItem('token');

    const [types, setTypes] = useState(['']);

    const [email, setEmail] = useState('');
    const [question, setQuestion] = useState('');
    const [answerType, setAnswerType] = useState('');
    const [answer, setAnswer] = useState('');
    const [answerId, setAnswerId] = useState(null);
    const [answerOptions, setAnswerOptions] = useState([{
        id:'',
        option:'',
    }]);
    const [options, setOptions] = useState([{
        id: '',
        option: '',
    }]);

    const [isAnswerSet, setIsAnswerSet] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

    const [typeOption, setTypeOption] = useState(0);

    const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
    const [messageInfo, setMessageInfo] = useState('');


    useEffect(() => {
        axios.get('/questions/answer-types',
            {
                headers: {
                    "Authorization": token,
                }
            })
            .then((res) => {
                setTypes(res.data);
            })
    }, []);

    useEffect(() => {
        loadQuestion();
    }, [])

    const loadQuestion = async () => {
        const response = await axios.get(`/questions/get/${questionId}`,
            {
                headers: {
                    "Authorization": token,
                }
            });

        setEmail(response.data.fromUser.email);
        setQuestion(response.data.question);
        setAnswerType(response.data.answerType);
        const ans = response.data.answer;
        setAnswer(ans ? ans.answer : '');
        setAnswerId(ans ? ans.id : null);
        setAnswerOptions(ans?.options ? ans.options : []);
        setIsAnswerSet(ans);

        setOptions(response.data.options);
    }

    useEffect(() => {
        setTypeOption(types.indexOf(answerType) + 1);
    }, [answerType]);

    const getField = () => {
        const id = "answer";
        const formControl = "form-control";
        const placeHolder = "answer";
        const onChange = (e) => setAnswer(e.target.value);

        switch (typeOption) {
            case 1:
                return (
                    <input
                        type={"text"}
                        id={id}
                        className={formControl}
                        placeholder={placeHolder}
                        value={answer}
                        onChange={onChange}
                    />
                );
            case 2:
                return (
                    <textarea
                        id={id}
                        className={formControl}
                        rows={5}
                        placeholder={placeHolder}
                        value={answer}
                        onChange={onChange}
                    />
                );
            case 3:
                return (
                    <div>
                        {options.map((option, index) => (
                            <div className="form-check" key={index}>
                                <label className="form-check-label">
                                    <input
                                        type="radio"
                                        name={id}
                                        value={option.option}
                                        checked={answer === option.option}
                                        onChange={(e) => {
                                            setAnswerOptions(Array.of(option));
                                            onChange(e);
                                        }}
                                        className="form-check-input mr-1"
                                    />
                                    {option.option}
                                </label>
                            </div>
                        ))}
                    </div>
                );
            case 4:
                return (
                    <div>
                        {options.map((option, index) => (
                            <div className="form-check" key={index}>
                                <label className="form-check-label">
                                    <input
                                        type="checkbox"
                                        name={id}
                                        value={option.option}
                                        checked={answer.split('\n').includes(option.option)}
                                        onChange={(e) => {
                                            const checked = e.target.checked;
                                            let updatedAnswer = answer;
                                            let newOptions = answerOptions;

                                            if (updatedAnswer.length !== 0 && !updatedAnswer.endsWith('\n')) {
                                                updatedAnswer += '\n';
                                            }

                                            if (checked) {
                                                updatedAnswer += option.option + '\n';
                                                //setAnswerOptions(prevOptions => [...prevOptions, option]);
                                                newOptions.push(option);
                                            } else {
                                                updatedAnswer = updatedAnswer
                                                    .replace(option.option + '\n', '');
                                                newOptions = newOptions.filter(op => op.id !== option.id);
                                            }

                                            setAnswer(updatedAnswer);
                                            setAnswerOptions(newOptions)
                                        }}
                                        className="form-check-input mr-1"
                                    />
                                    {option.option}
                                </label>
                            </div>
                        ))}
                    </div>
                );
            case 5:
                return (
                    <div>
                        <select
                            id={id}
                            className={formControl}
                            value={answer}
                            onChange={(e) => {
                                const optionId = e.target.options[e.target.selectedIndex].getAttribute("data-key");
                                setAnswerOptions(Array.of({ id: optionId, option: e.target.value }));
                                onChange(e);
                            }}
                        >
                            {options.map((option, index) => (
                                <option key={index} value={option.option} data-key={option.id}>
                                    {option.option}
                                </option>
                            ))}
                        </select>
                    </div>
                );
            case 6:
                return (
                    <div>
                        <DatePicker
                            id={id}
                            className={formControl}
                            selected={answer ? new Date(answer) : null}
                            onChange={(date) => setAnswer(date.toLocaleDateString(
                                'en-US',
                                {day: '2-digit', month: 'short', year: 'numeric'})
                            )}
                            placeholderText={placeHolder}
                            dateFormat={"MMM dd, yyyy"}
                            showYearDropdown
                            yearDropdownItemNumber={100}
                            scrollableYearDropdown
                            maxDate={new Date()}
                        />
                    </div>
                );

            default:
                return null;
        }
    };

    const editHandler = (event) => {
        setIsAnswerSet(false);
        setIsEditMode(true);
    }

    return (
        <div className="modal fade show" style={{display: 'block'}} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content text-bg-secondary">
                    <div className="modal-header">
                        <h5 className="modal-title">Answer question</h5>
                        <button type="button" className="btn-close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">From user</label>
                            <span className="form-control text-bg-secondary" id="email">{email}</span>
                        </div>
                        <div className="mb-3">
                            <label htmlFor="question" className="form-label">Question</label>
                            <span className="form-control text-bg-secondary" id="question">{question}</span>
                        </div>
                        <div className="mb-3">
                            {!isAnswerSet ? getField() :
                                <div>
                                    <span className="form-control text-bg-dark" id="question">{answer}</span>
                                </div>
                            }
                            <div className="text-danger">{answerError}</div>
                        </div>
                    </div>
                    <div className="modal-footer d-flex justify-content-between">
                        {!isAnswerSet && (
                            <div>
                                <button type="button" className="btn btn-success"
                                        onClick={() => {
                                            onSubmit(
                                                isEditMode,
                                                {
                                                    id: answerId,
                                                    answer: answer,
                                                    answerOptions: answerOptions.length === 0 ? null : answerOptions,
                                                    questionId: questionId,
                                                });

                                            setIsEditMode(false);
                                        }}
                                >
                                    Submit
                                </button>
                                <button type={"button"} className={"btn btn-dark m-2"}
                                        onClick={onClose}
                                >
                                    Cancel
                                </button>
                            </div>
                        )}
                        {isAnswerSet && (
                            <div>
                                <button type="button" className="btn btn-primary" onClick={editHandler}>
                                    Edit
                                </button>
                                <button type="button" className="btn btn-danger m-2"
                                        onClick={() => setIsConfirmModalOpen(true)}
                                >
                                    Delete
                                </button>
                                {isConfirmModalOpen && (
                                    <ConfirmationModal message={'Are you sure you want to delete answer?'}
                                                       onYes={() => {
                                                           setAnswer('');
                                                           setAnswerId(null);
                                                           setIsAnswerSet(false);
                                                           setIsConfirmModalOpen(false);
                                                           onDelete(answerId, questionId)
                                                       }}
                                                       onNo={() => setIsConfirmModalOpen(false)}
                                    />
                                )}
                            </div>
                        )}
                    </div>
                    {isInfoModalOpen && (
                        <div>
                            <InfoModal message={messageInfo}
                                       onClose={() => setIsInfoModalOpen(false)}/>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default AnswerQuestionModal;