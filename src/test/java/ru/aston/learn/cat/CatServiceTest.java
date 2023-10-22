package ru.aston.learn.cat;

import org.junit.jupiter.api.Test;
import ru.aston.learn.cat.client.CatClient;

public class CatServiceTest {

    @Test
    public void probe() {
        try (CatClient client = CatClient.getInstance()) {
            System.out.println(client.getCatImages(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
