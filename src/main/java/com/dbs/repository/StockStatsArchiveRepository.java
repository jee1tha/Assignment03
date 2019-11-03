package com.dbs.repository;

import com.dbs.models.StockStatsArchive;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockStatsArchiveRepository extends MongoRepository<StockStatsArchive, Long> {

}
