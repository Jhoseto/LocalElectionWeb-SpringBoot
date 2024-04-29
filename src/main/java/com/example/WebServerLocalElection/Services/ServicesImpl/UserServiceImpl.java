package com.example.WebServerLocalElection.Services.ServicesImpl;


import com.example.WebServerLocalElection.Models.UserEntity;
import com.example.WebServerLocalElection.Repository.UserRepository;
import com.example.WebServerLocalElection.Services.CodeGenerator;
import com.example.WebServerLocalElection.Services.UserService;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CodeGenerator codeGenerator) {
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void createUser(UserEntity userEntity) {
        if (!userRepository.existsByEmail(userEntity.getEmail())) {
            // Генериране на код
            String generatedCode = codeGenerator.generateCode();
            userEntity.setCode(generatedCode);
            userEntity.setVote(0);
            userRepository.save(userEntity);

        }
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        // Проверка дали имейлът вече е регистриран в базата данни
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isValidUser(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email);

        // Потребител със съответния имейл и парола съществува в системата
        return userEntity != null && userEntity.getPassword().equals(password);
    }

    @Override
    public boolean isValidCode(String code) {
        UserEntity verCode = userRepository.findByCode(code);
        return verCode != null;
    }

    @Override
    public String getVerificationCodeByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            return userEntity.getCode();
        } else {
            return null;
        }
    }

    // Проверка дали потребителят е вече гласувал
    @Override
    public boolean hasUserVoted(String verificationCode) {
        UserEntity userEntity = userRepository.findByCode(verificationCode);
        return userEntity != null && userEntity.getVote() != 0;
    }

    // Обновяване на гласа за кандидата в базата данни
    @Override
    public void voteForCandidate(int candidateCode, String verificationCode) {
        UserEntity userEntity = userRepository.findByCode(verificationCode);
        if (userEntity != null) {
            userEntity.setVote(candidateCode); // Записва гласа за кандидата
            userRepository.save(userEntity); // Запазва промените в базата данни
        } else {
            // Обработка на грешка, ако потребителят не е намерен
            throw new RuntimeException("Потребителят не е намерен с код: " + verificationCode);
        }
    }

    @Override
    public String getEmailByVerificationCode(String verificationCode) {
        UserEntity userEntity = userRepository.findByCode(verificationCode);
        if (userEntity != null) {
            return userEntity.getEmail();
        } else {
            return null;
        }
    }

    @Override
    public List<UserEntity> getAllUserInfo() {
        // Извличане на всички потребители от базата данни
        List<UserEntity> allUserEntities = userRepository.findAll();

        System.out.println("Total users retrieved: " + allUserEntities.size());
        for (UserEntity userEntity : allUserEntities) {
            System.out.println("User ID: " + userEntity.getId());
            System.out.println("Name: " + userEntity.getName());
            System.out.println("Email: " + userEntity.getEmail());

        }
        return allUserEntities;
    }
}
