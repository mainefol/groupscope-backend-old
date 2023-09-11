package org.groupscope.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.groupscope.entity.Learner;
import org.groupscope.entity.Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/*
 * This class represents a custom user entity for authentication and authorization in the system.
 * It implements the UserDetails interface required by Spring Security for user authentication.
 */

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class CustomUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    // The authentication provider of the user (e.g., LOCAL or GOOGLE).
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    // One-to-one relationship with the Learner entity. Each user has a corresponding learner.
    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "learner_id")
    private Learner learner;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(learner.getRole().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User: " + this.getLogin();
    }
}