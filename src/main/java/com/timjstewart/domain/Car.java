package com.timjstewart.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

@Entity()
@Table(name = "cars")
public class Car extends ResourceSupport
{
    private UUID uuid;
    private String make;
    private int year;
    private Date createdAt;
    private Date modifiedAt;

    protected Car()
    {
        // Required by Hibernate
    }

    public Car(int year, String make)
    {
        this.make = make;
        this.year = year;
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
    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    @Column(nullable = false)
    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final Date createdAt)
    {
        this.createdAt = createdAt;
    }

    @Column(name = "modifiedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedAt()
    {
        return modifiedAt;
    }

    public void setModifiedAt(final Date modifiedAt)
    {
        this.modifiedAt = modifiedAt;
    }

    @PrePersist
    protected void onCreate()
    {
        createdAt = modifiedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate()
    {
        modifiedAt = new Date();
    }
}


