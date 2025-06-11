```
honeyComboFactory/
├── pom.xml                          # Maven 빌드 설정
├── README.md
├── .gitignore

├── src/
│   └── main/
│       └── java/
│           └── zic.honeyComboFactory/
│               ├── api.auth/                   # 로그인 상태 확인 및 세션 정보 반환
│               ├── biz/                        # 비즈니스 로직 계층 (VO/DAO/Service)
│               │   ├── boardCombo/             # 꿀조합 게시판
│               │   ├── boardComboLiked/        # 게시판 좋아요
│               │   ├── member/                 # 회원 DAO/Service
│               │   ├── productCombo/           # 콤보 상품
│               │   ├── productComboComponent/  # 콤보 구성 요소
│               │   ├── productSingle/          # 단일 상품
│               │   ├── purchase/               # 결제 및 구매 처리
│               │   └── review/                 # 리뷰 기능
│               ├── common/
│               │   ├── config/                 # 스프링 설정 클래스
│               │   ├── http/                   # HTTP GET/POST 유틸
│               │   ├── service/                # 공통 서비스 로직
│               │   ├── transaction/            # 트랜잭션 AOP 처리
│               │   └── util/                   # 암호화, QR, JDBC 유틸
│               ├── external.sms/               # 문자 인증 서비스 연동
│               ├── view/                       # 사용자 요청 처리 (Controller 계층)
│               │   ├── boardCombo/             # 게시판 컨트롤러
│               │   ├── boardComboLiked/        # 좋아요 컨트롤러
│               │   ├── member/                 # 회원 및 소셜 로그인
│               │   ├── page/                   # 메인/마이페이지/관리자 이동 처리
│               │   ├── product/                # 상품 컨트롤러
│               │   ├── purchase/               # 구매 요청 및 장바구니 처리
│               │   └── review/                 # 리뷰 컨트롤러
│               ├── HoneyComboFactoryApplication.java   # 메인 실행 클래스
│               └── ServletInitializer.java             # WAR 배포용 서블릿 초기화

├── resources/
│   ├── mappings/                     # MyBatis SQL 매핑 XML
│   ├── static/assets/                # 정적 리소스 (js, css, img)
│   ├── templates/
│   ├── application.properties
│   ├── application.yml
│   └── sql-map-config.xml

├── webapp/
│   ├── admin/                        # 관리자 JSP 화면
│   └── client/                       # 사용자 JSP 화면

└── src/test/java/                   # 테스트 코드
```

│   ├── admin/                        # 관리자 JSP 화면
│   └── client/                       # 사용자 JSP 화면

└── src/test/java/                   # 테스트 코드
