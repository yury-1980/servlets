package ru.clevertec.dao;

import com.zaxxer.hikari.HikariDataSource;
import ru.clevertec.reader.ReaderConfig;
import ru.clevertec.reader.impl.ReaderConfigImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolManager {

    private static final String CAPACITY = "capacity";
    private static final String URL = "url";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";
    private static final String DRIVER = "driver";
    private static final String CONFIG_DB = "application.yaml";
    private static final ReaderConfig readerConfig = new ReaderConfigImpl();
    private static final Properties properties = readerConfig.readerFileYaml(CONFIG_DB);
    private static final HikariDataSource dataSource = new HikariDataSource();

    static {
        dataSource.setJdbcUrl(properties.getProperty(URL));
        dataSource.setUsername(properties.getProperty(USER_NAME));
        dataSource.setPassword(properties.getProperty(PASSWORD));
        dataSource.setDriverClassName(properties.getProperty(DRIVER));
        dataSource.setAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static int getCapacity() {
        return Integer.parseInt(properties.getProperty(CAPACITY));
    }

    public static void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
