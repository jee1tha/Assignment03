package com.dbs.repository;

import com.dbs.models.StockStatsArchieve;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockStatsArchieveRepository extends MongoRepository<StockStatsArchieve, Long> {

}
