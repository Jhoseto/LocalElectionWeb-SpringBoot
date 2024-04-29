package com.example.WebServerLocalElection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;



@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String email = user.getEmail();

        if (userService.isEmailAlreadyRegistered(email)) {
            System.out.println(HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        } else {

            userService.createUser(user);
            System.out.println("New user registered - " + user.getName() + " " + user.getEmail());

            // Изпратете имейл чрез emailService
            emailService.sendRegistrationEmail(user);

            return ResponseEntity.ok().body("NEW User registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Проверка на валидността на потребителските данни
        boolean isValidUser = userService.isValidUser(email, password);

        if (isValidUser) {
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verification(@RequestBody VerificationRequest verificationRequest) {
        String verificationCode = verificationRequest.getCode();

        // Проверка на валидността на потребителските данни
        boolean isValidCode = userService.isValidCode(verificationCode);

        if (isValidCode) {
            return ResponseEntity.ok().body("Valid Code");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Code");
        }
    }
    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody VoteRequest voteRequest) {
        String verificationCode = voteRequest.getVerificationCode();
        int candidateCode = voteRequest.getCandidateCode();

        System.out.println(verificationCode +" "+candidateCode);
        // Проверка на валидността на верификационния код
        boolean isValidCode = userService.isValidCode(verificationCode);

        if (isValidCode) {
            // Проверка дали потребителят е вече гласувал
            boolean hasVoted = userService.hasUserVoted(verificationCode);

            if (!hasVoted) {
                // Обновяване на гласа за кандидата в базата данни
                userService.voteForCandidate(candidateCode, verificationCode);

                // Изпращане на имейл до потребителя
                String userEmail = userService.getEmailByVerificationCode(verificationCode);
                if (userEmail != null) {
                    emailService.sendVoteEmail(userEmail, candidateCode);
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

    @GetMapping("/info")
    public ResponseEntity<List<User>> getAllUsersInfo() {
        // Извличане на всички данни от базата данни
        List<User> allUserInfo = userService.getAllUserInfo();

        // Извеждане на данните в конзолата
        System.out.println("All User Information:");
        for (User user : allUserInfo) {
            System.out.println("User ID: " + user.getId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            // Други данни, които искате да изпечатате
        }

        if (!allUserInfo.isEmpty()) {
            return ResponseEntity.ok().body(allUserInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

