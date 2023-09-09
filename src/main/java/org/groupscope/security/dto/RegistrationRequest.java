package org.groupscope.security.dto;

import lombok.Data;
import org.groupscope.entity.CustomUser;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    private String learnerName;

    private String learnerLastname;

    private String inviteCode;

    private String groupName;

    public CustomUser toUser() {
        CustomUser customUser = new CustomUser();
        customUser.setPassword(this.getPassword());
        customUser.setLogin(this.getLogin());

        return customUser;
    }

    public boolean isValid() {
        return (login != null && login.length() != 0) && (password != null && password.length() != 0);
    }

    @Override
    public String toString() {
        return "RegistrationRequest {" +
                "login = '" + login + '\'' +
                ", password = '" + password + '\'' +
                '}';
    }
}
