package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.domain.Rating;
import com.timjstewart.domain.User;
import com.timjstewart.repository.CarRepository;
import com.timjstewart.repository.RatingRepository;
import com.timjstewart.repository.UserRepository;
import org.hibernate.Hibernate;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class RatingController
{
    static final String ROUTE_COLLECTIVE = "/cars/{carId}/ratings";
    static final String ROUTE_SINGLE = "/cars/{carId}/ratings/user/{userId}";

    private static Logger LOG = LoggerFactory.getLogger(RatingController.class);

    private final RatingRepository repository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    protected RatingController(
        final RatingRepository repository,
        final CarRepository carRepository,
        final UserRepository userRepository)
    {
        this.repository = repository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.GET)
    public Page<Rating> getMany(@PathVariable UUID carId, Pageable pageable)
    {
        return addLinks(repository.findByCarUuid(carId, pageable));
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.GET)
    public HttpEntity<Rating> getOne(@PathVariable UUID carId, @PathVariable UUID userId)
    {
        final Rating rating = repository.findByCarUuidAndUserUuid(carId, userId);

        Hibernate.initialize(rating);
        if (rating == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(addLinks(rating), HttpStatus.OK);
        }
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.DELETE)
    public HttpEntity<Void> deleteOne(@PathVariable UUID carId, @PathVariable UUID userId)
    {
        try
        {
            Rating rating = repository.findByCarUuidAndUserUuid(carId, userId);
            repository.delete(rating.getUuid());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.POST)
    public HttpEntity<Rating> updateOne(@PathVariable UUID carId, @PathVariable UUID userId,
                                        @RequestBody Rating resource)
    {
        final Car car = carRepository.findOne(carId);
        final User user = userRepository.findOne(userId);
        resource.setCar(car);
        resource.setUser(user);
        final Rating rating = repository.save(resource);
        return new ResponseEntity<>(addLinks(rating), HttpStatus.OK);
    }

    private Page<Rating> addLinks(final Page<Rating> ratingPage)
    {
        return ratingPage.map(this::addLinks);
    }

    private Rating addLinks(final Rating rating)
    {
        rating.add(linkTo(methodOn(RatingController.class)
            .getOne(rating.getCar().getUuid(), rating.getUser().getUuid())).withSelfRel());
        return rating;
    }

    private HttpHeaders getCreatedHeaders(final Rating rating) throws URISyntaxException
    {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Link location = linkTo(methodOn(RatingController.class)
            .getOne(rating.getCar().getUuid(), rating.getUser().getUuid())).withSelfRel();
        responseHeaders.setLocation(new URI(location.getHref()));
        return responseHeaders;
    }
}


