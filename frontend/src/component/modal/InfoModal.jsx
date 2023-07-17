import React from 'react';

const InfoModal = ({message, onClose}) => {
    return (
        <div className="modal fade show" style={{display: 'block'}} tabIndex="-1">
            <div className="modal-dialog">
                <div className="modal-content text-bg-secondary">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5">Information</h1>
                        <button type="button" className="btn-close" aria-label="Close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        {message}
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-primary" onClick={onClose}>Ok</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default InfoModal;