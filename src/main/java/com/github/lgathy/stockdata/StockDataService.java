package com.github.lgathy.stockdata;

import java.util.*;
import java.util.stream.*;

public interface StockDataService {
    
    List<DailyClosePrice> collectMonthlyClosePrices(Stream<String> dailyStockData);
    
}
