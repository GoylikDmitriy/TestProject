package com.goylik.questionsPortal.questionsPortal.confirmation;

public enum ConfirmationType {
    REGISTRATION("/registrationConfirm"), DELETE("/user/deleteConfirm");

    private final String address;
    ConfirmationType(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
