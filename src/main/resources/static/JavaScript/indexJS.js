

var nameInput = document.getElementById("Name");
var ageInput = document.getElementById("Age");
var emailInput = document.getElementById("Email");
var passwordInput = document.getElementById("Password");
var confirmPasswordInput = document.getElementById("ConfirmPassword");


function validateForm() {

    if (nameInput.validity.patternMismatch) {
        nameError.textContent = "Моля, въведете трите ви имена на кирилица, като всяко от да започва с главна буква.";
        nameError.style.color = "red";
    } else {
        nameError.textContent = "";
    }


    if (ageInput.validity.rangeUnderflow) {
        ageError.textContent = "Трябва да имате навършени 18 години, за да се регистрирате.";
        ageError.style.color = "red";
    } else {
        ageError.textContent = "";
    }


    if (emailInput.validity.typeMismatch) {
        emailError.textContent = "Моля, въведете валиден имейл адрес.";
        emailError.style.color = "red";
    } else {
        emailError.textContent = "";
    }


    if (passwordInput.validity.patternMismatch) {
        passwordError.textContent = "Паролата трябва да е на латиница, като съдържа поне 6 символа, включително поне една главна буква и поне една цифра.";
        passwordError.style.color = "red";
    } else {
        passwordError.textContent = "";
    }


    if (confirmPasswordInput.validity.patternMismatch) {
        confirmPassError.textContent = "Паролите не съвпадат.";
        confirmPassError.style.color = "red";
    } else {
        confirmPassError.textContent = "";
    }
    return true;
}


document.addEventListener("DOMContentLoaded", function() {
    var form = document.getElementById("registrationForm");
    form.addEventListener("input", validateForm);
    form.addEventListener("change", validateForm);
});

// Изпращане на Регистрационната форма
function submitForm() {
    // Извикване на функцията за валидация
    if (!validateForm()) {
        registrationMessages.innerText = "Моля, попълнете всички полета.";
        registrationMessages.style.color = "red";
        return false; // Прекратява изпращането на формата
    }

    // Извличане на данните от полетата на формата
    var name = document.getElementById("Name").value;
    var age = document.getElementById("Age").value;
    var email = document.getElementById("Email").value;
    var password = document.getElementById("Password").value;
    var confirmPassword = document.getElementById("ConfirmPassword").value;

    // Проверка за празни полета
    if (name === "" || age === "" || email === "" || password === "" || confirmPassword === "") {
        registrationMessages.innerText = "Моля, попълнете всички полета.";
        registrationMessages.style.color = "red";
        return false; // Прекратява изпращането на формата
    }

    var formData = {
        name: name,
        age: age,
        email: email,
        password: password,
        _csrf: csrfToken // Включване на CSRF токена във формата
    };

    // Изпращане на данните към сървъра
    fetch('https://localhost:8443/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {
        if (response.ok) {
            console.log('Response:', response);
            document.getElementById('registrationMessages').innerText = 'Регистрацията е успешна! Моля, проверете вашата електронна поща за повече информация!';
            document.getElementById('registrationMessages').style.color = 'green';
            // Изчистване на полетата на формата
            document.getElementById('Name').value = '';
            document.getElementById('Age').value = '';
            document.getElementById('Email').value = '';
            document.getElementById('Password').value = '';
            document.getElementById('ConfirmPassword').value = '';
        } else if (response.status === 400) {
            console.error('Error:', response.statusText);
            document.getElementById('registrationMessages').innerText = 'Регистрацията не беше успешна. Потребител с такъв имейл вече е регистриран.';
        } else {
            console.error('Error:', response.statusText);
            document.getElementById('registrationMessages').innerText = 'Регистрацията не беше успешна. Моля, опитайте отново по-късно.';
        }
    })
        .catch(error => {
        console.error('Error:', error);
        document.getElementById('registrationMessages').innerText = 'Регистрацията не беше успешна. Моля, опитайте отново по-късно.';
    });

    // Връща false, за да предотврати презареждането на страницата
    return false;
}



// Изпращане на Логин формата
function loginForm() {
    var email = document.getElementById("LoginEmail").value;
    var password = document.getElementById("LoginPassword").value;

    var formData = {
        email: email,
        password: password
    };

    fetch('https://localhost:8443/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {
        if (response.ok) {
            console.log('Response:', response);
            document.getElementById('loginMessages').innerText = 'Входът е успешен!';
            // Изчистване на полетата на формата
            document.getElementById('LoginEmail').value = '';
            document.getElementById('LoginPassword').value = '';
            document.getElementById('loginMessages').style.color = 'green'; // Зелен цвят за успешен вход

            // Пренасочване към страницата за потвърждение след успешен вход
            window.location.href = 'VerificationPage.html';
        } else if (response.status === 401) {
            console.error('Error:', response.statusText);
            document.getElementById('loginMessages').innerText = 'Грешен имейл или парола';
            document.getElementById('loginMessages').style.color = 'red'; // Червен цвят за грешка в имейл или парола
        } else {
            console.error('Error:', response.statusText);
            document.getElementById('loginMessages').innerText = 'Входът не беше успешен. Моля, опитайте отново по-късно.';
            document.getElementById('loginMessages').style.color = 'red'; // Червен цвят за неуспешен вход
        }
    })
        .catch(error => {
        console.error('Error:', error);
        document.getElementById('loginMessages').innerText = 'Входът не беше успешен. Моля, опитайте отново по-късно.';
        document.getElementById('loginMessages').style.color = 'red'; // Червен цвят за неуспешен вход
    });
}
