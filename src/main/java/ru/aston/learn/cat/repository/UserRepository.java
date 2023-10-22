package ru.aston.learn.cat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import ru.aston.learn.cat.db.DataSourceSupplier;
import ru.aston.learn.cat.entity.User;

@Slf4j
public class UserRepository {

    DataSource dataSource = DataSourceSupplier.getInstance();


    public void save(User user) {
        //language=SQL
        String query = """
        insert into users (id, name, login, password) values (?,?,?,?)
        """;
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, user.getId());
                statement.setString(2,user.getName());
                statement.setString(3,user.getLogin());
                statement.setBytes(4,user.getPassword());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Can't save user with id = {}",user.getId(),e);
        }

    }

    public boolean containsByLogin(String login) {
        //language=SQL
        String query = """
        SELECT * FROM users u WHERE u.login = ?
        """;
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            log.error("Can't find user by login {}", login, e);
            throw new RuntimeException(e);
        }
    }

}
