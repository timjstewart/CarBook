package com.timjstewart.utility;

import org.springframework.hateoas.ResourceSupport;

import com.timjstewart.domain.User;
import java.util.ArrayList;

public class UserPage
{
    private int totalElements;
    private ArrayList<User> content;

    public int getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(final int totalElements)
    {
        this.totalElements = totalElements;
    }

    public ArrayList<User> getContent()
    {
        return content;
    }

    public void setContent(final ArrayList<User> content)
    {
        this.content = content;
    }
}


