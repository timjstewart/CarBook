package com.timjstewart.repository;

import com.timjstewart.domain.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, UUID>
{
    Rating findByCarUuidAndUserUuid(UUID carUuid, UUID userUuid);

    Page<Rating> findByCarUuid(UUID carUuid, Pageable pageable);

    int countByCarUuid(UUID carUUid);
}


