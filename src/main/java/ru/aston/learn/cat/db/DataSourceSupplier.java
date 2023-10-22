package ru.aston.learn.cat.db;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import ru.aston.learn.cat.service.PropertyService;

public class DataSourceSupplier {

    private static final PropertyService propertyService = PropertyService.getInstance();

    private static DataSource instance;

    public synchronized static DataSource getInstance() {
        if (instance == null) {
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(propertyService.get("db.url"));
            dataSource.setUser(propertyService.get("db.user"));
            dataSource.setPassword(propertyService.get("db.password"));
            instance = dataSource;
        }
        return instance;
    }


}
