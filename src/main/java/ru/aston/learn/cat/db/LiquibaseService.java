package ru.aston.learn.cat.db;

import java.sql.Connection;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiquibaseService {

    private static LiquibaseService instance;

    public synchronized static LiquibaseService getInstance() {
        if (instance == null) {
            instance = new LiquibaseService();
        }
        return instance;
    }

    private final DataSource dataSource = DataSourceSupplier.getInstance();

    private LiquibaseService() {
    }

    public void start() {
        try (Connection connection = dataSource.getConnection()) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog.xml", new ClassLoaderResourceAccessor(), database
            );

            liquibase.update(new Contexts());

            liquibase.close();
        } catch (Exception e) {
            log.error("Can't execute liquibase update", e);
            throw new RuntimeException(e);
        }
    }
}
