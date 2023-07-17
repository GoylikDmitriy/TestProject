import React from "react";
import Header from "./common/Header";

export default function Home() {
    return (
        <div>
            <Header />
            <div className="container mt-5">
                <h2 className="text-center">Welcome to the Questions Portal</h2>
            </div>
            <div className="row justify-content-center mt-5">
                <div className="col-md-5">
                    <a href="/questions/outgoing" className="card-link text-decoration-none">
                        <div className="card text-center">
                            <div className="card-body">
                                <h5 className="card-title">Ask questions</h5>
                                <p className="card-text">Here you can ask questions to other users.</p>
                            </div>
                        </div>
                    </a>
                </div>
                <div className="col-md-5">
                    <a href="/questions/incoming" className="card-link text-decoration-none">
                        <div className="card text-center">
                            <div className="card-body">
                                <h5 className="card-title">Answer the questions</h5>
                                <p className="card-text">Here you can answer the questions that other users have asked you.</p>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    );
}
