package com.dbs.service;

import com.dbs.models.StockStats;
import com.dbs.repository.StockStatsRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Service
public class StockStatsServiceImpl implements StockStatsService{

    @Autowired
    StockStatsRepository stockStatsRepository;

    @Override
    public List<StockStats> getAllStocksByCode(String tradeCode) {
        switch (tradeCode){
            case "google" : tradeCode = "NASDAQ:GOOGL";
                break;
            case "apple" : tradeCode = "NASDAQ:APPL";
                break;
            case "fb" : tradeCode = "NASDAQ:FB";
                break;


        }
        List<StockStats> list = stockStatsRepository.findByCodeOrderByRecordDateTimeAsc(tradeCode);
        list.sort(Comparator.comparing(o -> o.getRecordDateTime()));
        return list;

    }

    @Override
    public void saveStockStatsFromFile() {
        String Path = "/home/jee1tha/MyProjects/DBS/Assignment03/src/main/resources/assignment-trade.xls";
        List<StockStats> stockStats = readFromFile(Path);
        saveStock(stockStats);
    }

    // Service Method to fetch All Stock Stats from DB
    @Override
    public List<StockStats> getAllStock() {
        return stockStatsRepository.findAll();
    }

    @Override
    public void saveStock(List<StockStats> stockStats) {
        // Looping through the List of Stock stats and persisting in the DB
        stockStats.stream().forEach(stockstat -> {
            // Setting a unique ID from the available stats
            stockstat.set_id(stockstat.getCode().concat(stockstat.getRecordDateTime().toString()));
            stockStatsRepository.save(stockstat);
        });

    }

    @Override
    public void deleteStockByCodeAndRecordTime(String code, String record) {
        stockStatsRepository.deleteByCodeAndAndRecordDateTime(code,record);
    }

    @Override
    public void deleteAllStockStats() {
        stockStatsRepository.deleteAll();
    }

    private List<StockStats> readFromFile(String Path) {
        List<StockStats> stockStats = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(
                    new File(Path));
            DataFormatter dataFormatter = new DataFormatter();
            System.out.println("Retrieving Sheets");
            workbook.forEach(sheet -> {
                System.out.println("\n\nIterating over Rows and Columns \n");
                sheet.forEach(row -> {
                    // Skipping header row
                    if(row.getRowNum() == 0 )
                        return;
                    StockStats stockStat = new StockStats();
                    row.forEach(cell -> {
                        switch (cell.getColumnIndex()){
                            case 0 : stockStat.setCode(dataFormatter.formatCellValue(cell));
                                break;
                            case 1 : stockStat.setName(dataFormatter.formatCellValue(cell));
                                break;
                            case 2 : stockStat.setRecordDateTime(getDateFromString(dataFormatter.formatCellValue(cell)));
                                break;
                            case 3 : stockStat.setPrice(dataFormatter.formatCellValue(cell));
                                break;
                        }
                        String cellValue = dataFormatter.formatCellValue(cell);
                        System.out.print(cellValue + "\t");
                    });
                    stockStats.add(stockStat);
                    System.out.println();
                });
            });
        } catch ( IOException e ) {
            e.printStackTrace();
        };
        return stockStats;
    }

    private Date getDateFromString(String dateStr) {
        Date date = null;
        try {
            DateFormat parseFromatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a",Locale.ENGLISH);
             date = parseFromatter.parse(dateStr.replace("GMT-4", ""));
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  date;
    }

}
