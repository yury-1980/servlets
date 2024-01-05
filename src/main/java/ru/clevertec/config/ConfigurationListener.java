package ru.clevertec.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.servlet.ClientServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConfigurationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.clevertec");
        context.refresh();

        ClientServlet clientServlet = context.getBean(ClientServlet.class);
        ServletContext servletContext = sce.getServletContext();
        servletContext.addServlet("clientServlet", clientServlet)
                .addMapping("/v1/clients");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
