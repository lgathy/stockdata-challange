package com.github.lgathy.stockdata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class DailyClosePrice {
    
    private LocalDate date;
    
    private BigDecimal close;
    
}
