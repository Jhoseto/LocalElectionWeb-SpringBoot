document.addEventListener("DOMContentLoaded", function() {
    const submitButton = document.querySelector('button[type="submit"]');
    const voteMessages = document.getElementById('voteMessages');

    submitButton.addEventListener("click", function() {
        const selectedCandidate = document.querySelector('input[name="candidate"]:checked');

        if (selectedCandidate) {
            const candidateCode = selectedCandidate.value;
            const verificationCode = sessionStorage.getItem("verificationCode");

            sendVoteToServer(candidateCode, verificationCode);
        } else {
            displayMessage("Моля, изберете кандидат преди да гласувате.", 'red');
        }
    });

    function displayMessage(message, color) {
        voteMessages.innerText = message;
        voteMessages.style.color = color;
    }

    function sendVoteToServer(candidateCode, verificationCode) {
        fetch('https://localhost:8443/vote', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ candidateCode: candidateCode, verificationCode: verificationCode }),
        })
            .then(response => {
            if (response.ok) {
                displayMessage("Вашият вот е ОТЧЕТЕН УСПЕШНО! Проверете вашата поща за по-подробна информация", 'green');
            } else {
                displayMessage('Верификационният код е вече използван. Вашият вот НЕ Е ОТЧЕТЕН! ' + response.statusText, 'red');
            }
        })
            .catch(error => {
            displayMessage('Грешка: ' + error, 'red');
        });
    }

    const candidates = document.querySelectorAll('.candidate');

    candidates.forEach(candidate => {
        const candidateImage = candidate.querySelector('img');

        candidateImage.addEventListener('click', function(event) {
            const candidateURL = candidate.getAttribute('data-url');
            window.open(candidateURL, '_blank'); // Отваряне на линка в нов прозорец
        });

        candidate.addEventListener('click', function(event) {
            event.stopPropagation(); // Предотвратяване на извикването на събитието от родителския елемент

            // Увеличаване на избрания кандидат
            this.style.transform = 'scale(1.5)';
            this.style.transition = 'transform 0.5s ease';

            // Връщане на останалите кандидати в началната им позиция
            candidates.forEach(otherCandidate => {
                if (otherCandidate !== candidate) {
                    otherCandidate.style.transform = 'scale(1)';
                    otherCandidate.style.transition = 'transform 0.5s ease';
                }
            });
        });
    });
});
