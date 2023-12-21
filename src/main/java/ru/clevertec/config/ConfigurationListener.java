package ru.clevertec.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.service.impl.ClientServiceImpl;
import ru.clevertec.servlet.ClientServlet;
import ru.clevertec.valid.Validator;
import ru.clevertec.valid.impl.ValidatorImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Properties;

@WebListener
public class ConfigurationListener implements ServletContextListener {

    private static final String ENABLED = "true";
    private static final String CHANGE_LOG = "change-log";
    private static final String ENABLED_LIQUIBASE = "enabled";
    private static final String APPLICATION_YAML = "application.yaml";
    private static final Validator VALIDATOR = new ValidatorImpl();
    private static final MapperClient MAPPER_CLIENT = new MapperClientImpl();
    private static final ClientDao CLIENT_DAO = new ClientDaoImpl();
    private static final ClientService CLIENT_SERVICE = new ClientServiceImpl(CLIENT_DAO, MAPPER_CLIENT, VALIDATOR);

    public static ClientService getClientService() {
        return CLIENT_SERVICE;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);

        try {
            Config.getConfig();
            ConnectionPoolManager.getConnection();
            Properties properties = new Properties();
            properties.load(ClientServlet.class.getClassLoader().getResourceAsStream(APPLICATION_YAML));
            String enabled = properties.getProperty(ENABLED_LIQUIBASE);
            String changeLog = properties.getProperty(CHANGE_LOG);

            if (enabled.equals(ENABLED)) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(ConnectionPoolManager.getConnection()));
                Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
