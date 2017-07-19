package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.repository.CarRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CarController
{
    static final String ROUTE_COLLECTIVE = "/cars";
    static final String ROUTE_SINGLE = "/cars/{id}";

    private static Logger LOG = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarRepository repository;

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.GET)
    public Page<Car> getMany(Pageable pageable)
    {
        return addLinks(repository.findAll(pageable));
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.GET)
    public HttpEntity<Car> getOne(@PathVariable UUID id)
    {
        final Car car = repository.findOne(id);
        if (car != null)
        {
            return new ResponseEntity<>(addLinks(car), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public HttpEntity<Car> updateOne(@RequestBody Car resource)
    {
        final Car car = repository.save(resource);
        return new ResponseEntity<>(addLinks(car), HttpStatus.OK);
    }

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.POST)
    public HttpEntity<Car> createOne(@RequestBody Car resource) throws URISyntaxException
    {
        final Car car = repository.save(resource);
        return new ResponseEntity<>(addLinks(car), getCreatedHeaders(car), HttpStatus.CREATED);
    }

    private Page<Car> addLinks(final Page<Car> carPage)
    {
        return carPage.map(this::addLinks);
    }

    private Car addLinks(final Car car)
    {
        car.add(linkTo(methodOn(CarController.class).getOne(car.getUuid())).withSelfRel());
        return car;
    }

    private HttpHeaders getCreatedHeaders(final Car car) throws URISyntaxException
    {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Link location = linkTo(methodOn(CarController.class).getOne(car.getUuid())).withSelfRel();
        responseHeaders.setLocation(new URI(location.getHref()));
        return responseHeaders;
    }
}


