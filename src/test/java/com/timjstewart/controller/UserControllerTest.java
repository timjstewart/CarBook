package com.timjstewart.controller;

import com.timjstewart.domain.User;
import com.timjstewart.repository.UserRepository;
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
public class UserControllerTest
{

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void canCreateUser()
    {
        // Arrange
        final User created =
            template.postForObject(UserController.ROUTE_COLLECTIVE, new User("Fred"), User.class);

        // Act
        final User found = repository.findOne(created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Fred");
    }

    @Test
    public void canDeleteUser()
    {
        // Arrange
        final User created = repository.save(new User("Fred"));

        // Act
        template.delete(UserController.ROUTE_SINGLE, created.getUuid());

        // Assert
        final User found = repository.findOne(created.getUuid());
        assertThat(found).isNull();
    }

    @Test
    public void canGetUser()
    {
        // Arrange
        final User created = repository.save(new User("Fred"));

        // Act
        final User found =
            template.getForObject(UserController.ROUTE_SINGLE, User.class, created.getUuid());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Fred");
    }

    @Test
    public void canUpdateUser()
    {
        // Arrange
        final User created = repository.save(new User("Fred"));

        // Act
        created.setName("Frederick");
        template.put(UserController.ROUTE_SINGLE, created, created.getUuid());

        // Assert
        final User found = repository.findOne(created.getUuid());
        assertThat(found).isEqualTo(created);
    }
}

