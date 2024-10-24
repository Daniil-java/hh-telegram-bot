package com.education.hhtelegrambot.entities.hh;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HhSimpleResponseDto {
    private String url;
    private Long hhId;
}
