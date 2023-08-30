const regBtn = document.getElementById("reg-btn");

const nameInput = document.getElementById("name-input");
const idCardInput = document.getElementById("id-card-input");

// 안면 사진 등록 버튼 클릭 리스너
regBtn.addEventListener("click", () => {
    getData(cameraOutput.src).then(function (resolvedData) {
        fetchServer(resolvedData);
    })
});


// 촬영된 이미지 데이터 가져오는 함수
async function getData(url) {
    const response = await fetch(url);
    const data = await response.blob();
    const filename = url.split("/").pop();
    const metadata = { type: 'image/jpg' };

    return new Promise(function (resolve, reject) {
        resolve(new File([data], filename, metadata))
    });
}

// API서버에 입력 정보(이름, 신분증, 안면이미지) 전송 및 응답 처리
async function fetchServer(faceImage) {
    const idInput = idCardInput.files[0];
    let faces;
    let id;

    const faceReader = new FileReader();
    faceReader.onload = function(event) {
        const data = event.target.result;
        faces = [data]
    }
    faceReader.readAsBinaryString(faceImage);

    const idReader = new FileReader();
    idReader.onload = function(event) {
        const data = event.target.result;
        id = data;
    }
    idReader.readAsBinaryString(idInput);
    
    // let faces = [abc];
    let userNm = nameInput.value;
    // let idImg = idCardInput.files[0];

    let body = {
        faces: faces,
        userNm: userNm,
        id: idImg
    };
    console.log(JSON.stringify(body));

    await fetch("/api/registration", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "company": "gaon"
        },
        body: JSON.stringify(body)
    })
    .then(res => {
        if (res.status == 201) {
            alert("정상 등록");
            location.href = "/authentication";
        } else {
            res.text().then(text => {
                alert(`[등록 실패] ${res.statusText}`);
                location.reload();
            })
        }
    }).catch(error => alert(error));
}