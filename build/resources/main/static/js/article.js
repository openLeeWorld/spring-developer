const deleteButton = document.getElementById('delete-btn');
const modifyButton = document.getElementById('modify-btn');
const createButton = document.getElementById('create-btn');
const logoutButton = document.getElementById('logout-btn');

if (deleteButton) {
    //삭제 기능
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert("삭제 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("삭제 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}

if (modifyButton) {
    //수정 기능
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });

        function success() {
            alert("수정 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}

if (createButton) {
    //생성 기능
    createButton.addEventListener('click', (event) => {
        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });
        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }
        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

if (logoutButton) {
    //로그아웃 하면서 기능
    logoutButton.addEventListener('click', (event) => {

        function success() {
            alert("로그아웃 완료되었습니다.");
            location.replace("/login");
        }
        function fail() {
            alert("로그아웃 실패했습니다.");
        }

        httpRequest_logout(success, fail);
    });
}
    // 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split("=");

        if(key === dic[0]){
            result = dic[1];
            return true;
        }
    });

    return result;
}

function deleteCookie(key) {
    // 현재 날짜를 이전으로 설정하여 쿠키를 만료시킵니다.
    document.cookie = key + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

// http 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then((response) => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if (response.status === 401 && refresh_token) {
            fetch("/api/token", {
                method: "POST",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    refreshToken: getCookie("refresh_token"),
                }),
            })
            .then((res) => {
                if (res.ok) {
                    return res.json();
                }
            })
            .then((result) => { // 재발급이 성공하면 로컬 스토리지 값을 새로운 액세스 토큰으로 교체
                localStorage.setItem("access_token", result.accessToken);
                httpRequest(method, url, body, success, fail);
            })
            .catch((error) => fail());
        } else {
            return fail();
        }
    });
}

// 로그아웃하면서 토큰을 없애는 함수
function httpRequest_logout(success, fail) {
    if(localStorage.getItem("access_token")) {
       localStorage.removeItem("access_token");
    }
    const refresh_token = getCookie("refresh_token");
    if (refresh_token) {
        fetch("/api/token", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                refreshToken: refresh_token,
            }),
        })
        .then((res) => {
            if (res.status === 200) {
                deleteCookie("refresh_token");
                return success();
            }
            else {
                deleteCookie("refresh_token");
                location.replace("/login");
                return;
            }
        })
        .catch((error) => fail());
    } else {
        return fail();
    }
}

