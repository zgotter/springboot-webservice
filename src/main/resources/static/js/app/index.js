// index 라는 변수의 속성으로 function을 추가한 이유
//  - 각각의 js 파일 안에 init, save 함수를 각각 정의한다고 가정하자.
//  - 브라우저의 스코프(scope)는 공용 공간으로 쓰이기 때문에 나중에 로딩된 js의 init, save가 먼저 로딩된 js의 function을 덮어쓰게 된다.
//  - 여러 사람이 참여하는 프로젝트에서는 중복된 함수 이름은 자주 발생할 수 있다.
//  - 모든 함수 이름을 확인하면서 만들 수는 없기 때문에 이러한 문제를 피하려고 index.js 만의 "유효범위(scope)"를 만들어 사용한다.
//  - 사용 방법은 "var index" 이란 객체를 만들어 해당 객체에서 필요한 모든 함수를 선언한다.
//  - 이렇게 하면 index 객체 안에서만 함수가 유효하기 때문에 다른 JS와 겹칠 위험이 사라진다.

var main = {
    init: function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save: function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
           type: 'POST',
           url: '/api/v1/posts',
           dataType: 'json',
           contentType: 'application/json; charset=utf-8',
           data: JSON.stringify(data)
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();