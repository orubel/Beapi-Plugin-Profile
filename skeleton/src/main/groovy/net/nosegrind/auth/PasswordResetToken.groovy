package main.groovy.net.nosegrind.auth

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;

import net.nosegrind.apiframework.Person;

@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Person user;

    private Date expiryDate;
}