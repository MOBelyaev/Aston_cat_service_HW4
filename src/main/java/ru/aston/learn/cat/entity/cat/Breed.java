package ru.aston.learn.cat.entity.cat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Breed {

    private String id;
    private String weight;
    private String name;
    private String temperament;
    private String origin;
    private String description;
    private String lifeSpan;

    private List<Cat> cats;
}
