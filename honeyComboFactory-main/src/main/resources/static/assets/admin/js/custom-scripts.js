/*------------------------------------------------------
    Author : www.webthemez.com
    License: Commons Attribution 3.0
    http://creativecommons.org/licenses/by/3.0/
---------------------------------------------------------  */

(function ($) {
    "use strict";
    var mainApp = {

        initFunction: function () {
            /* MENU 활성화 */
            $('#main-menu').metisMenu();

            $(window).bind("load resize", function () {
                if ($(this).width() < 768) {
                    $('div.sidebar-collapse').addClass('collapse')
                } else {
                    $('div.sidebar-collapse').removeClass('collapse')
                }
            });

            // ===== ❌ MORRIS 차트 관련 모두 비활성화 ===== //
            /*
            if (document.getElementById("morris-bar-chart")) {
                Morris.Bar({...});
            }

            if (document.getElementById("morris-donut-chart")) {
                Morris.Donut({...});
            }

            if (document.getElementById("morris-area-chart")) {
                Morris.Area({...});
            }

            if (document.getElementById("morris-line-chart")) {
                Morris.Line({...});
            }
            */

            // ===== ❌ CSS 차트도 안 쓸 거면 제거 ===== //
            /*
            $('.bar-chart').cssCharts({ type: "bar" });
            $('.donut-chart').cssCharts({ type: "donut" }).trigger('show-donut-chart');
            $('.line-chart').cssCharts({ type: "line" });
            $('.pie-thychart').cssCharts({ type: "pie" });
            */
        },

        initialization: function () {
            mainApp.initFunction();
        }

    }

    // 초기화 실행
    $(document).ready(function () {
        $(".dropdown-button").dropdown();

        $("#sideNav").click(function () {
            if ($(this).hasClass('closed')) {
                $('.navbar-side').animate({ left: '0px' });
                $(this).removeClass('closed');
                $('#page-wrapper').animate({ 'margin-left': '260px' });
            } else {
                $(this).addClass('closed');
                $('.navbar-side').animate({ left: '-260px' });
                $('#page-wrapper').animate({ 'margin-left': '0px' });
            }
        });

        mainApp.initFunction();
    });

    $(".dropdown-button").dropdown();

}(jQuery));

// 관리자 회원 수정/삭제 버튼 
// 수정 버튼
		function editMember(memberNumber) {
			if(confirm('선택한 회원 정보를 수정하시겠습니까?')) {
				// AJAX를 사용하여 수정 페이지로 이동하거나 모달을 띄움
				window.location.href = '/admin/editMember?memberNumber=' + memberNumber;
			}
		}
		// 비동기 처리
		function deleteMember(memberNumber) {
			if(confirm('정말로 이 회원을 삭제하시겠습니까?')) {
				$.ajax({
					url: '/admin/deleteMember',
					type: 'POST',
					data: { memberNumber: memberNumber },
					success: function(response) {
						alert('회원이 성공적으로 삭제되었습니다.');
						location.reload(); // 페이지 새로고침
					},
					error: function(xhr, status, error) {
						alert('회원 삭제 중 오류가 발생했습니다.');
					}
				});
			}
		}
		
		
	
