package com.education.hh_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HhTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhTelegramBotApplication.class, args);
	}

}
