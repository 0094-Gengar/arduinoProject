function toggleZone(zoneId) {
    const currentButton = document.getElementById(`${zoneId}_btn`);
    const currentStatus = currentButton.style.backgroundColor === 'rgb(76, 175, 80)' ? 'zone_on' : 'zone_off';
    const newStatus = currentStatus === 'zone_on' ? 'zone_off' : 'zone_on';

    updateZoneUI(zoneId, newStatus); // 즉시 UI 반영

    function toggleZone(zoneId) {
        fetch(`/zones/${zoneId}/toggle`, {method: 'POST'})
            .then(response => response.json())
            .then(data => updateZoneUI(zoneId, data.status)) // data.status가 "zone_on" 또는 "zone_off"
            .catch(error => console.error('Error:', error));
    }
}

function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);

    // status가 "zone_on"이면 녹색, "zone_off"이면 회색으로 변경
    button.style.backgroundColor = status === "zone_on" ? '#4CAF50' : '#ccc';
}

