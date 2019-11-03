package com.dbs.service.impl;

import com.dbs.models.StockStats;
import com.dbs.repository.StockStatsRepository;
import com.dbs.service.StockStatsService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


@Service
public class StockStatsServiceImpl implements StockStatsService {

    @Autowired
    StockStatsRepository stockStatsRepository;

    @Autowired
    GoogleDriveServiceImpl googleDriveServiceImpl;

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
       if(list.size() == 0 || !hasTodayStockUpdated(list.get(list.size()-1) )){
           saveStockStatsFromFile();
           return stockStatsRepository.findByCodeOrderByRecordDateTimeAsc(tradeCode);
       }
        return list;
    }

    private boolean hasTodayStockUpdated(StockStats stockStat){
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date latestRecordDate = stockStat.getRecordDateTime();
        Date todaye = new Date();
        LocalDate date1= LocalDate.of(latestRecordDate.getYear(), latestRecordDate.getMonth(), latestRecordDate.getDate());
        LocalDate date2 = LocalDate.of(todaye.getYear(), todaye.getMonth(), todaye.getDate());
       return date1.compareTo(date2) > 0 ;
    }

    @Override
    public void saveStockStatsFromFile() {

        List<StockStats> stockStats = readFromFile(googleDriveServiceImpl.downloadFile());
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

    private List<StockStats> readFromFile(String Path) {
        List<StockStats> stockStats = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(new File(Path));
            Workbook workbook = new XSSFWorkbook(excelFile);
            for(int i =0 ; i < workbook.getNumberOfSheets(); i++) {
                Sheet datatypeSheet = workbook.getSheetAt(i);
                Iterator<Row> iterator = datatypeSheet.iterator();


            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                if(currentRow.getRowNum()==0 ){
                    continue;
                }
                if(checkIfRowIsEmpty(currentRow)){
                    continue;
                }
                Iterator<Cell> cellIterator = currentRow.iterator();
                StockStats stockStat = new StockStats();
                DataFormatter dataFormatter = new DataFormatter();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    if(cell.getCellType() == CellType.BLANK){
                        continue;
                    }
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
                }
                stockStats.add(stockStat);
            }
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
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

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !cell.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
