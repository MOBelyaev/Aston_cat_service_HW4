package ru.aston.learn.cat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.aston.learn.cat.entity.cat.Breed;

@Slf4j
public class BreedRepository {

    private static BreedRepository instance;

    public synchronized static BreedRepository getInstance() {
        if (instance == null) {
            instance = new BreedRepository();
        }
        return instance;

    }

    public List<Breed> findByCatId(Connection connection, String catId) throws SQLException {
        //language=SQL
        String query = """
        select b.id as id,
               b.name as "name",
               b.description as "desc",
               b.temperament as "temp",
               b.weight as weight,
               b.origin as origin,
               b.lifespan as lifespan
        from cat_breed cb
        left outer join breed b on cb.breed_id=b.id
        where cb.cat_id=?
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, catId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Breed> breeds = new ArrayList<>();
                while (resultSet.next()) {
                    breeds.add(map(resultSet));
                }
                return breeds;
            }
        }
    }

    private Breed map(ResultSet resultSet) throws SQLException {
        return Breed.builder()
                .id(resultSet.getString("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("desc"))
                .temperament(resultSet.getString("temp"))
                .weight(resultSet.getString("weight"))
                .origin(resultSet.getString("origin"))
                .lifeSpan(resultSet.getString("lifespan"))
                .build();
    }

    public void saveIfNotExist(Connection connection, List<Breed> src) throws SQLException {
        //language=SQL
        String query = """
                insert into breed(id, weight, name, temperament, origin, description, lifespan) values
                (?,?,?,?,?,?,?)
                on conflict do nothing
                """;
        for (Breed breed : src) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, breed.getId());
                statement.setString(2, breed.getWeight());
                statement.setString(3, breed.getName());
                statement.setString(4, breed.getTemperament());
                statement.setString(5, breed.getOrigin());
                statement.setString(6, breed.getDescription());
                statement.setString(7, breed.getLifeSpan());

                statement.executeUpdate();
            } catch (SQLException e) {
                log.error("Can't save breed with id {}", breed.getId(), e);
                throw e;
            }
        }
    }
}
