package com.education.hh_telegram_bot.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtil {
    public static void sleep(int milliseconds, String logError) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error(logError, e);
        }
    }
}
