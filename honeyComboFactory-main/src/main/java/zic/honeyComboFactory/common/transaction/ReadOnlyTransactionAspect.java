package zic.honeyComboFactory.common.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Aspect // AOP 클래스임을 명시
public class ReadOnlyTransactionAspect {

    private final TransactionTemplate transactionTemplate;

    @Autowired
    public ReadOnlyTransactionAspect(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate; // 직접 주입
        // readOnly 전용 설정
        this.transactionTemplate.setReadOnly(true);
    }

    // "get", "select"로 시작하는 서비스 메서드만 잡기
    @Pointcut("execution(* zic.honeyComboFactory.biz..*Impl.get*(..)) || "
            + "execution(* zic.honeyComboFactory.biz..*Impl.select*(..))")
    public void readOnlyMethod() {
    }

    // 읽기 전용 트랜잭션으로 실행
    @Around("readOnlyMethod()")
    public Object applyReadOnlyTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        return transactionTemplate.execute(status -> {
            try {
                return joinPoint.proceed(); // 원래 메서드 실행
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}