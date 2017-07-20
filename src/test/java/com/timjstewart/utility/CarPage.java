package com.timjstewart.utility;

import com.timjstewart.domain.Car;

import java.util.ArrayList;

public class CarPage
{
    private int totalElements;
    private ArrayList<Car> content;

    public int getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(final int totalElements)
    {
        this.totalElements = totalElements;
    }

    public ArrayList<Car> getContent()
    {
        return content;
    }

    public void setContent(final ArrayList<Car> content)
    {
        this.content = content;
    }
}


