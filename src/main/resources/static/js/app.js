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

setInterval(updateTime, 1000);
updateTime();

function toggleZone(zoneId) {
    fetch(`/zones/${zoneId}/toggle`, {method: 'POST'})
        .then(response => response.json())
        .then(data => {
            updateZoneUI(zoneId, data.status);
            if (data.status === 'zone_on' && data.startTime) {
                updatePatrolInfo(zoneId, parseInt(data.startTime));
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === 'zone_on' ? 'green' : 'gray';
}

function updatePatrolInfo(zoneId, startTime) {
    const liveZone = document.getElementById('live-zone');
    const liveTime = document.getElementById('live-time');
    const liveDuration = document.getElementById('live-duration');

    liveZone.innerText = `[${zoneId.toUpperCase()}]`;

    const startDate = new Date(startTime);  // 서버에서 받은 startTime을 Date 객체로 변환
    liveTime.innerText = startDate.toLocaleString('ko-KR', {
        month: 'long',
        day: 'numeric',
        weekday: 'short',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
    });

    // 실시간 경과 시간 업데이트
    clearInterval(window.patrolInterval); // 중복 방지용 인터벌 초기화
    window.patrolInterval = setInterval(() => {
        const now = new Date();
        const elapsed = now - startDate;  // 경과 시간 계산
        const minutes = Math.floor(elapsed / 60000); // 분 단위로 계산
        const seconds = Math.floor((elapsed % 60000) / 1000); // 초 단위로 계산
        liveDuration.innerText = `${minutes}분 ${seconds}초`;
    }, 1000);
}


function formatDuration(milliseconds) {
    const hours = Math.floor(milliseconds / (1000 * 60 * 60));
    const minutes = Math.floor((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((milliseconds % (1000 * 60)) / 1000);
    return `${hours}h ${minutes}m ${seconds}s`;
}
