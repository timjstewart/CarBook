package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.presentation.RatedCar;
import com.timjstewart.repository.CarRepository;
import com.timjstewart.repository.RatingRepository;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class CarController
{
    static final String ROUTE_COLLECTIVE = "/cars";
    static final String ROUTE_SINGLE = "/cars/{id}";

    private static Logger LOG = LoggerFactory.getLogger(CarController.class);

    private final CarRepository repository;
    private final RatingRepository ratingRepository;

    protected CarController(final CarRepository carRepository, final RatingRepository ratingRepository)
    {
        this.repository = carRepository;
        this.ratingRepository = ratingRepository;
    }

    @RequestMapping(path = ROUTE_COLLECTIVE, method = RequestMethod.GET)
    public Page<RatedCar> getMany(@RequestParam(name = "year", required = false) Integer year,
                                  Pageable pageable)
    {
        if (year == null)
        {
            return addRatings(addLinks(repository.findAll(pageable)));
        }
        else
        {
            return addRatings(addLinks(repository.findAllByYear(year, pageable)));
        }
    }

    @RequestMapping(path = ROUTE_SINGLE, method = RequestMethod.GET)
    public HttpEntity<RatedCar> getOne(@PathVariable UUID id)
    {
        final Car car = repository.findOne(id);
        if (car == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(addRatings(addLinks(car)),
                HttpStatus.OK);
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

    private RatedCar addRatings(final Car car)
    {
        return new RatedCar(car, ratingRepository.countByCarUuid(car.getUuid()));
    }

    private Page<RatedCar> addRatings(Page<Car> cars)
    {
        return cars.map(this::addRatings);
    }

    private HttpHeaders getCreatedHeaders(final Car car) throws URISyntaxException
    {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Link location =
            linkTo(methodOn(CarController.class).getOne(car.getUuid())).withSelfRel();
        responseHeaders.setLocation(new URI(location.getHref()));
        return responseHeaders;
    }
}