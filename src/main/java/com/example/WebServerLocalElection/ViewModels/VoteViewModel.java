package com.example.WebServerLocalElection.ViewModels;

public class VoteViewModel {
    private int candidateCode;
    private String verificationCode;


    public VoteViewModel(int candidateCode,
                         String verificationCode) {
        this.candidateCode = candidateCode;
        this.verificationCode = verificationCode;
    }

    public int getCandidateCode() {
        return candidateCode;
    }

    public VoteViewModel setCandidateCode(int candidateCode) {
        this.candidateCode = candidateCode;
        return this;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public VoteViewModel setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }
}
