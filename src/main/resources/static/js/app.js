// 서버로부터 상태를 변경하고, 즉시 UI를 반영하는 함수
function toggleZone(zoneId) {
    const currentButton = document.getElementById(`${zoneId}_btn`);
    const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';
    const newStatus = currentStatus === 'zone_on' ? 'zone_off' : 'zone_on';

    updateZoneUI(zoneId, newStatus); // 즉시 UI 반영

    // 서버에 상태 변경 요청 (POST)
    fetch(`/zones/${zoneId}/toggle`, { method: 'POST' })
        .then(response => response.json())
        .then(data => updateZoneUI(zoneId, data.status)) // 서버 응답 기반 UI 갱신
        .catch(error => console.error('Error:', error));
}

// 서버 상태를 주기적으로 확인하여 UI를 업데이트하는 함수
function checkAndUpdateZoneStatus(zoneId) {
    fetch(`/zones/${zoneId}`)
        .then(response => response.json())
        .then(data => {
            const newStatus = data.status;
            const currentButton = document.getElementById(`${zoneId}_btn`);
            const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';

            if ((newStatus === 'zone_on' && currentStatus !== 'zone_on') ||
                (newStatus === 'zone_off' && currentStatus !== 'zone_off')) {
                updateZoneUI(zoneId, newStatus);
            }
        })
        .catch(error => console.error('Error:', error));
}

// UI를 업데이트하는 함수 (버튼 색상 변경)
function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === 'zone_on' ? '#4CAF50' : '#ccc';
}

// 3초마다 상태를 확인하여 UI를 갱신하는 주기적 호출
setInterval(() => {
    checkAndUpdateZoneStatus("zone_a");
    checkAndUpdateZoneStatus("zone_b");
    checkAndUpdateZoneStatus("zone_c");
}, 2000);
