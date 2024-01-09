package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);

		//최종 잘되는지 확인
		// 빌드 :: ./gradlew clean build
		// 실행 :: /build/libs로 이동 -> java -jar jpashop-0.0.1-SNAPSHOT.jar
		//이게 배포파일이 된당


	}

	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		Hibernate5JakartaModule hibernate5Module = new Hibernate5JakartaModule();
		//강제 지연 로딩 설정
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

		return hibernate5Module;
	}

}


