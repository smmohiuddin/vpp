package com.powerledger.vpp.repository;

import com.powerledger.vpp.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {

    @Query("SELECT b FROM Battery b WHERE b.postcode BETWEEN :startPostcode  AND :endPostcode " +
            "AND (:minCapacity IS NULL OR b.capacity >= :minCapacity) " +
            "AND (:maxCapacity IS NULL OR b.capacity <= :maxCapacity) " +
            "ORDER BY b.name ASC")
    List<Battery> findByPostcodeBetweenAndCapacityOrderByNameASC(@Param("startPostcode") String startPostcode, @Param("endPostcode") String endPostcode, @Param("minCapacity") Integer minCapacity, @Param("maxCapacity") Integer maxCapacity);
}
