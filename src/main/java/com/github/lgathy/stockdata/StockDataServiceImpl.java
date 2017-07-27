package com.github.lgathy.stockdata;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static java.util.stream.Collector.Characteristics.*;

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
        return Collector.of(
            MonthyClosePrices::new,
            MonthyClosePrices::accumulate,
            MonthyClosePrices::combine,
            MonthyClosePrices::copyValuesSorted,
            CONCURRENT, UNORDERED);
    }
    
    @Value
    private static class MonthyClosePrices {
        
        Map<YearMonth, DailyClosePrice> latestsPerMonth = new ConcurrentHashMap<>();
        
        public MonthyClosePrices accumulate(DailyClosePrice newElement) {
            YearMonth key = YearMonth.from(newElement.getDate());
            latestsPerMonth.compute(key, (ym, current) -> DATE_ORDER.max(current, newElement));
            return this;
        }
        
        public MonthyClosePrices combine(MonthyClosePrices other) {
            if (other.size() > this.size()) {
                // let's not do the hard work: it's faster to add the smaller map to the larger
                return other.combine(this);
            }
            // let's make a copy of the our inner state first
            MonthyClosePrices result = this.copy();
            // we just need to treat other's values as new elements:
            other.latestsPerMonth.values().forEach(result::accumulate);
            return result;
        }
        
        private MonthyClosePrices copy() {
            MonthyClosePrices copy = new MonthyClosePrices();
            copy.latestsPerMonth.putAll(this.latestsPerMonth);
            return copy;
        }
        
        private int size() { return latestsPerMonth.size(); }
    
        public ImmutableList<DailyClosePrice> copyValues() {
            return ImmutableList.copyOf(latestsPerMonth.values());
        }
    
        public ImmutableList<DailyClosePrice> copyValuesSorted() {
            return ImmutableList.sortedCopyOf(DATE_ORDER, latestsPerMonth.values());
        }
    }
    
    static final Splitter CSV_SPLITTER = Splitter.on(',');
    
    static final int DATE_POS = 0;
    
    static final int CLOSE_PRICE_POS = 4;
    
    static final Ordering<DailyClosePrice> DATE_ORDER = Ordering.from(Comparator.comparing(DailyClosePrice::getDate)).nullsFirst();
    
}
