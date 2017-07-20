package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.domain.Rating;
import com.timjstewart.domain.User;
import com.timjstewart.repository.CarRepository;
import com.timjstewart.repository.RatingRepository;
import com.timjstewart.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RatingControllerTest
{
    @Autowired
    private RatingRepository repository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate template;

    @After
    public void setUp()
    {
        repository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void canCreateRating()
    {
        // Arrange
        Car car = carRepository.save(new Car(1984, "Honda"));
        User user = userRepository.save(new User("Bill"));

        final Rating rating = new Rating(3);

        final Rating created =
            template.postForObject(RatingController.ROUTE_SINGLE, rating, Rating.class,
                car.getUuid(), user.getUuid());

        // Act
        final Rating found = repository.findOne(created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getCar().getMake()).isEqualTo("Honda");
        assertThat(found.getCar().getYear()).isEqualTo(1984);
    }

    @Test
    public void canDeleteRating()
    {
        // Arrange
        final User user = userRepository.save(new User("Bill"));
        final Car car = carRepository.save(new Car(1984, "Honda"));
        final Rating rating = new Rating(3);
        rating.setUser(user);
        rating.setCar(car);
        final Rating created = repository.save(rating);

        // Act
        template.delete(RatingController.ROUTE_SINGLE,
            created.getCar().getUuid(), created.getUser().getUuid());

        // Assert
        final Rating found = repository.findOne(created.getUuid());
        assertThat(found).isNull();
    }

    @Test
    public void canGetRating()
    {
        // Arrange
        final Rating rating = new Rating(3);
        rating.setUser(userRepository.save(new User("Bill")));
        rating.setCar(carRepository.save(new Car(1984, "Honda")));

        final Rating created = repository.save(rating);

        // Act
        final Rating found = template
            .getForObject(RatingController.ROUTE_SINGLE, Rating.class, created.getCar().getUuid(),
                created.getUser().getUuid());

        // Assert
        assertThat(found).isNotNull();
    }

    @Test
    public void canUpdateRating()
    {
        // Arrange
        Car car = carRepository.save(new Car(1984, "Honda"));
        User user = userRepository.save(new User("Bill"));

        final Rating rating = new Rating(3);
        rating.setCar(car);
        rating.setUser(user);
        final Rating created = repository.save(rating);

        // Act
        rating.setStars(4);
        template.put(RatingController.ROUTE_SINGLE, created, car.getUuid(), user.getUuid());

        // Assert
        final Rating found = repository.findOne(created.getUuid());
        assertThat(found).isEqualTo(created);
    }
}

