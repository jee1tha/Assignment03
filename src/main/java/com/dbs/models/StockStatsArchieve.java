package com.dbs.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "stock_stats_archieve")
public class StockStatsArchieve extends StockStats{
}
