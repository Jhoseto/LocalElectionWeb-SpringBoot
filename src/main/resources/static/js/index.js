function validateForm() {
    var nameInput = document.getElementById("Name");
    var ageInput = document.getElementById("Age");
    var emailInput = document.getElementById("Email");
    var passwordInput = document.getElementById("Password");
    var confirmPasswordInput = document.getElementById("ConfirmPassword");

    var nameError = document.getElementById("nameError");
    var ageError = document.getElementById("ageError");
    var emailError = document.getElementById("emailError");
    var passwordError = document.getElementById("passwordError");
    var confirmPassError = document.getElementById("confirmPassError");

    if (nameInput.validity.patternMismatch) {
        nameError.textContent = "Моля, въведете трите си имена на кирилица, като всяко започва с главна буква.";
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
}
function checkPasswordMatch() {
    var password = document.getElementById("Password").value;
    var confirmPassword = document.getElementById("ConfirmPassword").value;

    if (password !== confirmPassword) {
        document.getElementById('confirmPassError').textContent = "Паролите не съвпадат.";
        document.getElementById('confirmPassError').style.color = "red";
    } else {
        document.getElementById('confirmPassError').textContent = "";
    }
}



function submitForm() {
    validateForm();

    var registrationMessages = document.getElementById("registrationMessages");
    var name = document.getElementById("Name").value;
    var age = document.getElementById("Age").value;
    var email = document.getElementById("Email").value;
    var password = document.getElementById("Password").value;
    var confirmPassword = document.getElementById("ConfirmPassword").value;

    if (name === "" || age === "" || email === "" || password === "" || confirmPassword === "") {
        registrationMessages.innerText = "Моля, попълнете всички полета.";
        registrationMessages.style.color = "red";
        return false;
    }

    var formData = {
        name: name,
        age: age,
        email: email,
        password: password,
    };

    fetch('https://192.168.1.3:2662/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {
            if (response.ok) {
                console.log('Response:', response);
                registrationMessages.innerText = 'Регистрацията е успешна! Моля, проверете вашата електронна поща за повече информация!';
                registrationMessages.style.color = 'green';
                document.getElementById('registrationForm').reset();
            } else if (response.status === 400) {
                console.error('Error:', response.statusText);
                registrationMessages.innerText = 'Регистрацията не беше успешна. Потребител с такъв имейл вече е регистриран.';
            } else {
                console.error('Error:', response.statusText);
                registrationMessages.innerText = 'Регистрацията не беше успешна. Моля, опитайте отново по-късно.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            registrationMessages.innerText = 'Регистрацията не беше успешна. Моля, опитайте отново по-късно.';
        });

    return false;
}

function loginForm() {
    var email = document.getElementById("LoginEmail").value;
    var password = document.getElementById("LoginPassword").value;

    var formData = {
        email: email,
        password: password
    };

    fetch('https://192.168.1.3:2662/login', {
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
                document.getElementById('LoginForm').reset();
                document.getElementById('loginMessages').style.color = 'green';
                window.location.href = 'verificationPage';
            } else if (response.status === 401) {
                console.error('Error:', response.statusText);
                document.getElementById('loginMessages').innerText = 'Грешен имейл или парола';
                document.getElementById('loginMessages').style.color = 'red';
            } else {
                console.error('Error:', response.statusText);
                document.getElementById('loginMessages').innerText = 'Входът не беше успешен. Моля, опитайте отново по-късно.';
                document.getElementById('loginMessages').style.color = 'red';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('loginMessages').innerText = 'Входът не беше успешен. Моля, опитайте отново по-късно.';
            document.getElementById('loginMessages').style.color = 'red';
        });
}
