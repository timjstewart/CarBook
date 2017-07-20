package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.domain.Rating;
import com.timjstewart.domain.User;
import com.timjstewart.presentation.RatedCar;
import com.timjstewart.repository.CarRepository;
import com.timjstewart.repository.RatingRepository;
import com.timjstewart.repository.UserRepository;
import com.timjstewart.utility.CarPage;
import com.timjstewart.utility.RatedCarPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CarControllerTest
{

    @Autowired
    private CarRepository repository;

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp()
    {
//        ratingRepository.deleteAll();
//        repository.deleteAll();
//        userRepository.deleteAll();
    }

    @Test
    public void canCreateCar()
    {
        // Arrange
        final Car created = template.postForObject(CarController.ROUTE_COLLECTIVE,
            new Car(1973, "Ford"), Car.class);

        // Act
        final Car found = repository.findOne(created.getUuid());

        // Assert
        assertThat(found).isNotNull();
    }

    @Test
    public void canDeleteCar()
    {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        template.delete(CarController.ROUTE_SINGLE, created.getUuid());

        // Assert
        final Car found = repository.findOne(created.getUuid());
        assertThat(found).isNull();
    }

    @Test
    public void canGetCar()
    {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        final RatedCar found = template.getForObject(CarController.ROUTE_SINGLE,
            RatedCar.class, created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getRatings()).isEqualTo(0);
    }

    @Test
    public void canUpdateCar()
    {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        created.setYear(1974);
        template.put(CarController.ROUTE_SINGLE, created, created.getUuid());

        // Assert
        final Car found = repository.findOne(created.getUuid());
        assertThat(found).isEqualTo(created);
    }

    @Test
    public void canComputeRatings()
    {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));
        final User fred = userRepository.save(new User("Fred"));
        final User wilma = userRepository.save(new User("Wilma"));

        // Give the car one rating.
        final Rating fredRating = new Rating(3);
        fredRating.setUser(fred);
        fredRating.setCar(created);
        ratingRepository.save(fredRating);

        // Give the car a second rating from another user.
        final Rating wilmaRating = new Rating(2);
        wilmaRating.setUser(wilma);
        wilmaRating.setCar(created);
        ratingRepository.save(wilmaRating);

        // Act
        final RatedCar found = template.getForObject(CarController.ROUTE_SINGLE,
            RatedCar.class, created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getRatings()).isEqualTo(2);
    }

    @Test
    public void canFindByYear()
    {
        // Arrange
        repository.save(new Car(1973, "Ford"));
        repository.save(new Car(1973, "Chevy"));
        repository.save(new Car(2000, "Honda"));

        // Act
        final CarPage cars = template.getForObject(CarController.ROUTE_COLLECTIVE + "?year=1973",
            CarPage.class);

        // Assert
        assertThat(cars.getContent().size()).isEqualTo(2);
        assertThat(cars.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void getManyIncludesRating()
    {
        // Arrange
        final Car car = repository.save(new Car(1973, "Ford"));
        final User user = userRepository.save(new User("Barney"));

        final Rating rating = new Rating(3);
        rating.setCar(car);
        rating.setUser(user);
        ratingRepository.save(rating);

        // Act
        final RatedCarPage cars =
            template.getForObject(CarController.ROUTE_COLLECTIVE + "?year=1973",
                RatedCarPage.class);

        // Assert
        // Find the car we just added.
        final Optional<RatedCar> ratedCar = cars.getContent().stream()
            .filter(x -> x.getCar().getUuid().equals(car.getUuid())).findFirst();
        assertThat(ratedCar.isPresent());
        assertThat(ratedCar.get().getRatings()).isEqualTo(1);
    }
}

