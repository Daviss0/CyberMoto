package com.cybermoto.service;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.cybermoto.utils.CpfValidator;

@Service
public class UserServiceImpl implements UserService{

@Autowired
private UserRepository userRepository;




  @Override
    public void saveUser(User user) throws IllegalArgumentException {

      if(userRepository.existsByEmail(user.getEmail())) {
         throw new IllegalArgumentException("E-mail ja cadastrado no sistema");
      }
      if(user.getEmail() == null || user.getEmail().isBlank()) {
          throw new IllegalArgumentException("Campo E-mail é obrigatório");
      }
      if(user.getPassword() == null || user.getPassword().isBlank()) {
          throw new IllegalArgumentException("A senha é obrigatória");
      }
      if(!user.getPassword().equals(user.getConfPassword())) {
          throw new IllegalArgumentException("Senhas não coincidem");
      }
      if (user.getType() == null) {
          throw new IllegalArgumentException("O tipo de usuário é obrigatório");
      }
      if(user.getName() == null || user.getName().isBlank()) {
          throw new IllegalArgumentException("O nome é obrigatório");
      }
      if(user.getCpf() == null || user.getCpf().isBlank()) {
          throw new IllegalArgumentException("Campo CPF é obrigatório");
      }
      if(!CpfValidator.isCpfValid(user.getCpf())) {
         throw new IllegalArgumentException("CPF inválido");
      }
        String senhaCriptografada = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        userRepository.save(user);
  }

}
