package com.example.WebServerLocalElection.ViewModels;

public class VerificationViewModel {
    private String code;
    private int vote;


    public VerificationViewModel(String code,
                                 int vote) {
        this.code = code;
        this.vote = vote;
    }

    public String getCode() {
        return code;
    }

    public VerificationViewModel setCode(String code) {
        this.code = code;
        return this;
    }

    public int getVote() {
        return vote;
    }

    public VerificationViewModel setVote(int vote) {
        this.vote = vote;
        return this;
    }
}
