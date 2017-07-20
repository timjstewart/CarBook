package com.timjstewart.controller;

import com.timjstewart.domain.User;
import com.timjstewart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController
{
    static final String ROUTE_COLLECTIVE = "/users";
    static final String ROUTE_SINGLE = "/users/{id}";

    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserRepository repository;

    protected UserController(final UserRepository repository)
    {
        this.repository = repository;
    }

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.GET)
    public Page<User> getMany(Pageable pageable)
    {
        return addLinks(repository.findAll(pageable));
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.GET)
    public HttpEntity<User> getOne(@PathVariable UUID id)
    {
        final User user = repository.findOne(id);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(addLinks(user), HttpStatus.OK);
        }
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.DELETE)
    public HttpEntity<Void> deleteOne(@PathVariable UUID id)
    {
        try
        {
            repository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.PUT)
    public HttpEntity<User> updateOne(@RequestBody User resource)
    {
        final User user = repository.save(resource);
        return new ResponseEntity<>(addLinks(user), HttpStatus.OK);
    }

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.POST)
    public HttpEntity<User> createOne(@RequestBody User resource) throws URISyntaxException
    {
        final User user = repository.save(resource);
        return new ResponseEntity<>(addLinks(user), getCreatedHeaders(user), HttpStatus.CREATED);
    }

    private Page<User> addLinks(final Page<User> userPage)
    {
        return userPage.map(this::addLinks);
    }

    private User addLinks(final User user)
    {
        user.add(linkTo(methodOn(UserController.class).getOne(user.getUuid())).withSelfRel());
        return user;
    }

    private HttpHeaders getCreatedHeaders(final User user) throws URISyntaxException
    {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Link location =
            linkTo(methodOn(UserController.class).getOne(user.getUuid())).withSelfRel();
        responseHeaders.setLocation(new URI(location.getHref()));
        return responseHeaders;
    }
}


