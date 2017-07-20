package com.timjstewart.utility;

import com.timjstewart.presentation.RatedCar;

import java.util.ArrayList;

public class RatedCarPage
{
    private int totalElements;
    private ArrayList<RatedCar> content;

    public int getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(final int totalElements)
    {
        this.totalElements = totalElements;
    }

    public ArrayList<RatedCar> getContent()
    {
        return content;
    }

    public void setContent(final ArrayList<RatedCar> content)
    {
        this.content = content;
    }
}


