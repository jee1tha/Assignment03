package com.dbs.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stock_stats_archive")
public class StockStatsArchive extends StockStats{
}
