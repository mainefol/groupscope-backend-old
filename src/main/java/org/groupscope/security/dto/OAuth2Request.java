package org.groupscope.security.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OAuth2Request {

    @NotEmpty
    private String IdToken;

    private String learnerName;

    private String learnerLastname;

    private String inviteCode;

    private String groupName;

    public RegistrationRequest toRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setLearnerName(this.getLearnerName());
        request.setLearnerLastname(this.getLearnerLastname());
        request.setInviteCode(this.getInviteCode());
        request.setGroupName(this.getGroupName());
        return request;
    }
}
