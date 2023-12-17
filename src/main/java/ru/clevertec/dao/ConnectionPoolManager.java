package ru.clevertec.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.clevertec.reader.ReaderConfig;
import ru.clevertec.reader.impl.ReaderConfigImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionPoolManager {

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static final String URL = "url";
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER = "driver";
    private static final String CONFIG_DB = "config.yaml";
    private final ReaderConfig readerConfig = new ReaderConfigImpl();
    private final Map<String, String> STRING_MAP = readerConfig.readerFileYaml(CONFIG_DB);

    {

        dataSource.setUrl(STRING_MAP.get(URL));
        dataSource.setUsername(STRING_MAP.get(USER_NAME));
        dataSource.setPassword(STRING_MAP.get(PASSWORD));
        dataSource.setDriverClassName(STRING_MAP.get(DRIVER));
        dataSource.setDefaultAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
