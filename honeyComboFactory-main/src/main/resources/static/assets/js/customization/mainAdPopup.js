/**
 * 메인페이지 광고 팝업 js
 */

// 쿠키 가져오는 함수
function getCookie(name) {
  const value = "; " + document.cookie; // 쿠키 문자열 파싱
  const parts = value.split("; " + name + "="); // 쿠키 이름 분리
  if (parts.length === 2) return parts.pop().split(";").shift(); // 쿠키 값만 반환
}

window.addEventListener("load", function () { // 페이지 요소 로딩 후에 실행함
  if (!getCookie("advertiseCookie")) { 
		// 쿠키가 존재하지 않을 때 광고 띄우기
	  Swal.fire({
		title: '오늘의 꿀조합 추천! \n 부대찌개 컵라면', // 광고 제목
		html: '오모리김치찌개컵라면 + 볶음김치 + 햄<br>= 감칠맛 폭발! 든든한 한끼 식사', // 광고 내용
      imageUrl: 'assets/img/comboProduct/30000.png', // 이미지 경로 
      imageWidth: 300,   // 이미지 크기 조정
      imageHeight: 300,
      imageAlt: '문세윤 부대찌개 컵라면 레시피', // 이미지 설명
      showCancelButton: true, // 취소 버튼 표시
      confirmButtonText: '제품 보러 가기', // 확인 버튼
      cancelButtonText: '하루 동안 보지 않기', // 취소 버튼
    }).then((result) => {
    	// 만약 확인버튼을 눌렀다면 해당 제품 상세페이지로 이동
      if (result.isConfirmed) {
		window.location.href = 'productDetail.do?productComboNumber=30000'; 
		// 해당 제품의 상세 정보 페이지 이동
        // 하루동안 보지 않기 눌렀을 때 쿠키 저장하여 1일동안 광고 보지 않기
      } else if (result.dismiss === Swal.DismissReason.cancel) {
 		// 만료일
    	  const expires = new Date();
        expires.setDate(expires.getDate() + 1); // 현재 시각 + 1일 뒤 만료일 설정
        document.cookie = "advertiseCookie=true; expires=" + expires.toUTCString() + "; path=/";
 		// 쿠키 저장 
 		// 사이트 전체에서 쿠키 적용 가능하도록 설정
      	}
    });
  }
});
