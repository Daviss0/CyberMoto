package com.cybermoto.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

    @Documented
    @Constraint(validatedBy = PasswordMatchersValidator.class)
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordMatches {
        String message() default "As senhas não coincidem";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

