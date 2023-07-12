import React from "react";
import Header from "./common/Header";

export default function Home() {
    return (
        <div>
            <Header/>
            <div className={"container mt-5"}>
                <h2>Welcome to questions portal</h2>
            </div>
        </div>
    );
}
