package com.jyami.algoreaderbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaekJoonResDto {
    private long number;
    private String name;
    private String category;
    private String time;
    private long collectCount;
    private long submitCount;
    private float collectRate;

}
