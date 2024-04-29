package com.example.WebServerLocalElection.Controllers;


import com.example.WebServerLocalElection.Models.UserEntity;
import com.example.WebServerLocalElection.Services.ServicesImpl.EmailServiceImpl;
import com.example.WebServerLocalElection.Services.ServicesImpl.UserServiceImpl;
import com.example.WebServerLocalElection.ViewModels.LoginViewModel;
import com.example.WebServerLocalElection.ViewModels.VerificationViewModel;
import com.example.WebServerLocalElection.ViewModels.VoteViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@Controller
public class ActionsController {

    private final UserServiceImpl userServiceImpl;
    private final EmailServiceImpl emailServiceImpl;

    @Autowired
    public ActionsController(UserServiceImpl userServiceImpl, EmailServiceImpl emailServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.emailServiceImpl = emailServiceImpl;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity userEntity) {
        String email = userEntity.getEmail();

        if (userServiceImpl.isEmailAlreadyRegistered(email)) {
            System.out.println(HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        } else {

            userServiceImpl.createUser(userEntity);
            System.out.println("New user registered - " + userEntity.getName() + " " + userEntity.getEmail());

            // Изпратете имейл чрез emailService
            emailServiceImpl.sendRegistrationEmail(userEntity);

            return ResponseEntity.ok().body("NEW User registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginViewModel loginViewModel) {
        String email = loginViewModel.getEmail();
        String password = loginViewModel.getPassword();

        // Проверка на валидността на потребителските данни
        boolean isValidUser = userServiceImpl.isValidUser(email, password);

        if (isValidUser) {
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verification(@RequestBody VerificationViewModel verificationViewModel) {
        String verificationCode = verificationViewModel.getCode();

        // Проверка на валидността на потребителските данни
        boolean isValidCode = userServiceImpl.isValidCode(verificationCode);

        if (isValidCode) {
            return ResponseEntity.ok().body("Valid Code");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Code");
        }
    }
    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody VoteViewModel voteViewModel) {
        String verificationCode = voteViewModel.getVerificationCode();
        int candidateCode = voteViewModel.getCandidateCode();

        System.out.println(verificationCode +" "+candidateCode);
        // Проверка на валидността на верификационния код
        boolean isValidCode = userServiceImpl.isValidCode(verificationCode);

        if (isValidCode) {
            // Проверка дали потребителят е вече гласувал
            boolean hasVoted = userServiceImpl.hasUserVoted(verificationCode);

            if (!hasVoted) {
                // Обновяване на гласа за кандидата в базата данни
                userServiceImpl.voteForCandidate(candidateCode, verificationCode);

                // Изпращане на имейл до потребителя
                String userEmail = userServiceImpl.getEmailByVerificationCode(verificationCode);
                if (userEmail != null) {
                    emailServiceImpl.sendVoteEmail(userEmail, candidateCode);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Грешка при изпращане на имейл: потребителят не е намерен.");
                }

                return ResponseEntity.ok().body("Гласуването е успешно.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Потребителят вече е гласувал.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалиден код.");
        }
    }

    @GetMapping("/getInfo")
    public ResponseEntity<List<UserEntity>> getAllUsersInfo() {
        // Извличане на всички данни от базата данни
        List<UserEntity> allUserEntityInfo = userServiceImpl.getAllUserInfo();

        // Извеждане на данните в конзолата
        System.out.println("All User Information:");
        for (UserEntity userEntity : allUserEntityInfo) {
            System.out.println("User ID: " + userEntity.getId());
            System.out.println("Name: " + userEntity.getName());
            System.out.println("Email: " + userEntity.getEmail());

        }

        if (!allUserEntityInfo.isEmpty()) {
            return ResponseEntity.ok().body(allUserEntityInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

