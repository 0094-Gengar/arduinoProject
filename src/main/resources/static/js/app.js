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
            // 상태를 'zone_on' 또는 'zone_off'로 변환
            const status = data.status ? 'zone_on' : 'zone_off';
            updateZoneUI(zoneId, status); // UI 업데이트
        })
        .catch(error => console.error('Error:', error));
}

// WebSocket 연결 설정
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // 구역 상태 업데이트 수신
    stompClient.subscribe('/topic/zoneStatus', function (message) {
        const statusData = JSON.parse(message.body); // 예: { "zoneId": "zone_a", "status": true }

        // 상태를 'zone_on' 또는 'zone_off'로 변환
        const status = statusData.status ? 'zone_on' : 'zone_off';
        updateZoneUI(statusData.zoneId, status);
    });
});

// 구역 UI 업데이트 함수
function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    if (status === 'zone_on') {
        button.style.backgroundColor = '#4CAF50'; // 활성화 상태 (초록색)
    } else if (status === 'zone_off') {
        button.style.backgroundColor = '#ccc'; // 비활성화 상태 (회색)
    }
}
