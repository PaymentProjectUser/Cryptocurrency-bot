package com.course.change.demo.repository;

import com.course.change.demo.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {

    @Query("select c from Cryptocurrency c where c.symbolName IN (?1)")
    List<Cryptocurrency> findAllIdsByTestPlayerIs(List<String> symbolNames);
}
