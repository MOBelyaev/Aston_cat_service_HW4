package ru.aston.learn.cat.service;

import java.util.List;
import ru.aston.learn.cat.client.CatClient;
import ru.aston.learn.cat.client.response.CatImageResponse;
import ru.aston.learn.cat.entity.cat.Cat;
import ru.aston.learn.cat.mapper.CatMapper;
import ru.aston.learn.cat.repository.CatRepository;

public class CatService {

    private static CatService instance;

    public static CatService getInstance() {
        if (instance == null) {
            instance = new CatService();
        }
        return instance;
    }

    private final CatClient client = CatClient.getInstance();
    private final CatMapper mapper = CatMapper.getInstance();
    private final CatRepository catRepository = CatRepository.getInstance();

    private CatService() {
    }

    public void enrichCatData(int cnt) {
        List<CatImageResponse> images = client.getCatImages(cnt);

        for (CatImageResponse image : images) {
            catRepository.saveIfNotExist(mapper.toCatEntity(image));
        }
    }

    public List<Cat> getCatImages(int page, int pageSize) {
        return catRepository.findPage(page, pageSize);
    }
}
