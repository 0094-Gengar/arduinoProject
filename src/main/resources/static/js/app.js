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

function checkAndUpdateZoneStatus(zoneId) {
    fetch(`https://full-bedbug-0094-gengar-e52ccc05.koyeb.app/zones/${zoneId}`) // 절대 경로 사용
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json(); // JSON으로 변환
        })
        .then(data => {
            const newStatus = data === true ? 'zone_on' : 'zone_off'; // 응답이 true/false로 오기 때문에 상태 변환
            const currentButton = document.getElementById(`${zoneId}_btn`);
            const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';

            // 상태가 변경된 경우에만 UI 업데이트
            if (newStatus !== currentStatus) {
                updateZoneUI(zoneId, newStatus);
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === "zone_on" ? '#4CAF50' : '#ccc';

    // 현재 시간 표시
    const timeElement = document.getElementById(`${zoneId}_time`);
    const currentTime = new Date().toLocaleTimeString();
    timeElement.textContent = `Last updated: ${currentTime}`;
}

// 2초마다 상태 확인
setInterval(() => {
    checkAndUpdateZoneStatus("zone_a");
    checkAndUpdateZoneStatus("zone_b");
    checkAndUpdateZoneStatus("zone_c");
}, 2000);
