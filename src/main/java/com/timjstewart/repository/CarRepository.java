package com.timjstewart.repository;

import com.timjstewart.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarRepository extends PagingAndSortingRepository<Car, UUID>
{
    Page<Car> findAllByYear(int year, Pageable pageable);
}


