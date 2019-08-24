package com.jyami.algoreaderbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaekJoonResDto {
    private long number;
    private String name;
    private String category;
    private String time;
    private long collectCount;
    private long submitCount;
    private float collectRate;

}
