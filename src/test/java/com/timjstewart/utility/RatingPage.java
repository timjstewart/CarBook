package com.timjstewart.utility;

import org.springframework.hateoas.ResourceSupport;

import com.timjstewart.domain.Rating;
import java.util.ArrayList;

public class RatingPage
{
    private int totalElements;
    private ArrayList<Rating> content;

    public int getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(final int totalElements)
    {
        this.totalElements = totalElements;
    }

    public ArrayList<Rating> getContent()
    {
        return content;
    }

    public void setContent(final ArrayList<Rating> content)
    {
        this.content = content;
    }
}


