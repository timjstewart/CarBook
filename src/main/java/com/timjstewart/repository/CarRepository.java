package com.timjstewart.repository;

import com.timjstewart.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CarRepository extends PagingAndSortingRepository<Car, UUID>
{
    Page<Car> findAllByYear(int year, Pageable pageable);
}


