package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.domain.Rating;
import com.timjstewart.domain.User;
import com.timjstewart.repository.CarRepository;
import com.timjstewart.repository.RatingRepository;
import com.timjstewart.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RatingControllerTest {

    @Autowired
    private RatingRepository repository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate template;

    private Car car;
    private User user;

    @Before
    public void setUp() {
        car = carRepository.save(new Car(1984, "Honda"));
        user = userRepository.save(new User("Bill"));
    }

    @Test
    public void canCreateRating() {
        // Arrange
        final Rating rating = new Rating(3);

        final Rating created = template.postForObject(RatingController.ROUTE_SINGLE, rating, Rating.class,
                car.getUuid(), user.getUuid());

        // Act
        final Rating found = repository.findOne(created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getCar().getMake()).isEqualTo("Honda");
        assertThat(found.getCar().getYear()).isEqualTo(1984);
    }

    @Test
    public void canDeleteRating() {
        // Arrange
        final Rating rating = new Rating(3);
        rating.setUser(user);
        rating.setCar(car);
        final Rating created = repository.save(rating);

        // Act
        template.delete(RatingController.ROUTE_SINGLE, created.getUuid());

        // Assert
        final Rating found = repository.findOne(created.getUuid());
        assertThat(found).isNull();
    }

    @Test
    public void canGetRating() {
        // Arrange
        final Rating rating = new Rating(3);
        rating.setUser(user);
        rating.setCar(car);
        final Rating created = repository.save(rating);

        // Act
        final Rating found = template.getForObject(RatingController.ROUTE_SINGLE, Rating.class, created.getUuid());

        // Assert
        assertThat(found).isNotNull();
    }

    @Test
    public void canUpdateRating() {
        // Arrange
        final Rating rating = new Rating(3);
        rating.setUser(user);
        rating.setCar(car);
        final Rating created = repository.save(rating);

        // Act
        rating.setStars(4);
        template.put(RatingController.ROUTE_SINGLE, created, created.getUuid());

        // Assert
        final Rating found = repository.findOne(created.getUuid());
        assertThat(found).isEqualTo(created);
    }
}

