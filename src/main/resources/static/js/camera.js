let constraints = { video: { facingMode: "user" }, audio: false };
const cameraSensor = document.getElementById("camera-sensor");
const cameraView = document.getElementById("camera-view");
const cameraOutput = document.getElementById("camera-output");
const cameraTrigger = document.getElementById("camera-trigger");

const result = document.getElementById("result");

// 카메라 준비
async function cameraStart() {
    await navigator.mediaDevices.getUserMedia(constraints)
        .then(function (stream) {
            track = stream.getTracks()[0];
            cameraView.srcObject = stream;
        })
        .catch(function (error) {
            alert("카메라에 문제가 있습니다.", error);
            cameraTrigger.disabled = true;
        })
}

//촬영 버튼 클릭 리스너
cameraTrigger.addEventListener("click", function () {
    cameraTrigger.classList.add("hidden");
    cameraSensor.width = cameraView.videoWidth;
    cameraSensor.height = cameraView.videoHeight;
    cameraSensor.getContext("2d").drawImage(cameraView, 0, 0);
    cameraOutput.classList.remove("hidden");
    cameraOutput.src = cameraSensor.toDataURL("image/png");
    result.classList.remove("hidden");
    result.classList.add("taken");
});

// 페이지가 로드되면 함수 실행
window.addEventListener("load", cameraStart, false);