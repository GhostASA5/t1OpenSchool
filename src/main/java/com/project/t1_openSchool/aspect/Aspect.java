package com.project.t1_openSchool.aspect;

import com.project.t1_openSchool.model.Task;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    private static final Logger logger = LoggerFactory.getLogger(Aspect.class.getName());

    @Before("@annotation(LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before invoked method: from class {}: method {} with args: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList()
        );
    }

    @AfterThrowing(value = "@annotation(LogThrowing)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.info("Throw exception from class {}: method {} with message: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage()
        );
    }

    @AfterReturning(value = "@annotation(LogResult)", returning = "taskResult")
    public void logAfterReturningTask(JoinPoint joinPoint, Task taskResult) {
        logger.info("Call from class {} method {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );
        logger.info("Method result: {}", taskResult);
    }

    @Around("@annotation(LogSpendTime)")
    public Object around(ProceedingJoinPoint joinPoint) {
        logger.info("Calling from class {} method {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );

        Object[] args = joinPoint.getArgs();
        logger.info("Method args: {}", Arrays.toString(args));

        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {}", throwable.getMessage());
            throw new RuntimeException("Exception in method: " + throwable.getMessage(), throwable);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Method result: {}", result);
        logger.info("Method execution time: {} ms", endTime - startTime);

        return result;
    }
}
