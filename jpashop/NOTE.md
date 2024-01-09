JDK 11
Spring boot starter를 통해서 프로젝트 생성


build.gradle에서 스프링 부트에 사용하는 패키지와 의존관계를 설정한다.
External Library에 라이브러리들과 그에 의존하는 라이브러리가 담겨있다.

Lombok은 중복 코드를 줄여주는 플러그인
Getter, Setter같은 것을 속성에 일일이 달아주어도 되지 않아도 됨 (Hello 클래스 참고)

./gradlew dependencies : 의존관계를 보여줌
spring-boot-starter-web
1. 내장 톰켓을 가지고 있음
2. spring-webmvc 모듈에 의존하고 있음
3. spring-core

thymeleaf : 템플릿 엔진으로 사용
data-jpa 
1. aop와 관련된것.
2. jdbc가 있음
3. HikariCP : 커넥션 풀로 사용
4. Hibernate
5. Spring data JPA 
6. 로깅은 logback을 기본으로 사용하고 있음

test
1. junit, spring-test, mockito, assertj

h2datase : 가끔 버전이 안맞아서 문제가 있을 수 있음. 다운!=사용일 때 혹시 안되면 맞춰주기
커넥션풀은 히카리씨피
웹 뷰 템플릿은 타임리프
로깅은 SLF4J에 로그백 구현체를 꽂아서 사용
테스트


Timeleaf
- JSP를 대체해서 요즘 스프링이 미는 템플릿 엔진
- 2.x대에서는 <br>같은거를 안닫아주면 에러가 났었음


dependency에 devtools를 추가하면 캐싱 등을 사용하여 개발 환경에서 용이해진다

서버 재시작을 안하고 변경 후 빌드->리컴파일 하면 서버 재시작 안하고 변경사항을 확인할수있다.


H2 데이터베이스
개발이나 테스트 용도로 가볍고 편리한 DB, 웹 콘솔 UI를 제공함
Java로 실행됨, /h2/bin에서 터미널명령 ./h2.sh로 실행
http://localhost:8082/login.jsp?jsessionid=54e3c4f4afbf20d741d349677f3f76ca
jdbc:h2:~/test 해당 경로에 mv.db가 생성된 것을 확인하면 JDBC URL에 TCP 모드로 접속할 수 있다.
jdbc:h2:tcp://localhost/~/test를 JDBC URL에 입력하기

Member Entity를 만들고, Repository를 통해 연산한다.
테스트 코드를 작성해서 테스트 가능
빌드 후 배포파일 실행해서 최종확인


테이블을 설계할 때 N:M 관계는 실무에서 1:N, M:1 관계로 풀어야 함
1:N 관계는 단방향을 권장
테이블과 컬럼명에 대한 관례는 대문자 + _ 혹은 소문자 + _
연관관계의 외래키가 있는 테이블을 주인으로 정하는 것이 좋다.
일대다 관계의 경우에는 다쪽에 외래키가 있으므로 다를 주인으로 정하면 된다.


엔티티 설계시 주의점
- setter 사용 지양
- 모든 연관관계는 지연로딩으로 설정
  - 즉시 로딩(Eager) : Order를 조회하는 시점에 다른 연관 테이블을 로딩 -> 예측이 안되고 성능 문제 발생. Order를 가져오면 그만큼 Member를 가져옴
  - 지연 로딩 : 모든 연관은 fetch=LAZY로 설정
  - ManyToOne은 기본이 Eager, OneToMany는 기본이 Lazy
- 테이블, 컬럼명 생성 전략
  - 논리명 : 테이블 이름을 적지 않았을 때의 네이밍 룰
  - 물리명 : 이름이 적혀있을 때를 포함한 모든 네이밍 룰

테스트 시 test/resources/application.yml에서 디비를 메모리 모드로 변경할 수 있음

API를 만들 때는 절대 엔티티를 그대로 반환하지 말자
- 엔티티가 변할 때 API의 스펙이 변화하기 때문!!

준영속 엔티티
변경 감지로 업데이트 vs Merge(준영속 엔티티를 영속성 엔티티로 변경)
변경감지는 원하는 속성만 선택하여 변경할 수 있지만 Merge는 모든 속성이 변경된다.
-> 만약 어떤 속성이 null이라면 Merge시에는 null로 업데이트가 된다. 위험함 Merge를 안쓰는 것이 중요

컨트롤러에서 어설프게 엔티티를 생성해서는 안h
