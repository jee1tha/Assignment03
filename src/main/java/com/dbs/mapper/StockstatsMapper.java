package com.dbs.mapper;

import com.dbs.models.StockStats;
import com.dbs.models.StockStatsArchieve;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockstatsMapper {
    StockStatsArchieve toArchieve (StockStats stockStats);
    List<StockStatsArchieve> toArchieveList ( List<StockStats> stockStats);
}
