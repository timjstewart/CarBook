package com.timjstewart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cars")
public class Car extends ResourceSupport
{
    UUID uuid;

    protected Car()
    {
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

    public Car(int year, String make) {
        this.make = make;
        this.year = year;
    }

    private String make;
    private int year;

    @Column(nullable=false)
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Column(nullable=false)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}


