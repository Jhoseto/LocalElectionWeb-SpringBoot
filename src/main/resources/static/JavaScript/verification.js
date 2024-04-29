function confirmVerification() {

    var verificationCode = document.getElementById("verificationCode").value;


    sessionStorage.setItem("verificationCode", verificationCode);

    var formData = {
        code: verificationCode
    };


    fetch('https://localhost:8443/verification', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {
        if (response.ok) {

            console.log('Verification successful');
            document.getElementById('VerificationMessages').innerText = 'Верификацията е успешена!';
            document.getElementById('VerificationMessages').style.color = 'green';
            document.getElementById('verificationCode').value = '';
            window.location.href = 'VotePage.html';
        } else {

            console.error('Verification failed:', response.statusText);
            document.getElementById('VerificationMessages').innerText = 'Грешен код! Моля, въведете правилен верификационен код.';
            document.getElementById('VerificationMessages').style.color = 'red';
        }
    })
        .catch(error => {

        console.error('Error:', error);
        document.getElementById('VerificationMessages').innerText = 'Сървърна грешка. Моля, опитайте отново по-късно.';
        document.getElementById('VerificationMessages').style.color = 'red';
    });
}
function returnToHomePage() {
    window.location.href = "index.html";
}