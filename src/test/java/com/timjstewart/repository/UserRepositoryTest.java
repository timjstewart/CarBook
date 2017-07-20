package com.timjstewart.repository;

import com.timjstewart.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest
{
    @Autowired
    private UserRepository repository;

    @Test
    public void canCreateUser()
    {
        // Arrange
        final User user = new User("Fred");

        // Act
        final User createdUser = repository.save(user);

        // Assert
        final User foundUser = repository.findOne(createdUser.getUuid());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(createdUser);
    }

    @Test
    public void canUpdateUser()
    {
        // Arrange
        final User user = new User("Fred");
        final User createdUser = repository.save(user);
        final User foundUser = repository.findOne(createdUser.getUuid());

        // Act
        foundUser.setName("Barney");
        repository.save(foundUser);

        // Assert
        final User updatedUser = repository.findOne(createdUser.getUuid());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).isEqualTo(foundUser);
    }

    @Test
    public void canDeleteUser()
    {
        // Arrange
        final User user = new User("Fred");
        final User createdUser = repository.save(user);

        // Act
        repository.delete(createdUser.getUuid());

        // Assert
        final User foundUser = repository.findOne(createdUser.getUuid());
        assertThat(foundUser).isNull();
    }
}