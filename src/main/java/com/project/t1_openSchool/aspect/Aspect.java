package com.project.t1_openSchool.aspect;

import org.aspectj.lang.JoinPoint;
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
                Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList());
    }
}
