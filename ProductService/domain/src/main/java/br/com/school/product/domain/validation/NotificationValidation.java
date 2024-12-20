package br.com.school.product.domain.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class NotificationValidation {
    
    @Getter
    private List<Error> errors;

    private NotificationValidation(List<Error> errors) {
        this.errors = errors;
    }

    public static NotificationValidation create() {
        return new NotificationValidation(new ArrayList<>());
    }

    public NotificationValidation append(Error error) {
        errors.add(error);
        return this;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
