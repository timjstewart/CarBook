package com.timjstewart.repository;

import com.timjstewart.domain.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarRepositoryTest
{

    @Autowired
    private CarRepository repository;

    @Test
    public void canCreateCar()
    {
        // Arrange
        final Car car = new Car(2001, "Jaguar");

        // Act
        final Car createdCar = repository.save(car);

        // Assert
        final Car foundCar = repository.findOne(createdCar.getUuid());
        assertThat(foundCar).isNotNull();
        assertThat(foundCar).isEqualTo(createdCar);
    }

    @Test
    public void canUpdateCar()
    {
        // Arrange
        final Car car = new Car(1958, "Chevy");
        final Car createdCar = repository.save(car);
        final Car foundCar = repository.findOne(createdCar.getUuid());

        // Act
        foundCar.setYear(1957);
        foundCar.setMake("Ford");
        repository.save(foundCar);

        // Assert
        final Car updatedCar = repository.findOne(createdCar.getUuid());
        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar).isEqualTo(foundCar);
    }

    @Test
    public void canDeleteCar()
    {
        // Arrange
        final Car car = new Car(1956, "Fiat");
        final Car createdCar = repository.save(car);

        // Act
        repository.delete(createdCar.getUuid());

        // Assert
        final Car foundCar = repository.findOne(createdCar.getUuid());
        assertThat(foundCar).isNull();
    }
}