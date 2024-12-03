// 실시간 시간 갱신 함수
function updateTime() {
    const now = new Date();
    const options = {
        month: 'long',
        day: 'numeric',
        weekday: 'short',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    };
    const formattedTime = now.toLocaleString('ko-KR', options).replace(',', ' ');
    document.getElementById('date-time').innerText = formattedTime;
}

// 1초마다 시간 갱신
setInterval(updateTime, 1000);
updateTime(); // 즉시 실행

// 구역 활성화/비활성화 상태 변경 및 버튼 색상 변경
function toggleZone(zoneId) {
    console.log(`Toggling zone: ${zoneId}`); // 디버깅 로그

    fetch(`/zones/${zoneId}/toggle`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            console.log('Response Data:', data);               // 전체 데이터 확인
            console.log('Status Type:', typeof data.status);   // 타입 확인
            console.log('Status Value:', `"${data.status}"`);  // 값 확인 (공백 포함 여부 확인)

            // 상태를 'zone_on' 또는 'zone_off'로 변환
            const status = data.status.trim() === 'zone_on' ? 'zone_on' : 'zone_off';
            console.log(`Zone ${zoneId} updated to: ${status}`); // 상태 변경 로그
            updateZoneUI(zoneId, status); // UI 업데이트
        })
        .catch(error => console.error('Error in toggleZone:', error));
}

// WebSocket 연결 설정 (변수 이름을 'socketClient'로 변경)
const socketClient = new SockJS('/ws');
const stompClient = Stomp.over(socketClient);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame); // 연결 성공 로그

    // 구역 상태 업데이트 수신
    stompClient.subscribe('/topic/zoneStatus', function (message) {
        const statusData = JSON.parse(message.body); // 예: { "zoneId": "zone_b", "status": "zone_off" }

        // status 값이 정확히 처리되도록 수정
        const status = statusData.status === 'zone_on' ? 'zone_on' : 'zone_off'; // 정확히 비교

        console.log(`Updating zone ${statusData.zoneId} to: ${status}`); // 상태 변경 로그

        updateZoneUI(statusData.zoneId, status); // 상태에 맞게 UI 업데이트
    });
});

// 구역 UI 업데이트 함수
function updateZoneUI(zoneId, status) {
    console.log(`Updating UI for zone: ${zoneId}, status: ${status}`); // UI 업데이트 로그

    const button = document.getElementById(`${zoneId}_btn`);
    if (status === 'zone_on') {
        button.style.backgroundColor = '#4CAF50'; // 활성화 상태 (초록색)
    } else if (status === 'zone_off') {
        button.style.backgroundColor = '#ccc'; // 비활성화 상태 (회색)
    }
}
