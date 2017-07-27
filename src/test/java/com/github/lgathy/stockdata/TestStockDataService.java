package com.github.lgathy.stockdata;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class TestStockDataService {
    
    private StockDataService service;
    
    @Before
    public void setup() {
        service = input -> ImmutableList.of(); // TODO temp dummy implementation
    }
    
    @Test
    public void processTestdata() throws IOException {
        List<String> inputLines = Resources.readLines(getClass().getResource("/testdata.csv"), Charsets.UTF_8);
        List<DailyClosePrice> monthlyClosePrices = service.collectMonthlyClosePrices(inputLines.stream());
        assertNotNull("monthlyClosePrices", monthlyClosePrices);
    }
    
}
