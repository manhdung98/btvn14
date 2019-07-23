package com.github.icovn.handler;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Aspect
@Configuration
@Component
public class Handler {
	static final Logger logger = Logger.getLogger(Handler.class);
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "123";
	public static final String WRONG_PASS = "Wrong Password!";
	public static final String NOT_ARG = "Method do not have arg!";
	public static final String ARG_VALUE = "argument's value: ";
	public static final String PAR_NAME = "parameter's name ";
	public static final String OUTPUT = "output: ";
	public static final String TIME = "time: ";

	@Before("execution(* com.github.icovn.aop..*(..))")
	public void login(JoinPoint join) throws Throwable {
		Scanner sc = new Scanner(System.in);
		String username = sc.nextLine();
		String password = sc.nextLine();
		if (!PASSWORD.equals(password) && !USERNAME.equals(username)) {
			throw new Throwable(WRONG_PASS);
		} else {
			logger.info("Login Sucsessfully!");
		}
	}

	@Around("execution(* com.github.icovn.aop..*(..))")
	public Object logBefore(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object value = joinPoint.proceed();
		long timeTaken = System.currentTimeMillis() - startTime;

		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		Object[] arr = joinPoint.getArgs();
		if (arr.length == 0) {
			logger.info(NOT_ARG);
			logger.info(ARG_VALUE + joinPoint.getArgs()[0]);
		} else {
			for (int i = 0; i < arr.length; i++) {
				logger.info(PAR_NAME + i + ": = " + codeSignature.getParameterNames()[i]);
				logger.info(ARG_VALUE + i + ": = " + joinPoint.getArgs()[i]);
			}
		}

		logger.info(OUTPUT + value);
		logger.info(TIME + timeTaken);

		return joinPoint.proceed();
	}

}
