package com.education.hhtelegrambot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
//		SpringApplication.run(HhTelegramBotApplication.class, args);

		String url = "https://client.work-zilla.com/api/order/v4/list/open?hideInsolvoOrders=false";

		Map<String, String> cookies = new HashMap<>();
		cookies.put(".AspNetCore.Session", "CfDJ8JKrZDF426hNre42bGoboFZWZu43S8aoRjBQHTV9v5BHWkzxz%2B9%2FArlRxy9Ry1cpl42Lmt6BbZst67O1%2BBQgEPZAP%2F2qaDc1wekuqvTWm4EZKYmoZnWaUxTslvaFZWIbI%2BfSmDGZF5t8Cb%2FzeerQtKVrA8wXV1ZyOfzv0NRaOqdA");
		cookies.put("__stripe_mid", "4674c04e-4aa5-455c-b362-e67162be1cc7aeb2f7");
		cookies.put("__ddg1_", "p2TqlBvjWGL6q8mermdA");
		cookies.put("BrowserId", "d243597c-3fbd-4cb6-8735-97697d5d4c20");
		cookies.put("tempDataId", "gkmzuql54v");
		cookies.put("__stripe_sid", "3f186d98-3e50-4d33-ae90-5526ee66b0f14436ad");
		cookies.put("lang", "ru");
		cookies.put("Bearer", "CfDJ8FQTi_BdrXtPgWha8MoxrYjOhvKofp3iRTqTuymUERMxdPUOYQFGtoG8Cfd54KkwEQMi09UV2OMwCX-uW3iiEl-gzUu9C1mYL2SyfJ4ClaChlpFAQwLiGfTlHyPyIt_7jZnS_9hoN3U4GgEJq9B22QlpzY4uF7bGyLFH6if-cQw3aAcy2B-cTUzkZujq7j58HtWzLilnZ_jaDYK2MQZTOR7WV3jR8DZ012HyPNHHpWEgh3Q99_N1kHJMKs5RmSlMHaA8OqeScjy8GikTt6RSNGtpK3CNMsZxiCo75mHZaED5vcaCPqABJzTiSWd13uH4Zo8cTiqNJpEhhe7E4XndL3R-kluafVvvs0cAfJUcIHRcDNHeSsgZLilrL0GLHTJL_pBqcNA_q0b31RDocH3Y496Zr776uNrWJfTxVF5eZptik4dpUxrjnJfFPF4ZTg9Ysm7JM9uLem73dLVi-5ag7AkVS_-RYMhCEDwnGqBkZ5xr2MDqkhWxOK0eQ0eshdWL4aL2wHiABKtBe8Tc7KnvmtDz1wqhFVm5T-16SbuLTkAgIIcvdPONjw5DhHmGfPRC8b3MjqMxCbY6jwhUBmmX4BD-qFL-7A2VnFmau3SjF2v_8KYL7Hrq880yfwu5hPuPIulSsFs2SYHsgmq91UeKvRBe23RMEg2FWC7mfr4xkZP95iph-5oJ4PvZZJRpisuO9-L1RRbb0pV4ca5yS9RdpnQbFoJKIxgz-FSw645K4uA8u2xwjJ7yv04RJQP2ioTfg8xs7aRRDEMKNDvtG6TZIDjGrk0yan0BS7Cf7-xEbvAP4MHNd-jsJnDRb3J0aAnceRsqgb0spGHyKi-tCjve3IA");

		Map<String, String> headers = new HashMap<>();
		headers.put("agentid", "uid-d0280bdf-8fbd-4a8f-bac8-cf1373ecfbf1");
		headers.put("cookie", ".AspNetCore.Session=CfDJ8JKrZDF426hNre42bGoboFZWZu43S8aoRjBQHTV9v5BHWkzxz%2B9%2FArlRxy9Ry1cpl42Lmt6BbZst67O1%2BBQgEPZAP%2F2qaDc1wekuqvTWm4EZKYmoZnWaUxTslvaFZWIbI%2BfSmDGZF5t8Cb%2FzeerQtKVrA8wXV1ZyOfzv0NRaOqdA; __stripe_mid=4674c04e-4aa5-455c-b362-e67162be1cc7aeb2f7; __ddg1_=p2TqlBvjWGL6q8mermdA; BrowserId=d243597c-3fbd-4cb6-8735-97697d5d4c20; tempDataId=gkmzuql54v; __stripe_sid=3f186d98-3e50-4d33-ae90-5526ee66b0f14436ad; lang=ru; Bearer=CfDJ8FQTi_BdrXtPgWha8MoxrYjOhvKofp3iRTqTuymUERMxdPUOYQFGtoG8Cfd54KkwEQMi09UV2OMwCX-uW3iiEl-gzUu9C1mYL2SyfJ4ClaChlpFAQwLiGfTlHyPyIt_7jZnS_9hoN3U4GgEJq9B22QlpzY4uF7bGyLFH6if-cQw3aAcy2B-cTUzkZujq7j58HtWzLilnZ_jaDYK2MQZTOR7WV3jR8DZ012HyPNHHpWEgh3Q99_N1kHJMKs5RmSlMHaA8OqeScjy8GikTt6RSNGtpK3CNMsZxiCo75mHZaED5vcaCPqABJzTiSWd13uH4Zo8cTiqNJpEhhe7E4XndL3R-kluafVvvs0cAfJUcIHRcDNHeSsgZLilrL0GLHTJL_pBqcNA_q0b31RDocH3Y496Zr776uNrWJfTxVF5eZptik4dpUxrjnJfFPF4ZTg9Ysm7JM9uLem73dLVi-5ag7AkVS_-RYMhCEDwnGqBkZ5xr2MDqkhWxOK0eQ0eshdWL4aL2wHiABKtBe8Tc7KnvmtDz1wqhFVm5T-16SbuLTkAgIIcvdPONjw5DhHmGfPRC8b3MjqMxCbY6jwhUBmmX4BD-qFL-7A2VnFmau3SjF2v_8KYL7Hrq880yfwu5hPuPIulSsFs2SYHsgmq91UeKvRBe23RMEg2FWC7mfr4xkZP95iph-5oJ4PvZZJRpisuO9-L1RRbb0pV4ca5yS9RdpnQbFoJKIxgz-FSw645K4uA8u2xwjJ7yv04RJQP2ioTfg8xs7aRRDEMKNDvtG6TZIDjGrk0yan0BS7Cf7-xEbvAP4MHNd-jsJnDRb3J0aAnceRsqgb0spGHyKi-tCjve3IA");

		Connection connection = Jsoup.connect(url)
//				.cookies(cookies)
				.headers(headers)
				.method(Connection.Method.GET)
				.ignoreContentType(true);

		try {
			Document document = connection.get();
			String body = document.body().text();
			System.out.println("Response: " + body);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
