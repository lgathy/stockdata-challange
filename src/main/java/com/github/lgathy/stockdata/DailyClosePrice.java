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
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        DailyClosePrice that = (DailyClosePrice) o;
        
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return close != null ? close.compareTo(that.close) == 0 : that.close == null;
    }
    
}
