package com.dbs.repository;

import com.dbs.models.StockStats;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockStatsRepository extends MongoRepository<StockStats, Long> {

    void deleteByCodeAndAndRecordDateTime(String code,String recordTime);

    List<StockStats> findByCodeOrderByRecordDateTimeAsc(String code);
}
