document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('fetchDataButton').addEventListener('click', function() {
        fetchDataFromTable();
    });
});

function fetchDataFromTable() {
    fetch('https://localhost:8443/info', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Network response was not ok.');
    })
        .then(data => {
        const tableBody = document.querySelector('#infoTable tbody');
        tableBody.innerHTML = '';

        if (data && data.length > 0) {
            data.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.code}</td>
                    <td>${user.vote}</td>
                `;
                tableBody.appendChild(row);
            });

            let votesCount = [0, 0, 0, 0];
            data.forEach(user => {
                votesCount[user.vote]++;
            });

            drawBarChart(votesCount);
        } else {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="2">No data to display</td>`;
            tableBody.appendChild(row);
        }
    })
        .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
        const tableBody = document.querySelector('#infoTable tbody');
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="2">Error fetching data from server</td>`;
        tableBody.appendChild(row);
    });
}

function drawBarChart(percentageVotes) {
    const totalVotes = percentageVotes.reduce((total, votes) => total + votes, 0);
    const percentageData = percentageVotes.map(votes => ((votes / totalVotes) * 100).toFixed(2) + '%');

    const data = {
        labels: ['Без глас', 'М.Камбарев', 'Н.Мелемов', 'С.Сабрутев'],
        datasets: [{
            label: 'Гласове (%)',
            data: percentageVotes,
            backgroundColor: [
                'rgba(128, 128, 128, 0.2)', // Сиво
                'rgba(255, 206, 86, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(75, 192, 192, 0.2)'
            ],
            borderColor: [
                'rgba(128, 128, 128, 0.2)',
                'rgba(128, 128, 128, 0.2)',
                'rgba(128, 128, 128, 0.2)',
                'rgba(128, 128, 128, 0.2)'
            ],
            borderWidth: 1
        }]
    };

    const config = {
        type: 'bar',
        data: data,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            label += context.parsed.y + ' гласа, ' + percentageData[context.dataIndex];
                            return label;
                        }
                    }
                },
                datalabels: {
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    formatter: function(value, context) {
                        return percentageData[context.dataIndex];
                    }
                }
            }
        }
    };

    const myChart = new Chart(
        document.getElementById('myChart'),
        config
    );
}


