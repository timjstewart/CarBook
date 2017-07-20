package com.timjstewart.repository;

import com.timjstewart.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<User, UUID>
{
}


