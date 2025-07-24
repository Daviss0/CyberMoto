package com.cybermoto.service;

import com.cybermoto.entity.Client;
import com.cybermoto.repository.ClientRepository;
import com.cybermoto.utils.CpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    ClientRepository clientRepository;

    @Override
    public void addClient(Client client) {
        if(clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Email já existe no sistema");
        }
        if(clientRepository.existsByCpf(client.getCpf())) {
            throw new IllegalArgumentException("CPF já existe no sistema");
        }
        if (!CpfValidator.isCpfValid(client.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(client.getPassword());
        client.setPassword(senhaCriptografada);

        clientRepository.save(client);
    }
}
