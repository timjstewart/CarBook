package com.timjstewart.controller;

import com.timjstewart.domain.Car;
import com.timjstewart.repository.CarRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class CarControllerTest {

    @Autowired
    private CarRepository repository;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void canCreateCar() {
        // Arrange
        final Car created = template.postForObject(CarController.ROUTE_COLLECTIVE, new Car(1973, "Ford"), Car.class);

        // Act
        final Car found = repository.findOne(created.getUuid());

        // Assert
        assertThat(found).isNotNull();
    }

    @Test
    public void canDeleteCar() {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        template.delete(CarController.ROUTE_SINGLE, created.getUuid());

        // Assert
        final Car found = repository.findOne(created.getUuid());
        assertThat(found).isNull();
    }

    @Test
    public void canGetCar() {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        final Car found = template.getForObject(CarController.ROUTE_SINGLE, Car.class, created.getUuid());

        // Assert
        assertThat(found).isNotNull();
    }

    @Test
    public void canUpdateCar() {
        // Arrange
        final Car created = repository.save(new Car(1973, "Ford"));

        // Act
        created.setYear(1974);
        template.put(CarController.ROUTE_SINGLE, created, created.getUuid());

        // Assert
        final Car found = repository.findOne(created.getUuid());
        assertThat(found).isEqualTo(created);
    }
}

