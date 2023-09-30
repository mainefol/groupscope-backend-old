package org.groupscope.security.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.groupscope.security.entity.CustomUser;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationRequest extends AuthRequest {

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
