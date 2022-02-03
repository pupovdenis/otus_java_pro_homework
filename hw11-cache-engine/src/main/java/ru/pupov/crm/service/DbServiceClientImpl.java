package ru.pupov.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.cachehw.HwCache;
import ru.pupov.cachehw.HwListener;
import ru.pupov.cachehw.MyCache;
import ru.pupov.core.repository.DataTemplate;
import ru.pupov.core.sessionmanager.TransactionManager;
import ru.pupov.crm.model.Client;
import ru.pupov.str.Data;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final HwCache<String, Client> cache = new MyCache<>();

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
//                log.info("created client: {}", clientCloned);
            } else {
                clientDataTemplate.update(session, clientCloned);
//                log.info("updated client: {}", clientCloned);
            }
            cache.put(clientCloned.getId().toString(), clientCloned);
            log.info("Cache size after saving = {}", cache.getCacheSize());
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (cache.getCacheSize() == 0) {
            log.warn("Cache is empty");
        }
        var client = Optional.ofNullable(cache.get(String.valueOf(id)));
        if (client.isPresent()) {
            return client;
        }
        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
//            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(value -> cache.put(value.getId().toString(), value));
            log.info("Cache size after getting from db = {}", cache.getCacheSize());
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(session -> {
            //            log.info("clientList:{}", clientList);
            return clientDataTemplate.findAll(session);
       });
    }

    public void clearCache() {
        log.info("Clear cache");
        cache.clear();
    }

    public int getCacheSize() {
        return cache.getCacheSize();
    }
}
