package com.education.hh_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class HhTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhTelegramBotApplication.class, args);
	}

}
