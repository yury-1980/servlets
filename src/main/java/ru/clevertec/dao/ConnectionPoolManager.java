//package ru.clevertec.dao;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Component
//@PropertySource("classpath:application.properties")
//public class ConnectionPoolManager {
//
//    @Value("${capacity}")
//    private String capacity;
//
//    @Value("${url}")
//    private String url;
//
//    @Value("${user}")
//    private String userName;
//
//    @Value("${password}")
//    private String password;
//
//    @Value("${driver}")
//    private String driver;
//    //    private  final String CONFIG_DB = "application.yaml";
////    private  final ReaderConfig READER_CONFIG = new ReaderConfigImpl();
////    private  final Properties PROPERTIES = READER_CONFIG.readerFileYaml(CONFIG_DB);
//    private final HikariDataSource DATA_SOURCE = new HikariDataSource();
//
//    {
//        DATA_SOURCE.setJdbcUrl(url);
//        DATA_SOURCE.setUsername(userName);
//        DATA_SOURCE.setPassword(password);
//        DATA_SOURCE.setDriverClassName(driver);
//        DATA_SOURCE.setAutoCommit(false);
//    }
//
//    public Connection getConnection() throws SQLException {
//        return DATA_SOURCE.getConnection();
//    }
//
//    public int getCapacity() {
//        return Integer.parseInt(capacity);
//    }
//}
