package com.timjstewart.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.timjstewart.domain.Rating;

public interface RatingRepository extends PagingAndSortingRepository<Rating, UUID>
{
    Rating findByCarUuidAndUserUuid(UUID carUuid, UUID userUuid);
    Page<Rating> findByCarUuid(UUID carUuid, Pageable pageable);
    int countByCarUuid(UUID carUUid);
}


