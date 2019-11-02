package com.dbs.controller;

import com.dbs.models.StockStats;
import com.dbs.service.StockStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class StockServiceController {

    @Autowired
    private StockStatsService stockStatsService;

    @RequestMapping(value = "/google",method = RequestMethod.GET)
    public String hello(HttpServletRequest request, Model model){
        List<StockStats> stockStats = stockStatsService.getAllStocksByCode("NASDAQ:GOOGL");
        model.addAttribute("stockstats",stockStats );
        model.addAttribute("code", stockStats.get(0).getCode());
        model.addAttribute("name", stockStats.get(0).getName());
        model.addAttribute("finalprice", stockStats.get(stockStats.size()-1).getPrice());
        model.addAttribute("openprice", stockStats.get(0).getPrice());
        model.addAttribute("highprice", getHighestPrice(stockStats));
        model.addAttribute("lowprice", getLowestPrice(stockStats));
        float priceChange =  getChange(stockStats.get(stockStats.size()-1).getPrice(),
                stockStats.get(0).getPrice());
        model.addAttribute("change", String.format("%+.02f",priceChange));
        model.addAttribute("changepercentage",String.format("%.02f",getChangePercentage(priceChange,Float.parseFloat(stockStats.get(stockStats.size()-1).getPrice()))));
        model.addAttribute("closed", stockStats.get(stockStats.size()-1).getRecordDateTime());

        return "google";
    }

    private float getChange(String PriceFirst,String priceLast){
       return Float.parseFloat(priceLast)-Float.parseFloat(PriceFirst);
    }

    private float getChangePercentage(float changedPrice, float finalPrice){
       return (changedPrice/finalPrice)* 100;
    }

    private float getLowestPrice(List<StockStats> stockStats ){

        float minValue = Float.parseFloat(stockStats.get(0).getPrice());
        for(int i=1;i<stockStats.size();i++){
            if(Float.parseFloat(stockStats.get(i).getPrice()) < minValue){
                minValue = Float.parseFloat(stockStats.get(i).getPrice()) ;
            }
        }
        return minValue;
    }
    private float getHighestPrice(List<StockStats> stockStats ){

        float highValue = Float.parseFloat(stockStats.get(0).getPrice());
        for(int i=1;i<stockStats.size();i++){
            if(Float.parseFloat(stockStats.get(i).getPrice()) > highValue){
                highValue = Float.parseFloat(stockStats.get(i).getPrice()) ;
            }
        }
        return highValue;
    }
}

