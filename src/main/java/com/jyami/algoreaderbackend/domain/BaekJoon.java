package com.jyami.algoreaderbackend.domain;

import com.jyami.algoreaderbackend.dto.BaekJoonResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "baekjoon")
public class BaekJoon {
    @Id
    private String id;

    @Indexed
    private Long number;

    private String name;
    private String category;
    private Long collectCount;
    private Long submitCount;
    private Float collectRate;

    @Override
    public String toString() {
        return number + " : " + name;
    }

    public BaekJoonResDto toDto() {
        return BaekJoonResDto.builder()
                .number(this.number)
                .name(this.name)
                .category(this.category)
                .collectCount(this.collectCount)
                .collectRate(this.collectRate)
                .submitCount(this.submitCount)
                .build();
    }
}
