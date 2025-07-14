package com.cybermoto.service;

import com.cybermoto.entity.User;
import com.cybermoto.enums.StatusUser;
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

        boolean isEdit = user.getId() != null;

        if (!isEdit && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Campo E-mail é obrigatório");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório");
        }

        if (user.getCpf() == null || user.getCpf().isBlank()) {
            throw new IllegalArgumentException("Campo CPF é obrigatório");
        }

        if (!CpfValidator.isCpfValid(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (user.getType() == null) {
            throw new IllegalArgumentException("O tipo de usuário é obrigatório");
        }

        User userToSave = isEdit
                ? userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"))
                : new User();

        userToSave.setName(user.getName());
        userToSave.setEmail(user.getEmail());
        userToSave.setCpf(user.getCpf());
        userToSave.setType(user.getType());
        userToSave.setStatus(user.getStatus());

        // senha só é alterada se campo estiver preenchido
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            if (!user.getPassword().equals(user.getConfPassword())) {
                throw new IllegalArgumentException("Senhas não coincidem");
            }
            String senhaCriptografada = new BCryptPasswordEncoder().encode(user.getPassword());
            userToSave.setPassword(senhaCriptografada);
        }

        userRepository.save(userToSave);
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if(user.getStatus() == StatusUser.ATIVO) {
            user.setStatus(StatusUser.INATIVO);
            userRepository.save(user);
        }
        else  {
            user.setStatus(StatusUser.ATIVO);
        }
            userRepository.save(user);
    }
}
