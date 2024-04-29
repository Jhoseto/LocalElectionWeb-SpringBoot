package com.example.WebServerLocalElection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            // Генериране на код
            String generatedCode = CodeGenerator.generateCode();
            user.setCode(generatedCode);
            user.setVote(0);
            userRepository.save(user);

        }
    }

    public boolean isEmailAlreadyRegistered(String email) {
        // Проверка дали имейлът вече е регистриран в базата данни
        return userRepository.existsByEmail(email);
    }

    public boolean isValidUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true; // Потребител със съответния имейл и парола съществува в системата
        }
        return false; // Потребител със съответния имейл и парола не съществува в системата
    }
    public boolean isValidCode(String code) {
        User verCode = userRepository.findByCode(code);

        return verCode != null;
    }


    public String getVerificationCodeByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getCode();
        } else {
            return null;
        }
    }

    // Проверка дали потребителят е вече гласувал
    public boolean hasUserVoted(String verificationCode) {
        User user = userRepository.findByCode(verificationCode);
        return user != null && user.getVote() != 0;
    }

    // Обновяване на гласа за кандидата в базата данни
    public void voteForCandidate(int candidateCode, String verificationCode) {
        User user = userRepository.findByCode(verificationCode);
        if (user != null) {
            user.setVote(candidateCode); // Записва гласа за кандидата
            userRepository.save(user); // Запазва промените в базата данни
        } else {
            // Обработка на грешка, ако потребителят не е намерен
            throw new RuntimeException("Потребителят не е намерен с код: " + verificationCode);
        }
    }

    public String getEmailByVerificationCode(String verificationCode) {
        User user = userRepository.findByCode(verificationCode);
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }

    public List<User> getAllUserInfo() {
        // Извличане на всички потребители от базата данни
        List<User> allUsers = userRepository.findAll();

        // Дебъг отпечатвания
        System.out.println("Total users retrieved: " + allUsers.size());
        for (User user : allUsers) {
            System.out.println("User ID: " + user.getId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            // Други данни, които искате да изпечатате
        }

        return allUsers;
    }
}
