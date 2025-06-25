package com.cybermoto.service;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{

@Autowired
private UserRepository userRepository;


    @Override
  public User loginUser(String email, String password) {
      return userRepository.findByEmail(email)
              .filter(user -> new BCryptPasswordEncoder()
                      .matches(password, user.getPassword()))
              .orElse(null);
  }

  @Override
    public void saveUser(User user) {
        String senhaCriptografada = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        userRepository.save(user);
  }

}
