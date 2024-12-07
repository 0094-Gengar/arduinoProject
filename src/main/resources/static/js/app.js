// 구역별 상태와 타이머 관리 변수
const zoneTimers = {}; // { zone_a: timerId, zone_b: timerId, ... }

// 타이머 시작
function startZoneTimer(zoneId, startTime) {
    // 기존 타이머가 있다면 제거
    if (zoneTimers[zoneId]) {
        clearInterval(zoneTimers[zoneId]);
    }

    // 실시간 업데이트
    zoneTimers[zoneId] = setInterval(() => {
        const liveDuration = document.getElementById(`duration-${zoneId}`);
        const elapsedTime = Date.now() - startTime;
        liveDuration.innerText = formatDuration(elapsedTime);
    }, 1000);
}

// 타이머 중지
function stopZoneTimer(zoneId) {
    if (zoneTimers[zoneId]) {
        clearInterval(zoneTimers[zoneId]);
        zoneTimers[zoneId] = null;
    }
}

// 구역 상태 업데이트
function toggleZone(zoneId) {
    fetch(`/zones/${zoneId}/toggle`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            updateZoneUI(zoneId, data.status);
            if (data.status === 'zone_on' && data.startTime) {
                updatePatrolUI(zoneId, parseInt(data.startTime), formatDuration(Date.now() - parseInt(data.startTime)));
                startZoneTimer(zoneId, parseInt(data.startTime));
            } else if (data.status === 'zone_off' && data.duration) {
                updatePatrolUI(zoneId, null, data.duration); // 종료 시 duration 사용
                stopZoneTimer(zoneId);
            }

        })
        .catch(error => console.error('Error:', error));
}

// 구역 UI 업데이트
function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === 'zone_on' ? 'green' : 'gray';
}

// 순찰 정보 업데이트
function updatePatrolUI(zoneId, time, duration) {
    const liveTime = document.getElementById(`time-${zoneId}`);
    const liveDuration = document.getElementById(`duration-${zoneId}`);

    liveTime.innerText = time ? new Date(time).toLocaleString() : "-";
    liveDuration.innerText = duration || "-";
}

// ------- 변경 전
//
