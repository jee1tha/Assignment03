package com.dbs.service.mapper;

import com.dbs.models.StockStats;
import com.dbs.models.StockStatsArchive;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockstatsMapper {
    StockStatsArchive toarchive (StockStats stockStats);
    List<StockStatsArchive> toarchiveList ( List<StockStats> stockStats);
}
