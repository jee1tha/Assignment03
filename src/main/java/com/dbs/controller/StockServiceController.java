package com.dbs.controller;

import com.dbs.models.StockStats;
import com.dbs.service.StockStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * This Controller retrieves data from the service layer and pass it on to the
 * thymeleaf template
 *
 * @author  Jeewantha
 */
@Controller
public class StockServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(StockServiceController.class);

    @Autowired
    private StockStatsService stockStatsService;

    @RequestMapping(value = "/{company}",method = RequestMethod.GET)
    public String getStockDetails(@PathVariable(value = "company") final String company, Model model) throws Exception    {
        LOG.info("[getStockDetails] --> for Company : " + company);

        List<StockStats> stockStats = stockStatsService.getAllStocksByCode(company);
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
        model.addAttribute("closed", datetoGMT(stockStats.get(stockStats.size()-1).getRecordDateTime()));

        return "stock";
    }

    private String datetoGMT(Date date){
        // Create a DateFormat and set the timezone to GMT.
        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
            return df.format(date);
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

