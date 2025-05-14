/**
 *  스위트 알럿 출력용 js
 */

// 스위트 알럿창 출력 기능
const printSweetAlert = (icon, title, text, pageLink) => {
	console.log("스위트알럿 출력할 아이콘 : [" + icon + "]");
	console.log("스위트알럿 출력할 제목 글 : [" + title + "]");
	console.log("스위트알럿 출력할 내용 글 : [" + text + "]");
	console.log("스위트알럿 후 이동할 링크 : [" + pageLink + "]");

	Swal.fire({
		icon: icon,
		title: title,
		text: text
	}).then(() => {
		if (pageLink) { // 페이지 이동이 필요하다면
			// 페이지 이동
			location.href = pageLink;
		}
	});
}