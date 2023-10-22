package ru.aston.learn.cat.client.response.breed;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CatBreed {

    private String id;
    private CatWeight weight;
    private String name;
    private String temperament;
    private String origin;
    private String description;
    @JsonProperty("life_span")
    private String lifeSpan;
}
