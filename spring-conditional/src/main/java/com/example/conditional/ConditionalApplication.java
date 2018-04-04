package com.example.conditional;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ConditionalApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConditionalApplication.class, args);
	}
}

@Service
@ConditionalOnProperty(prefix = "com.example.conditional.timeservice", name = "enabled", havingValue = "true", matchIfMissing = true)
class TimeService {
	@PostConstruct public void printTime() {
		System.out.println("Time: " + new Date());
	}
}

@Service
@Conditional(DummyCondition.class)
class NameService {
	@PostConstruct public void printName() {
		System.out.println("Luke Skywalker");
	}
}

@Configuration
@EnableScheduling
@ConditionalOnBean(HelloService.class)
class ScheduledService {
	@Autowired
	HelloService service;

	@Scheduled(fixedDelay = 1000)
	public void hello() {
		service.sayHello();
	}
}

interface HelloService {
	public void sayHello();
}

//@Service
class HelloServiceImpl implements HelloService {
	@Override public void sayHello() {
		System.out.println("helloooo");
	}
}