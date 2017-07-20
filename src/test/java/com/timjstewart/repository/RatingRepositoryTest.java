package com.timjstewart.repository;

import com.timjstewart.domain.Car;
import com.timjstewart.domain.Rating;
import com.timjstewart.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RatingRepositoryTest
{
    @Autowired
    private RatingRepository repository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void canCreateRating()
    {
        // Arrange
        final Rating rating = new Rating(4);
        rating.setCar(carRepository.save(new Car(1932, "VW")));
        rating.setUser(userRepository.save(new User("Tim")));

        // Act
        final Rating createdRating = repository.save(rating);

        // Assert
        final Rating foundRating = repository.findOne(createdRating.getUuid());
        assertThat(foundRating).isNotNull();
        assertThat(foundRating).isEqualTo(createdRating);
    }

    @Test
    public void canUpdateRating()
    {
        // Arrange
        final Rating rating = new Rating(2);
        rating.setCar(carRepository.save(new Car(1932, "VW")));
        rating.setUser(userRepository.save(new User("Tim")));
        final Rating createdRating = repository.save(rating);
        final Rating foundRating = repository.findOne(createdRating.getUuid());

        // Act
        foundRating.setStars(1);
        repository.save(foundRating);

        // Assert
        final Rating updatedRating = repository.findOne(createdRating.getUuid());
        assertThat(updatedRating).isNotNull();
        assertThat(updatedRating).isEqualTo(foundRating);
    }

    @Test
    public void canDeleteRating()
    {
        // Arrange
        final Rating rating = new Rating(5);
        rating.setCar(carRepository.save(new Car(1932, "VW")));
        rating.setUser(userRepository.save(new User("Tim")));
        final Rating createdRating = repository.save(rating);

        // Act
        repository.delete(createdRating.getUuid());

        // Assert
        final Rating foundRating = repository.findOne(createdRating.getUuid());
        assertThat(foundRating).isNull();
    }

    @Test(expected = java.lang.Exception.class)
    public void cannotDoubleRate()
    {
        // Arrange
        final Car car = carRepository.save(new Car(1932, "VW"));
        final User user = userRepository.save(new User("Tim"));

        final Rating validRating = new Rating(5);
        validRating.setCar(car);
        validRating.setUser(user);
        repository.save(validRating);

        // Act
        final Rating invalidRating = new Rating(3);
        invalidRating.setCar(car);
        invalidRating.setUser(user);
        repository.save(invalidRating);
    }
}