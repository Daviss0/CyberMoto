package com.cybermoto.service;

import com.cybermoto.entity.Client;
import com.cybermoto.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findAll()
                .stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return org.springframework.security.core.userdetails.User
                .withUsername(client.getEmail())
                .password(client.getPassword())
                .roles("CLIENT")
                .build();

    }
}
