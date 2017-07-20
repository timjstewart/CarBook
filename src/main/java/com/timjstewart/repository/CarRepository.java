package com.timjstewart.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.timjstewart.domain.Car;

public interface CarRepository extends PagingAndSortingRepository<Car, UUID>
{
    Page<Car> findAllByYear(int year, Pageable pageable);
}


