package com.dbs.repository;

import com.dbs.models.StockStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

public interface StockStatsRepository extends MongoRepository<StockStats, Long> {

    List<StockStats> findByCodeOrderByRecordDateTimeAsc(String code);

    List<StockStats> deleteAllByRecordDateTimeBefore(Date date);

    List<StockStats> getAllByRecordDateTimeBefore(Date date);

}
