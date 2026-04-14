package it.supermercato.magazzino;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // pointcut for all methods in service package
    @Pointcut("execution(* it.supermercato.magazzino.service..*(..))")
    public void serviceLayer() {}

    // advice to log before method execution
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("==> Execute Service: {}.{}()", 
            joinPoint.getSignature().getDeclaringTypeName(), 
            joinPoint.getSignature().getName());
    }

    // advice to log exceptions thrown by service methods
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logError(JoinPoint joinPoint, Throwable exception) {
        log.error("!!! ERROR in {}.{}() - Message: {}", 
            joinPoint.getSignature().getDeclaringTypeName(), 
            joinPoint.getSignature().getName(), 
            exception.getMessage());
    }
}