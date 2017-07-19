package com.timjstewart.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.timjstewart.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, UUID>
{
}


