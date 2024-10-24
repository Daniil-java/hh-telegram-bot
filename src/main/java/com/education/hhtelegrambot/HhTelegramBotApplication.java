package com.education.hhtelegrambot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class HhTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhTelegramBotApplication.class, args);
	}

}
