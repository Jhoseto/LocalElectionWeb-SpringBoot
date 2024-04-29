package com.example.WebServerLocalElection.Services;

import com.example.WebServerLocalElection.Models.UserEntity;

import java.util.List;

public interface UserService {

    void createUser(UserEntity userEntity);

    boolean isEmailAlreadyRegistered(String email);

    boolean isValidUser(String email, String password);

    boolean isValidCode(String code);

    String getVerificationCodeByEmail(String email);


    boolean hasUserVoted(String verificationCode);


    void voteForCandidate(int candidateCode, String verificationCode);

    String getEmailByVerificationCode(String verificationCode);

    List<UserEntity> getAllUserInfo();
}
