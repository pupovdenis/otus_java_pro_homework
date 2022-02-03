package ru.pupov.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.cachehw.HwCache;
import ru.pupov.cachehw.MyCache;
import ru.pupov.core.repository.DataTemplateHibernate;
import ru.pupov.core.repository.HibernateUtils;
import ru.pupov.core.sessionmanager.TransactionManagerHibernate;
import ru.pupov.crm.model.Address;
import ru.pupov.crm.model.Client;
import ru.pupov.crm.model.Phone;
import ru.pupov.crm.service.DbServiceClientImpl;

import java.util.ArrayList;

public class DbServiceDemo {

//    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);
//
//    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
//
//    public static void main(String[] args) {
//        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
//
//        var dbUrl = configuration.getProperty("hibernate.connection.url");
//        var dbUserName = configuration.getProperty("hibernate.connection.username");
//        var dbPassword = configuration.getProperty("hibernate.connection.password");
//
////        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
//
//        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
//
//        var transactionManager = new TransactionManagerHibernate(sessionFactory);
/////
//        var clientTemplate = new DataTemplateHibernate<>(Client.class);
/////
//        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
//        dbServiceClient.saveClient(new Client("dbServiceFirst"));
//
//        var client1 = Client.
//        dbServiceClient.saveClient(someClient);
//
//        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
//        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
//        log.info("clientSecondSelected:{}", clientSecondSelected);
/////
//        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
//        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
//        log.info("clientUpdated:{}", clientUpdated);
//
//        log.info("All clients");
//        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
//    }
}
