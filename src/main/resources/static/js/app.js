const zoneTimers = {};

// 페이지 로드 시 LocalStorage에서 데이터 불러오기
document.addEventListener("DOMContentLoaded", () => {
    const storedZones = JSON.parse(localStorage.getItem("zones")) || {};
    for (const [zoneId, data] of Object.entries(storedZones)) {
        if (data.status === "zone_on" && data.startTime) {
            updateZoneUI(zoneId, "zone_on");
            updateActiveZone(zoneId, "zone_on");
            updatePatrolUI(zoneId, data.startTime, formatDuration(Date.now() - data.startTime));
            startZoneTimer(zoneId, data.startTime);
        } else {
            updateZoneUI(zoneId, "zone_off");
            updatePatrolUI(zoneId, null, data.duration);
        }
    }
});

// 타이머 시작
function startZoneTimer(zoneId, startTime) {
    if (zoneTimers[zoneId]) clearInterval(zoneTimers[zoneId]);
    zoneTimers[zoneId] = setInterval(() => {
        const elapsedTime = Date.now() - startTime;
        document.getElementById(`duration-${zoneId}`).innerText = formatDuration(elapsedTime);
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
    fetch(`/zones/${zoneId}/toggle`, { method: "POST" })
        .then((response) => response.json())
        .then((data) => {
            updateZoneUI(zoneId, data.status);
            updateActiveZone(zoneId, data.status);

            const storedZones = JSON.parse(localStorage.getItem("zones")) || {};
            if (data.status === "zone_on" && data.startTime) {
                const startTime = parseInt(data.startTime);
                updatePatrolUI(zoneId, startTime, formatDuration(Date.now() - startTime));
                startZoneTimer(zoneId, startTime);

                storedZones[zoneId] = {
                    status: "zone_on",
                    startTime: startTime,
                    duration: null,
                };
            } else if (data.status === "zone_off") {
                stopZoneTimer(zoneId);
                if (data.duration) {
                    updatePatrolUI(zoneId, null, data.duration);
                    storedZones[zoneId] = {
                        status: "zone_off",
                        startTime: null,
                        duration: data.duration,
                    };
                }
            }

            // LocalStorage에 저장
            localStorage.setItem("zones", JSON.stringify(storedZones));
        })
        .catch((error) => console.error("Error:", error));
}

// 구역 UI 업데이트
function updateZoneUI(zoneId, status) {
    const button = document.getElementById(`${zoneId}_btn`);
    button.style.backgroundColor = status === "zone_on" ? "green" : "gray";
}

// 활성화된 구역 표시 업데이트
function updateActiveZone(zoneId, status) {
    const activeZone = document.getElementById("active-zone");
    const zoneNameMap = {
        zone_a: "구역 A",
        zone_b: "구역 B",
        zone_c: "구역 C",
    };
    const zoneName = zoneNameMap[zoneId] || "알 수 없는 구역";
    activeZone.innerText = status === "zone_on" ? `${zoneName} 긴급 순찰 중` : "-";
}

// 순찰 정보 업데이트
function updatePatrolUI(zoneId, time, duration) {
    const liveTime = document.getElementById(`time-${zoneId}`);
    const liveDuration = document.getElementById(`duration-${zoneId}`);

    liveTime.innerText = time ? new Date(time).toLocaleString() : "-";
    liveDuration.innerText = duration || "-";
}

// 시간 형식 변환
function formatDuration(ms) {
    const seconds = Math.floor(ms / 1000) % 60;
    const minutes = Math.floor(ms / 60000) % 60;
    const hours = Math.floor(ms / 3600000);
    return `${hours}시간 ${minutes}분 ${seconds}초`;
}
