package com.timjstewart.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ratings")
public class Rating extends ResourceSupport
{
    UUID uuid;
    private int stars;
    private Car car;
    private User user;

    protected Rating()
    {
        // Required by Hibernate/Jackson
    }

    public Rating(int stars)
    {
        this.stars = stars;
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
    public int getStars()
    {
        return stars;
    }

    public void setStars(int stars)
    {
        this.stars = stars;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "car_uuid")
    public Car getCar()
    {
        return car;
    }

    public void setCar(Car car)
    {
        this.car = car;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_uuid")
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}


