package com.github.lgathy.stockdata;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Runner {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("Usage: <command> 'inputFilePath' 'outputFilePath'");
        }
        Path inputFilePath = Paths.get(args[0]);
        Path outputFilePath = Paths.get(args[1]);
        try {
            new StockDataServiceImpl().writeMonthlyClosePricesIntoFile(inputFilePath, outputFilePath);
            log.info("Output file succesfully written: {}", outputFilePath);
        } catch (RuntimeException | IOException e) {
            log.error("Operation failed: " + e.getMessage(), e);
        }
    
    }
}
