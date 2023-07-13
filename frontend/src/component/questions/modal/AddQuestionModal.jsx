import React, {useEffect, useState} from 'react';
import axios from "axios";

const AddQuestionModal = ({ toUserError, questionError, optionsError, onSubmit, onClose }) => {
    const token = localStorage.getItem('token');
    const [types, setTypes] = useState(['']);

    const [email, setEmail] = useState('');
    const [question, setQuestion] = useState('');
    const [answerType, setAnswerType] = useState('');
    const [options, setOptions] = useState('');

    const [selectedOption, setSelectedOption] = useState('');

    useEffect(() => {
        axios.get('/questions/answer-types', {
            headers: {
                "Authorization": token,
            }})
            .then((res) => {
                setTypes(res.data);
                setAnswerType(res.data[0]);
            })
    }, []);

    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value);
        setAnswerType(types[event.target.value - 1]);
    };

    const handleOptionsInput = (event) => {
        setOptions(event.target.value);
    }

    const clearData = () => {
        setEmail('');
        setQuestion('');
        setAnswerType('');
        setOptions('');
    }

    const getOptions = (options) => {
        if (selectedOption === '3' || selectedOption === '4' || selectedOption === '5') {
            return options ? options.split('\n') : null;
        }

        return null;
    }

    return (
        <div className="modal fade show" style={{ display: 'block' }} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content text-bg-secondary">
                    <div className="modal-header">
                        <h5 className="modal-title">Add question</h5>
                        <button type="button" className="btn-close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">For user</label>
                        <input type="email" className="form-control" id="email" value={email}
                               onChange={(e) => setEmail(e.target.value)}/>
                        <div className="text-danger">{toUserError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="question" className="form-label">Question</label>
                        <input type="text" className="form-control" id="question" value={question}
                               onChange={(e) => setQuestion(e.target.value)}/>
                        <div className="text-danger">{questionError}</div>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="answerType" className="form-label">Answer type</label>
                        <select
                            className="form-select"
                            id="answerType"
                            aria-label="Select an option"
                            value={selectedOption}
                            onChange={handleSelectChange}
                        >
                            {types.map((type, index) => (
                                <option key={index + 1} value={index + 1}>{type}</option>
                            ))}
                        </select>
                    </div>
                    {selectedOption && (selectedOption === '3' || selectedOption === '4' || selectedOption === '5') && (
                        <div className="mb-3">
                            <label htmlFor="options" className="form-label">Options</label>
                            <textarea
                                rows={5}
                                placeholder={"option1\noption2\noption3\n..."}
                                className="form-control"
                                id="options"
                                value={options}
                                onChange={handleOptionsInput}
                            >
                            </textarea>
                            <div className="text-danger">{optionsError}</div>
                        </div>
                    )}
                </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-dark" onClick={onClose}>
                            Close
                        </button>
                        <button type="button"
                                onClick={() => onSubmit({
                                    email: email,
                                    question: question,
                                    answerType: answerType,
                                    options: getOptions(options),
                                }, clearData)}
                                className="btn btn-success">
                            Add question
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddQuestionModal;
