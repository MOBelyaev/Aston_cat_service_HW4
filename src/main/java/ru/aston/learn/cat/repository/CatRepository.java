package ru.aston.learn.cat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import ru.aston.learn.cat.db.DataSourceSupplier;
import ru.aston.learn.cat.entity.cat.Breed;
import ru.aston.learn.cat.entity.cat.Cat;

@Slf4j
public class CatRepository {

    private static CatRepository instance;

    public static synchronized CatRepository getInstance() {
        if (instance == null) {
            instance = new CatRepository();
        }
        return instance;
    }

    private final BreedRepository breedRepository = BreedRepository.getInstance();
    private final DataSource dataSource = DataSourceSupplier.getInstance();

    private CatRepository() {
    }

    public List<Cat> findPage(int page, int pageSize) {
        // language=SQL
        String query = """
        select c.id as id,
               c.image_url as imageUrl,
               c.image_height as imageHeight,
               c.image_width as imageWidth
        from cats c limit ? offset ?
        """;

        int offset = page * pageSize;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, pageSize);
                statement.setInt(2, offset);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Cat> cats = new ArrayList<>();
                    while (resultSet.next()) {
                        Cat cat = map(resultSet);
                        cat.setBreeds(breedRepository.findByCatId(connection, cat.getId()));
                        cats.add(cat);
                    }
                    return cats;
                }
            }
        } catch (SQLException e) {
            log.error("Can't extract cats page", e);
            throw new RuntimeException(e);
        }
    }

    private Cat map(ResultSet resultSet) throws SQLException {
        return Cat.builder()
                .id(resultSet.getString("id"))
                .imageUrl(resultSet.getString("imageUrl"))
                .imageHeight("imageHeight")
                .imageWidth("imageWidth")
                .build();
    }

    /**
     * Сохраняет в БД изображение с котом и информацию о породах в транзакции. Также обновляет
     * данные в связующей таблице cat_breed.
     *
     * @param cat Сущность для сохранения
     */
    public void saveIfNotExist(Cat cat) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try {
                saveCatIfNotExist(connection, cat);

                breedRepository.saveIfNotExist(connection, cat.getBreeds());

                saveCatBreedReference(
                        connection,
                        cat.getId(),
                        cat.getBreeds().stream().map(Breed::getId).toList()
                );
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Transaction save cat failed", e);
            throw new RuntimeException(e);
        }
    }

    public void saveCatIfNotExist(Connection connection, Cat cat) throws SQLException {
        //language=SQL
        String query = """
                insert into cats(id, image_url, image_width, image_height) values (?, ?, ?, ?) on conflict do nothing
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cat.getId());
            statement.setString(2, cat.getImageUrl());
            statement.setString(3, cat.getImageWidth());
            statement.setString(4, cat.getImageHeight());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't save cat image with id {}", cat.getId());
            throw e;
        }
    }

    private void saveCatBreedReference(Connection connection,
                                       String catId,
                                       List<String> breedIds) throws SQLException {
        //language=SQL
        String query = """
                insert into cat_breed(cat_id, breed_id) values (?, ?) on conflict do nothing
                """;
        for (String breedId : breedIds) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, catId);
                statement.setString(2, breedId);

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Can't save cat-breed relation. catId={}, breedId={}", catId, breedId);
                throw e;
            }
        }
    }
}
