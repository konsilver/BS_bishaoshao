package com.bishaoshao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.bishaoshao.mapper")
@ComponentScan(basePackages = "com.bishaoshao")
public class BishaoshaoApplication {
	public static void main(String[] args) {
		SpringApplication.run(BishaoshaoApplication.class);
	}

}