// 구역 활성화/비활성화 상태 변경 및 버튼 색상 변경
function toggleZone(zoneId) {
    console.log(`Toggling zone: ${zoneId}`); // 디버깅 로그

    // 현재 버튼 상태를 즉시 업데이트 (UI 반영)
    const currentButton = document.getElementById(`${zoneId}_btn`);
    const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';
    const newStatus = currentStatus === 'zone_on' ? 'zone_off' : 'zone_on';

    updateZoneUI(zoneId, newStatus); // UI를 먼저 업데이트

    // 서버와 통신하여 상태 변경
    fetch(`/zones/${zoneId}/toggle`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            console.log('Response Data:', data);
            const status = data.status.trim() === 'zone_on' ? 'zone_on' : 'zone_off';
            // 서버에서 받은 응답에 따라 상태를 재확인하고 UI 업데이트
            updateZoneUI(zoneId, status);
        })
        .catch(error => {
            console.error('Error in toggleZone:', error);
            // 만약 서버 요청에 실패하면, UI를 원래 상태로 복구
            updateZoneUI(zoneId, currentStatus);
        });
}

// WebSocket 연결 설정 (변수 이름을 'socketClient'로 변경)
const socketClient = new SockJS('https://full-bedbug-0094-gengar-e52ccc05.koyeb.app/ws');
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
