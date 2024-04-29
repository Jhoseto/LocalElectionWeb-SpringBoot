package com.example.WebServerLocalElection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;
    @Autowired
    private static VoteRequest voteRequest;
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(User user) {
        String subject = "МЕСТНИ ИЗБОРИ 2023 Регистрационна форма";
        String verificationCode = userService.getVerificationCodeByEmail(user.getEmail());
        String text = "Вашата регистрация в платформа за провеждане на местни избори е УСПЕШНА.\n" +
                "Вашите данни са:\n" +
                "Име: " + user.getName() + "\n" +
                "Години: " + user.getAge() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Парола: " + user.getPassword() + "\n" +
                "Верификационен КОД: " + verificationCode + "\n" +
                "--------------------------------------------------------\n\n" +
                "За да влезнете в системата използвайте вашият Имейл и Парола.\n" +
                "След това въведете в полето вашият уникален верификационен КОД, с който\n" +
                "ще имате право да упражните правото си на глас САМО ВЕДНЪЖ.\n" +
                "Бъдете разумни !\n" +
                "Вашият глас вече има значение !";

        sendSimpleEmail(user.getEmail(), subject, text);
        System.out.println("Send registration email to " + user.getEmail());
    }

    private void sendSimpleEmail(String to, String subject, String text) {
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

    private String getCandidateName(int candidateCode) {
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

    public String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}


