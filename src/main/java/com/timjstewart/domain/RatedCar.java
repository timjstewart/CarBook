package com.timjstewart.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RatedCar
{
    private Car car;
    private int ratings;

    protected RatedCar()
    {
        // Required by Hibernate/Jackson
    }

    public RatedCar(final Car car, int ratings)
    {
        this.car = car;
        this.ratings = ratings;
    }

    @JsonUnwrapped
    public Car getCar()
    {
        return car;
    }

    public void setCar(final Car car)
    {
        this.car = car;
    }

    public int getRatings()
    {
        return ratings;
    }

    public void setRatings(final int ratings)
    {
        this.ratings = ratings;
    }
}
