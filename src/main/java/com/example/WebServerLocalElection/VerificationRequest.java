package com.example.WebServerLocalElection;

public class VerificationRequest {
    private String code;
    private int vote;
    public VerificationRequest() {};

    public VerificationRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
