import React, {useEffect, useState} from 'react';
import axios from "axios";

const EditQuestionModal = ({ questionId, questionError, optionsError, onSubmit, onClose }) => {
    const token = localStorage.getItem('token');

    const [types, setTypes] = useState(['']);

    const [selectedOption, setSelectedOption] = useState('');

    const [question, setQuestion] = useState('');
    const [answerType, setAnswerType] = useState('');
    const [options, setOptions] = useState([{
        id: '',
        option: ''
    }]);
    const [optionsValue, setOptionsValue] = useState('');
    const [email, setEmail] = useState('');
    const [answer, setAnswer] = useState('');

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

        setEmail(response.data.toUser.email);
        setQuestion(response.data.question);
        setAnswerType(response.data.answerType);
        setAnswer(response.data.answer);
        setOptions(response.data.options);

        //setOptionsValue(options.join('\n'));
        response.data.options.forEach(option => {
            setOptionsValue(prevState => prevState ?
                prevState + '\n' + option.option :
                prevState + option.option);
        });
    }

    useEffect(() => {
        setSelectedOption(getTypeValue(answerType).toString());
    }, [answerType]);

    const handleQuestionInput = (event) => {
        setQuestion(event.target.value);
    }

    const handleOptionsInput = (event) => {
        setOptionsValue(event.target.value);
    }

    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value);
        setAnswerType(types[event.target.value - 1]);
    }

    const getTypeValue = (answerType) => types.indexOf(answerType) + 1;

    const getOptions = (optionsValue) => {
        let updatedOptions = [{
            id: '',
            option: '',
        }];

        if (selectedOption === '3' || selectedOption === '4' || selectedOption === '5') {
            if (optionsValue) {
                const splitOptions = optionsValue.split('\n');
                for (let i = 0; i < splitOptions.length; i++) {
                    let id = null;
                    if (i < options.length) {
                        id = options[i].id;
                    }

                    updatedOptions[i] = { id:id, option:splitOptions[i] };
                }
            }
            else {
                updatedOptions = null;
            }

            return updatedOptions;
        }

        return null;
    }

    return (
        <div className="modal fade show" style={{ display: 'block' }} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content text-bg-secondary">
                    <div className="modal-header">
                        <h5 className="modal-title">Edit question</h5>
                        <button type="button" className="btn-close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">For user</label>
                            <span className="form-control text-bg-dark" id="email">{email}</span>
                        </div>
                        <div className="mb-3">
                            <label htmlFor="question" className="form-label">Question</label>
                            <input type="text" className="form-control" id="question" value={question}
                                   onChange={handleQuestionInput}/>
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
                                    value={optionsValue}
                                    onChange={handleOptionsInput}
                                >
                                </textarea>
                                <div className="text-danger">{optionsError}</div>
                            </div>
                        )}
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-dark" onClick={onClose}>
                            Cancel
                        </button>
                        <button type="button"
                                onClick={() => onSubmit({
                                    id: questionId,
                                    email: email,
                                    question: question,
                                    answerType: answerType,
                                    options: getOptions(optionsValue),
                                    answer: answer,
                                })}
                                className="btn btn-success">
                            Save
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default EditQuestionModal;