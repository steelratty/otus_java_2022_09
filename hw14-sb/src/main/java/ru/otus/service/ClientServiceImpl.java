package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.repostory.AddressRepository;
import ru.otus.repostory.ClientRepository;

import java.util.List;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;

    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
       this.addressRepository = addressRepository;
    }

    @Override
    public List<Client> findAll() {
        List<Client> tmpList = clientRepository.findAll();
        for (Client cli:tmpList){
          if (cli.getAddressId() !=null){
              cli.setAddress(addressRepository.findById(cli.getOAddressId()).get());
         }
        }
        return tmpList;
    }

    @Override
    public Client findById(long id) {
        Client tmpClient;
        tmpClient = clientRepository.findById(id).get();
        if (tmpClient.getAddressId() !=null){
            tmpClient.setAddress(addressRepository.findById(tmpClient.getOAddressId()).get());
        }
        return tmpClient;
    }

    @Override
    public Client findByName(String name) {
        return clientRepository.findByName(name);
    }

    @Override
    public Client findRandom() {
        List<Client> clients = clientRepository.findAll();
        Random r = new Random();
        return clients.stream().skip(r.nextInt(clients.size() - 1)).findFirst().orElse(null);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
