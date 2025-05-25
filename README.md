# honeyComboFactory 프로젝트 🍯
‘꿀조합팩토리’는 편의점에서 판매되는 식품을 조합해, 사용자 맞춤형 세트를 구성하고 판매하는 웹 기반 서비스입니다. -중간 프로젝트-

## 기술 스택 ⚙️
<!-- 기술 스택 배지 -->
<p align="center">
  <!-- 첫 번째 줄: 프로그래밍 언어 -->
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />&nbsp;
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" alt="JavaScript" />&nbsp;
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5" />&nbsp;
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3" />
</p>

<p align="center">
  <!-- 두 번째 줄: 데이터베이스 -->
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
</p>

## 프로젝트 클론 📥
git clone https://github.com/JoungGyuMin/honeyComboFactory-mid.git

## 프로젝트 디렉토리 구조 🗂️
```
honeyComboFactory/
├── build/
│   └── classes/
│       └── controller/
│           ├── board/
│           │   └── combo/async/               # 게시판 관련 비동기 Servlet
│           ├── common/                        # FrontController 및 Action 처리
│           ├── member/
│           │   ├── action/                    # 회원 기능 Action 클래스
│           │   └── async/                     # 회원 관련 비동기 Servlet
│           ├── move/action/                   # JSP 페이지 이동 처리 Action
│           └── product/
│               ├── combo/async/               # 콤보 상품 비동기 처리
│               ├── common/async/              # 상품 주문, 리뷰 CRUD, 검색 비동기 처리
│               └── single/async/              # 단일 상품 비동기 처리
│       └── listnerCrawling/
│           └── ListnerCrawling.class          # 크롤링 리스너
│       └── model/
│           ├── common/                        # JDBC 유틸
│           ├── crawling/                      # 크롤링 클래스
│           ├── dao/                           # DAO 클래스 모음
│           └── dto/                           # DTO 클래스 모음

├── src/
│   └── main/
│       └── webapp/
│           ├── client/
│           │   ├── assets/
│           │   │   ├── css/
│           │   │   ├── fonts/
│           │   │   ├── img/
│           │   │   ├── js/
│           │   │   ├── scss/
│           │   │   └── webfonts/
│           │   ├── Doc/
│           │   │   ├── main.jsp               # 메인 페이지
│           │   │   ├── login.jsp              # 로그인
│           │   │   ├── join.jsp               # 회원가입
│           │   │   ├── myInfo.jsp             # 내 정보
│           │   │   ├── comboProduct.jsp       # 콤보 상품 목록
│           │   │   ├── productDetail.jsp      # 상품 상세
│           │   │   ├── boardDetail.jsp        # 게시글 상세
│           │   │   └── 기타 JSP 다수 포함...
│           ├── META-INF/
│           └── WEB-INF/
│               ├── lib/                       # JSTL, Servlet API, Mail 등 라이브러리
│               └── web.xml                    # (있는 경우) 웹 설정 파일

└── 기타 파일
    ├── .gitignore
    ├── README.md
```

## 주요 패키지 설명 📚

### DB (dto) 💼
- **boardCombo**: 게시판 관련 클래스 📝
- **boardComboLiked**: 게시글 좋아요 관련 클래스 ❤️
- **member**: 회원 관련 클래스 👥
- **productCombo**: 꿀조합 상품 관련 클래스 🍯🛍️
- **productComboComponent**: 꿀조합 상품 구성품 관련 클래스 🧩
- **productSingle**: 개별 상품 관련 클래스 📦
- **purchase**: 구매 관련 클래스 🛒
- **purchaseDetail**: 구매 상세정보 관련 클래스 📄
- **review**: 리뷰 관련 클래스 💬

### 크롤링 (listnerCrawling) 🕷️
- **ListnerCrawling**: 서버 실행 시 크롤링 관련 클래스 🕸️

### 모델 공통 (model.common) 🛠️
- **JDBCUtil**: MySQL 연결 유틸리티 🔌

### 모델 크롤링 (model.crawling) 🛠️
- **StoreCU**: Cu상품 크롤링 기능 관련 클래스 🏪
- **StoreGS25**: Gs25상품 크롤링 기능 관련 클래스 🏪

### 컨트롤러 공통 (controller.common) 🎛️
- **Action**: 컨트롤러 액션이 구현하는 공통 인터페이스 🔗
- **ActionFactory**: 요청 URL에 맞는 컨트롤러 액션 객체를 반환하는 팩토리 클래스 ⚙️
- **ActionForward**: 요청 처리 결과 이동 경로와 방식을 저장해 컨트롤러의 페이지 이동을 관리하는 DTO 🚦
- **FrontControllerDid**: *.did 패턴의 모든 요청을 받아 처리하는 Front Controller 서블릿 🎯
- **FrontControllerDo**: *.do 패턴의 모든 요청을 받아 처리하는 Front Controller 서블릿 🎯

### 컨트롤러 동기 (*.action) 🔄
- **move**: 페이지 이동 관련 클래스 🚀
- **member**: 회원 기능 관련 클래스 👤

### 컨트롤러 비동기 (*.async) ⚡
- **member**: 회원 기능 관련 클래스 👥
- **board.combo**: 게시판 기능 관련 클래스 📝
- **product.common**: 리뷰 기능 관련 클래스 💬
- **product.combo**: 꿀조합 상품 기능 관련 클래스 🍯🛍️
- **product.single**: 개별 상품 기능 관련 클래스 📦

## 프로젝트 주요 기능 ✨

- 브랜드 혼합 세트 상품 제공 🧃
- 상품 정보 통합 검색 및 필터링 🔍
- 리뷰 및 별점 시스템 🌟
- 편의점 위치 안내 📍
