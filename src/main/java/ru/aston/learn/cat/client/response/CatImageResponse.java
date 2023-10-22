package ru.aston.learn.cat.client.response;

import java.util.List;
import lombok.Data;
import ru.aston.learn.cat.client.response.breed.CatBreed;

@Data
public class CatImageResponse {

    private String id;
    private String url;
    private String width;
    private String height;
    private List<CatBreed> breeds;
}
