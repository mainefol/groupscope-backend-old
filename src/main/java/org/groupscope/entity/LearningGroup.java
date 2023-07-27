package org.groupscope.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * This class allows to unite and manage our group of learners.
 * Represents a learning group that contains learners and subjects.
 */

@Data
@Entity
@Table(name = "groups")
public class LearningGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String inviteCode;

    // Every group have a headman
    @OneToOne(cascade = CascadeType.ALL, targetEntity = Learner.class)
    @JoinColumn(name = "headmen_id")
    private Learner headmen;

    // Every group has subjects that the headmen has added
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<Subject> subjects;

    // List of learners in the group.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Learner.class)
    @JoinColumn(name = "group_id")
    private List<Learner> learners;

    public LearningGroup() {
        // Generate a random invite code for the group.
        SecureRandom secureRandom = new SecureRandom();
        this.inviteCode = new BigInteger(32, secureRandom).toString(32) + this.getId().toString();
        this.subjects = new ArrayList<>();
    }

    public LearningGroup(String groupName) {
        this.name = groupName;

        // Generate a random invite code for the group.
        SecureRandom secureRandom = new SecureRandom();
        this.inviteCode = new BigInteger(32, secureRandom).toString(32) + this.getId().toString();
        this.subjects = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningGroup that = (LearningGroup) o;
        return Objects.equals(name, that.name) && Objects.equals(inviteCode, that.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inviteCode);
    }
}
