// src/main/resources/static/js/app.js

// 실시간 시간 갱신 함수
function updateTime() {
    const now = new Date();
    const options = { month: 'long', day: 'numeric', weekday: 'short', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    const formattedTime = now.toLocaleString('ko-KR', options).replace(',', ' ');
    document.getElementById('date-time').innerText = formattedTime;
}

// 1초마다 시간 갱신
setInterval(updateTime, 1000);
updateTime(); // 즉시 실행

// 구역 활성화/비활성화 상태 변경 및 버튼 색상 변경
function toggleZone(zoneId) {
    fetch(`/zones/${zoneId}/toggle`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            const button = document.getElementById(`${zoneId}_btn`);
            if (data.status) {
                button.style.backgroundColor = '#4CAF50'; // 활성화 상태 (초록색)
            } else {
                button.style.backgroundColor = '#ccc'; // 비활성화 상태 (회색)
            }
        })
        .catch(error => console.error('Error:', error));
}
