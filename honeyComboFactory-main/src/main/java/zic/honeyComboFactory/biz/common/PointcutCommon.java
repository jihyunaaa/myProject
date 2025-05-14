package zic.honeyComboFactory.biz.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutCommon { // 포인트컷 객체
	// 모든 핵심 비즈니스 로직
	@Pointcut("execution(* zic.honeyComboFactory.biz..*Impl.*(..))")
	public void logAdvice() {}
}
