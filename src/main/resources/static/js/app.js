function toggleZone(zoneId) {
    fetch(`/zones/${zoneId}/toggle`, {method: 'POST'})
        .then(response => response.json())
        .then(data => {
            updateZoneUI(zoneId, data.status);
            if (data.status === 'zone_on' && data.startTime) {
                updatePatrolInfo(zoneId, parseInt(data.startTime));
            } else if (data.status === 'zone_off' && data.duration) {
                updatePatrolInfoWithDuration(zoneId, data.duration);  // 서버에서 전달된 duration을 그대로 사용
            }
        })
        .catch(error => console.error('Error:', error));
}

function updatePatrolInfoWithDuration(zoneId, duration) {
    const liveZone = document.getElementById('live-zone');
    const liveTime = document.getElementById('live-time');
    const liveDuration = document.getElementById('live-duration');

    liveZone.innerText = `[${zoneId.toUpperCase()}]`;
    // liveTime.innerText = "(순찰 종료)";
    liveDuration.innerText = duration;  // 서버에서 받은 duration 문자열을 그대로 사용
}

// zone_off 상태일 때 추가 요청을 보내서 실제 경과 시간을 가져오는 방법
function fetchDurationFromServer(zoneId) {
    fetch(`/zones/${zoneId}/duration`)  // 경과 시간을 가져오는 추가 요청
        .then(response => response.json())
        .then(data => {
            const liveDuration = document.getElementById('live-duration');
            liveDuration.innerText = data.duration;  // 서버에서 다시 가져온 duration을 표시
        })
        .catch(error => console.error('Error fetching duration:', error));
}
