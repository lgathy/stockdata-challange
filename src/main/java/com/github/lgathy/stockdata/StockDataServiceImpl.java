package com.github.lgathy.stockdata;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

public class StockDataServiceImpl implements StockDataService {
    
    public List<DailyClosePrice> collectMonthlyClosePrices(Stream<String> dailyStockData) {
        return dailyStockData.map(this::parseLine).collect(newMonthlyClosePricesCollector());
    }
    
    public DailyClosePrice parseLine(String input) {
        List<String> columns = CSV_SPLITTER.splitToList(input);
        return new DailyClosePrice()
            .setDate(parseDate(columns.get(DATE_POS)))
            .setClose(parsePrice(columns.get(CLOSE_PRICE_POS)));
    }
    
    public LocalDate parseDate(String input) {
        return LocalDate.parse(input, DATE_FORMAT);
    }
    
    public BigDecimal parsePrice(String input) {
        return new BigDecimal(input);
    }
    
    public Collector<DailyClosePrice, ?, ? extends List<DailyClosePrice>> newMonthlyClosePricesCollector() {
        return ImmutableList.toImmutableList(); // TODO implement
    }
    
    static final Splitter CSV_SPLITTER = Splitter.on(',');
    
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d-MMM-yy", Locale.US);
    
    static final int DATE_POS = 0;
    
    static final int CLOSE_PRICE_POS = 4;
    
}
