package com.dbs.service.cronjobs;

import com.dbs.service.mapper.StockstatsMapper;
import com.dbs.models.StockStats;
import com.dbs.models.StockStatsArchieve;
import com.dbs.repository.StockStatsArchieveRepository;
import com.dbs.repository.StockStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class CronJobs {
    private static final Logger LOG = LoggerFactory.getLogger(CronJobs.class);

    @Autowired
    StockStatsRepository stockStatsRepository;

    @Autowired
    StockStatsArchieveRepository archieveRepository;

    @Autowired
    StockstatsMapper stockstatsMapper;

    @Scheduled(cron = "0 0 * 3 * *")
    public void housekeepOldrecords(){

        LOG.info("Starting Cron Job");

        try{
            SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //formattedDate.setTimeZone(TimeZone.getTimeZone("G"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format( System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)   );
            Date date       = format.parse ( dateString );
            List<StockStats> list = stockStatsRepository.getAllByRecordDateTimeBefore(date);
            LOG.info("Records 3 days older : " + list.size());

            List<StockStatsArchieve> archieveList = stockstatsMapper.toArchieveList(list);
            LOG.info("Archieve 3 days older : " + archieveList.size());

            archieveList.forEach( record -> archieveRepository.save(record));
            deleteStockRecordsBeforeDate(date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteStockRecordsBeforeDate(Date date){
        try{

            stockStatsRepository.deleteAllByRecordDateTimeBefore(date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
