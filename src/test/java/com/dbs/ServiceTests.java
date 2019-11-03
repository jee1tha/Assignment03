package com.dbs;

import com.dbs.models.StockStats;
import com.dbs.repository.StockStatsRepository;
import com.dbs.service.StockStatsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SpringBootTest(classes = StockServiceApplication.class)
@RunWith(SpringRunner.class)
public class ServiceTests {

    @Autowired
    StockStatsService stockStatsService;

    @Autowired
    StockStatsRepository stockStatsRepository;

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
        stockStatsService.saveStockStatsFromFile();
        List<StockStats>  stockStats = stockStatsService.getAllStocksByCode("NASDAQ:GOOGL");
        System.out.println(stockStats.size());
        stockStats.forEach(stockStat -> {
            System.out.print(stockStat.getCode() + " ");
            System.out.print(stockStat.getName()+ " ");
            System.out.print(stockStat.getRecordDateTime()+ " ");
            System.out.print(stockStat.getPrice()+ " ");
            System.out.println();

            Assert.assertTrue(stockStat.getCode().equals("NASDAQ:GOOGL"));

        }
        );
        Assert.assertNotNull(stockStats.size());
    }

    @Test
    public void getAllStocksTest(){
        stockStatsService.saveStockStatsFromFile();
        List<StockStats>  stockStats =stockStatsService.getAllStock();
        Assert.assertNotNull(stockStats.size());
        stockStatsRepository.deleteAll();
    }

}
