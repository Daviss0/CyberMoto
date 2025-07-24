package com.cybermoto.utils;

import com.cybermoto.entity.Client;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatches, Client> {

    @Override
    public boolean isValid(Client client, ConstraintValidatorContext context) {
        if (client.getPassword() == null || client.getConfPassword() == null) {
            return false;
        }
        return client.getPassword().equals(client.getConfPassword());
    }
}


