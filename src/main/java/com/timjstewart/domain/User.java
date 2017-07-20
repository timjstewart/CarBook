package com.timjstewart.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends ResourceSupport
{
    UUID uuid;
    private String name;

    protected User()
    {
        // Required by Hibernate/Jackson
    }

    public User(String name)
    {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(final UUID uuid)
    {
        this.uuid = uuid;
    }

    @Column(nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}


