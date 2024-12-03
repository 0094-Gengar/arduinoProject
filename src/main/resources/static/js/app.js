function toggleZone(zoneId) {
    const currentButton = document.getElementById(`${zoneId}_btn`);
    const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';
    const newStatus = currentStatus === 'zone_on' ? 'zone_off' : 'zone_on';

    updateZoneUI(zoneId, newStatus); // 즉시 UI 반영

    fetch(`/zones/${zoneId}/toggle`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            const status = data.status === 'zone_on' ? 'zone_on' : 'zone_off';
            updateZoneUI(zoneId, status); // 서버 응답에 따른 최종 UI 업데이트
        })
        .catch(error => {
            console.error('Error:', error);
            updateZoneUI(zoneId, currentStatus); // 오류 시 원래 상태로 복구
        });
}

function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === 'zone_on' ? '#4CAF50' : '#ccc';
}
