package com.dbs;

import com.dbs.models.StockStats;
import com.dbs.service.StockStatsService;
import junit.framework.Assert;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootTest(classes = StockServiceApplication.class)
@RunWith(SpringRunner.class)
public class ServiceTests {

    @Autowired
    StockStatsService stockStatsService;

    public  Date getDateFromString(String dateStr) {
        String dateFormat = "dd/MM/yyyy HH:mm a";
        Date date = null;
        try {
             date = new SimpleDateFormat(dateFormat,Locale.ENGLISH).parse(dateStr.replace("GMT-4", ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    @Test
    public void getStocksTest(){
        deleteAllStockStats();
        stockStatsService.saveStockStatsFromFile();
        List<StockStats>  stockStats = stockStatsService.getAllStocksByCode("NASDAQ:GOOGL");
        System.out.println(stockStats.size());
        stockStats.forEach(stockStat -> {
            System.out.print(stockStat.getCode() + " ");
            System.out.print(stockStat.getName()+ " ");
            System.out.print(stockStat.getRecordDateTime()+ " ");
            System.out.print(stockStat.getPrice()+ " ");
            System.out.println();
                }
        );
    }

    @Test
    public void deleteAllStockStats(){
        stockStatsService.deleteAllStockStats();
        List<StockStats> stockStats = stockStatsService.getAllStock();
        Assert.assertTrue(0 == stockStats.size() );
    }

    @Test
    public void deleteStocksByCodeAndRecordTimeTest(){
      stockStatsService.deleteStockByCodeAndRecordTime("NASDAQ:GOOGL","11/10/2019 09:00 am GMT-4");
    }

}
