package com.dbs.service;

import com.dbs.models.StockStats;

import java.util.List;

public interface StockStatsService {

    List<StockStats> getAllStocksByCode (String tradeCode);

    List<StockStats> getAllStock ();

    void saveStock(List<StockStats> stockStats);

    void deleteStockByCodeAndRecordTime(String code, String recordTime);

    void deleteAllStockStats();

    void saveStockStatsFromFile();

}
