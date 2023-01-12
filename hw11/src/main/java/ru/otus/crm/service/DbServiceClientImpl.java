package ru.otus.crm.service;

import net.bytebuddy.dynamic.DynamicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HWCacheDemo;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.cachehw.MyCache;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private static final HwCache<String, Client> cache = new MyCache<>();
    private static final Logger loggerH = LoggerFactory.getLogger(HWCacheDemo.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public String get_cash_key(Long lKey){
        return "KEY:"+ lKey.toString();
    }

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        HwListener<String, Client> listener = new HwListener<>() {
            @Override
            public void notify(String key, Client value, String action) {
                loggerH.info("key:{}, value:{}, action: {}", key, value==null ? "null":value.toString(), action);
            }
        };
        cache.addListener(listener);

    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                cache.put(get_cash_key(clientCloned.getId()), clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            cache.put(get_cash_key(clientCloned.getId()), clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            /* получаем из кэша */
            Client cli = cache.get(get_cash_key(id));
            if (cli != null) {
                return Optional.of(cli);
            }

            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            if (clientOptional.isPresent()) {
                cache.put(get_cash_key(clientOptional.get().getId()), clientOptional.get());
            }
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
