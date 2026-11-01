package org.example;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LogAspect {


    @Before("execution(* org.example.RestoService.getFood())")
    public void addBeforeForService(JoinPoint jp) {
        log.info("Krish see service log {}", jp.getSignature());

    }
    @AfterReturning(pointcut = "execution(* org.example.RestoService.getFood())", returning = "foodResult")
    public void addAfterForService(JoinPoint jp, Object foodResult) {
        log.info("Krish see service log {}, See KK food returned: {} ", jp.getSignature(), foodResult);

    }

    @Before("execution(* org.example.HelloController.getStatus())")
    public void addBeforeLogger(JoinPoint jp) {
        log.info("Krish its my own log {}", jp.getSignature());

    }
    // 1. Before execution target: Logs right before entry
    @Before("execution(* org.example.*Controller.*(..))")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("KK See: AOP [Before Execution] -> Intercepted Method: {} | Arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // 2. Around execution target: Measures structural system performance/latency
    @Around("execution(* org.example.*Controller.*(..))")
    public Object profileExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();

        try {
            // Forward execution down the proxy chain to the actual method
            Object outputPayload = joinPoint.proceed();

            long runDuration = System.currentTimeMillis() - startTime;
            log.info("Krishnath see:AOP [Around Success] -> Method: {} finished in {} ms", methodName, runDuration);
            return outputPayload;

        } catch (Throwable throwable) {
            log.error("KK See: AOP [Around Failure] -> Exception caught in method {}: {}", methodName, throwable.getMessage());
            throw throwable;
        }
    }

}
