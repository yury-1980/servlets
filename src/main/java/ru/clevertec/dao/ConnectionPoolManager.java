package ru.clevertec.dao;

import com.zaxxer.hikari.HikariDataSource;
import ru.clevertec.util.reader.ReaderConfig;
import ru.clevertec.util.reader.impl.ReaderConfigImpl;

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
    private static final ReaderConfig READER_CONFIG = new ReaderConfigImpl();
    private static final Properties PROPERTIES = READER_CONFIG.readerFileYaml(CONFIG_DB);
    private static final HikariDataSource DATA_SOURCE = new HikariDataSource();

    static {
        DATA_SOURCE.setJdbcUrl(PROPERTIES.getProperty(URL));
        DATA_SOURCE.setUsername(PROPERTIES.getProperty(USER_NAME));
        DATA_SOURCE.setPassword(PROPERTIES.getProperty(PASSWORD));
        DATA_SOURCE.setDriverClassName(PROPERTIES.getProperty(DRIVER));
        DATA_SOURCE.setAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static int getCapacity() {
        return Integer.parseInt(PROPERTIES.getProperty(CAPACITY));
    }

    public static void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
