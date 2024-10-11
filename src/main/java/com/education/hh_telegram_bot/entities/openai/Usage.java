package com.education.hh_telegram_bot.entities.openai;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Usage {
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
