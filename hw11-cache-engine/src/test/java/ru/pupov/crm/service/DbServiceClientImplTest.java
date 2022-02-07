package ru.pupov.crm.service;

import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.core.repository.DataTemplateHibernate;
import ru.pupov.core.repository.HibernateUtils;
import ru.pupov.core.sessionmanager.TransactionManagerHibernate;
import ru.pupov.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.pupov.crm.model.Address;
import ru.pupov.crm.model.Client;
import ru.pupov.crm.model.Phone;

import java.util.ArrayList;
import java.util.List;

class DbServiceClientImplTest {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImplTest.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    @Test
    public void checkCacheWork() throws InterruptedException {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        List<Client> clients = new Client().getFakes(10);

        clients.forEach(dbServiceClient::saveClient);
        clients = dbServiceClient.findAll();

        List<Client> resultsWithCache = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        clients.forEach(it -> dbServiceClient.getClient(it.getId()).ifPresent(resultsWithCache::add));
        long timeWithCache = (System.currentTimeMillis() - startTime);
        logger.info("Time with cache = {}ms", timeWithCache);

        dbServiceClient.clearCache();

        List<Client> resultsNoCache = new ArrayList<>();
        startTime = System.currentTimeMillis();
        clients.forEach(it -> dbServiceClient.getClient(it.getId()).ifPresent(resultsNoCache::add));
        long timeNoCache = (System.currentTimeMillis() - startTime);
        logger.info("Time without cache = {}ms", timeNoCache);

        int cacheSizeBefore = dbServiceClient.getCacheSize();
        logger.info("Cache size before System.gc() = {}", cacheSizeBefore);

        System.gc();
        Thread.sleep(100);

        int cacheSizeAfter = dbServiceClient.getCacheSize();
        logger.info("Cache size after System.gc() = {}", cacheSizeAfter);


        Assertions.assertTrue(timeWithCache < timeNoCache);
        Assertions.assertTrue(resultsWithCache.size() > 0);
        Assertions.assertTrue(resultsNoCache.size() > 0);
        Assertions.assertTrue(cacheSizeAfter < cacheSizeBefore);
    }
}