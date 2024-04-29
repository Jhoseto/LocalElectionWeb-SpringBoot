package com.example.WebServerLocalElection;

public class VoteRequest {
    private int candidateCode;
    private String verificationCode;

    public VoteRequest() {};

    public VoteRequest(int candidateCode, String verificationCode) {
        this.candidateCode = candidateCode;
        this.verificationCode = verificationCode;
    }

    public int getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(int candidateCode) {
        this.candidateCode = candidateCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
