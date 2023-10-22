package ru.aston.learn.cat;

import ru.aston.learn.cat.db.LiquibaseService;

public class CatApplication {

    public static void main(String[] args) {

        LiquibaseService.getInstance().start();
    }
}
