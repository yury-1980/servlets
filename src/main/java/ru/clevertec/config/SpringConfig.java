package ru.clevertec.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.clevertec.util.gson.LocalDateAdapter;
import ru.clevertec.util.gson.LocalDateSerializer;
import ru.clevertec.util.gson.OffsetDateTimeAdapter;
import ru.clevertec.util.gson.OffsetDateTimeSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Configuration
@ComponentScan("ru.clevertec")
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Value("${enabled}")
    private boolean enabled;

    @Value("${change-log}")
    private String changeLog;

    @Value("${capacity}")
    private String capacity;

    @Value("${url}")
    private String url;

    @Value("${user}")
    private String userName;

    @Value("${password}")
    private String password;

    @Value("${driver}")
    private String driver;
    private final HikariDataSource DATA_SOURCE = new HikariDataSource();

    @Bean
    public Gson json() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
                .create();
    }

    @Bean
    public Connection getConnection() throws SQLException {
        DATA_SOURCE.setJdbcUrl(url);
        DATA_SOURCE.setUsername(userName);
        DATA_SOURCE.setPassword(password);
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setAutoCommit(false);

        return DATA_SOURCE.getConnection();
    }

    @PostConstruct
    private void configLiquibase() {
        try {

            if (enabled) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
                Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void connectionClose() {
        DATA_SOURCE.close();
    }
}