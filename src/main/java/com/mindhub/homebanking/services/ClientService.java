package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Client findByEmail(String email);

    void save(Client client);

    Client findById(Long id);

}
