package com.example.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@SpringBootApplication
public class SpringAspectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringAspectApplication.class, args);
	}
}

@Order(1)
@Component
class DocumentService implements ApplicationRunner {

	@Autowired
	SomeService service;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		service.doSomething();
		service.doSomething();
		service.doSomething();
	}
}

@Component
class SomeService {
	@LatencyLogger
	public void doSomething() throws InterruptedException {
		TimeUnit.SECONDS.sleep(new Random().nextInt(3)+1);
	}
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface LatencyLogger {
}

@Aspect
@Component
class LogInterceptor {

    @Around("@annotation(com.example.aspect.LatencyLogger)")
    public void logLattency(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        joinPoint.proceed();
        stopWatch.stop();

        StringBuffer logMessage = new StringBuffer();
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append(" execution time: ");
        logMessage.append(stopWatch.getTotalTimeMillis());
        logMessage.append(" ms");
        System.out.println(logMessage.toString());
    }
}