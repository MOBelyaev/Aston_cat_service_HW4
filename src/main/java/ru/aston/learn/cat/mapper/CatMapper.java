package ru.aston.learn.cat.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.aston.learn.cat.client.response.CatImageResponse;
import ru.aston.learn.cat.client.response.breed.CatBreed;
import ru.aston.learn.cat.client.response.breed.CatWeight;
import ru.aston.learn.cat.entity.cat.Breed;
import ru.aston.learn.cat.entity.cat.Cat;

public class CatMapper {

    private static CatMapper instance;

    public static synchronized CatMapper getInstance() {
        if (instance == null) {
            instance = new CatMapper();
        }
        return instance;
    }

    private CatMapper() {
    }

    public Cat toCatEntity(CatImageResponse src) {
        return Cat.builder()
                .id(src.getId())
                .imageUrl(src.getUrl())
                .imageHeight(src.getHeight())
                .imageWidth(src.getWidth())
                .breeds(toBreedEntities(src.getBreeds()))
                .build();
    }

    public List<Breed> toBreedEntities(List<CatBreed> src) {
        List<Breed> result = new ArrayList<>();
        for (CatBreed breed : src) {
            result.add(toBreedEntity(breed));
        }
        return result;
    }

    public Breed toBreedEntity(CatBreed src) {
        String weight = Optional.ofNullable(src.getWeight())
                .map(CatWeight::getMetric)
                .orElse(null);

        return Breed.builder()
                .id(src.getId())
                .weight(weight)
                .name(src.getName())
                .temperament(src.getTemperament())
                .origin(src.getOrigin())
                .description(src.getDescription())
                .lifeSpan(src.getLifeSpan())
                .build();
    }
}
