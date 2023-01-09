package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var clientFirst = dbServiceClient.saveClient(new Client("dbServiceFirst")).getId();
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond")).getId();
        var clientThird = dbServiceClient.saveClient(new Client("dbServiceThird")).getId();
        var clientForth = dbServiceClient.saveClient(new Client("dbServiceForth")).getId();

       for (int i = 0; i<5; i++){
            var clientFirstSelected = dbServiceClient.getClient(clientFirst)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFirst));
            log.info("clientFirstSelected:{}", clientFirstSelected);

            var clientSecondSelected = dbServiceClient.getClient(clientSecond)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond));
            log.info("clientSecondSelected:{}", clientSecondSelected);

            var clientThirdSelected = dbServiceClient.getClient(clientThird)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientThird));
            log.info("clientThirdSelected:{}", clientThirdSelected);

            var clientForthSelected = dbServiceClient.getClient(clientForth)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientForth));
            log.info("clientForthSelected:{}", clientForthSelected);
        }
    }
}
