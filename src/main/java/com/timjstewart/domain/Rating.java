package com.timjstewart.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(name = "ratings", uniqueConstraints = @UniqueConstraint(columnNames = {
    "car_uuid", "user_uuid"
}))
public class Rating extends ResourceSupport
{
    private UUID uuid;
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_uuid",
        foreignKey = @ForeignKey(name = "CAR_UUID_FK"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Car getCar()
    {
        return car;
    }

    public void setCar(Car car)
    {
        this.car = car;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid",
        foreignKey = @ForeignKey(name = "USER_UUID_FK"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}


