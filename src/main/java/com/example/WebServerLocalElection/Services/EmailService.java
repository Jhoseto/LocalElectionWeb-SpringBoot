package com.example.WebServerLocalElection.Services;

import com.example.WebServerLocalElection.Models.UserEntity;

public interface EmailService {


    void sendRegistrationEmail(UserEntity userEntity);

    void sendSimpleEmail(String to, String subject, String text);

    void sendVoteEmail(String userEmail, int candidateCode);

    String getCandidateName(int candidateCode);

    String getCurrentDateTime();
}
