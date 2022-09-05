package com.Lommunity.domain.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query(value = "select r from regions r where r.level = '3' and r.fullname like %:word%")
    List<Region> findRegionByWord(@Param("word") String word);
}
