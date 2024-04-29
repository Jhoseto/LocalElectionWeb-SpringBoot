package com.example.WebServerLocalElection.Services.ServicesImpl;

import com.example.WebServerLocalElection.Services.EmailService;
import com.example.WebServerLocalElection.Models.UserEntity;
import com.example.WebServerLocalElection.ViewModels.VoteViewModel;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private UserServiceImpl userServiceImpl;
    private static VoteViewModel voteViewModel;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserServiceImpl userServiceImpl) {
        this.javaMailSender = javaMailSender;
        this.userServiceImpl = userServiceImpl;
    }



    @Override
    public void sendRegistrationEmail(UserEntity userEntity) {
        String subject = "МЕСТНИ ИЗБОРИ 2023 Регистрационна форма";
        String verificationCode = userServiceImpl.getVerificationCodeByEmail(userEntity.getEmail());
        String text = "Вашата регистрация в платформа за провеждане на местни избори е УСПЕШНА.\n" +
                "Вашите данни са:\n" +
                "Име: " + userEntity.getName() + "\n" +
                "Години: " + userEntity.getAge() + "\n" +
                "Email: " + userEntity.getEmail() + "\n" +
                "Парола: " + userEntity.getPassword() + "\n" +
                "Верификационен КОД: " + verificationCode + "\n" +
                "--------------------------------------------------------\n\n" +
                "За да влезнете в системата използвайте вашият Имейл и Парола.\n" +
                "След това въведете в полето вашият уникален верификационен КОД, с който\n" +
                "ще имате право да упражните правото си на глас САМО ВЕДНЪЖ.\n" +
                "Бъдете разумни !\n" +
                "Вашият глас вече има значение !";

        sendSimpleEmail(userEntity.getEmail(), subject, text);
        System.out.println("Send registration email to " + userEntity.getEmail());
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            System.out.println("Email successfully sent to: " + to);
        } catch (MailException e) {
            System.out.println("Failed to send email to: " + to);
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendVoteEmail(String userEmail, int candidateCode) {
        String subject = "МЕСТНИ ИЗБОРИ 2023 Упражнен Вот";
        String candidateName = getCandidateName(candidateCode);
        String text = "Вашият глас е подаден успешно в системата за гласуване!\n" +
                "------------------------------------------------------\n" +
                "Избраният от вас кандидат за кмет е: " + candidateName + "\n" +
                "Време и Дата на гласуване: " + getCurrentDateTime();

        sendSimpleEmail(userEmail, subject, text);
        System.out.println("Send VOTE email to " + userEmail);
    }

    @Override
    public String getCandidateName(int candidateCode) {
        String candidaName = "";
        switch (candidateCode) {
            case 1:
                candidaName = "Михал Камбарев";
                break;
            case 2:
                candidaName = "Николай Мелемов";
                break;
            case 3:
                candidaName = "Стефан Сабрутев";
                break;
        }
        return candidaName;
    }

    @Override
    public String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}


