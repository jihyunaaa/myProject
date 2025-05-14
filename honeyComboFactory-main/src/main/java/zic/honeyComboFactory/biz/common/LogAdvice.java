package zic.honeyComboFactory.biz.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StopWatch;

@Configuration // Spring 설정 클래스를 정의할 때 사용하는 어노테이션
@EnableAspectJAutoProxy // AOP를 자동 활성화하는 어노테이션
@Aspect // 포인트컷과 공통로직을 결합하는 어노테이션
public class LogAdvice {
	// 메서드 시그니처 출력 로그
	@Before("PointcutCommon.logAdvice()")
	public void printMethodSignatureName(JoinPoint jp) {
		System.out.println("메서드 시그니처 출력 로그 실행");

		// 메서드 시그니처 추출
		String methodSignatureName = jp.getSignature().getName();
		System.out.println("수행할 메서드 시그니처 : [" + methodSignatureName + "]");
	}

	// 메서드 인자 출력 로그
	@After("PointcutCommon.logAdvice()")
	public void printMethodArguments(JoinPoint jp) {
		System.out.println("메서드 인자 출력 로그 실행");

		// 메서드 인자 추출
		Object[] args = jp.getArgs();
		System.out.println("수행할 메서드 인자 : [");
		for (Object arg : args) { // 복사체 사용
			System.out.println(arg);
		}
		System.out.println("]");
	}

	// 종단 관심 수행시간 출력 로그
	@Around("PointcutCommon.logAdvice()")
	public Object printMethodArguments(ProceedingJoinPoint pjp) {
		System.out.println("종단 관심 수행시간 출력 로그 실행");

		// 걸린 시간을 체크해주는 스프링에서 지원하는 객체
		StopWatch stopWatch = new StopWatch();
		stopWatch.start(); // 시간 체크 시작
		
		Object result = null;
		try {
			result = pjp.proceed(); // 핵심 관심 수행
		} catch (Throwable e) { // 예외 처리
			e.printStackTrace();
		}
		
		stopWatch.stop(); // 시간 체크 종료

		// 걸린시간 ms로 저장
		Long time = stopWatch.getTotalTimeMillis();
		System.out.println("핵심 로직 수행에 걸린 시간 : [" + time + "]");
		return result;
	}

}
