package com.main.model.validators;

import com.main.model.User;

import java.util.Objects;

public class UtilizatorValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String msg = "";
        if(entity.getFirstName() == null || Objects.equals(entity.getFirstName(), ""))
            msg += "First name empty!\n";
        if(entity.getLastName() == null || Objects.equals(entity.getLastName(), ""))
            msg += "Last name empty!\n";
        if(msg.length() > 0)
            throw new ValidationException(msg);
    }
}
