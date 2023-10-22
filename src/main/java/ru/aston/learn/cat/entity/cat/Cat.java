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
public class Cat {

    private String id;
    private String imageUrl;
    private String imageWidth;
    private String imageHeight;

    private List<Breed> breeds;
}
