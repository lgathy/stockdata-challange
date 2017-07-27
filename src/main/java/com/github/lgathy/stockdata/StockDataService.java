package com.github.lgathy.stockdata;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public interface StockDataService {
    
    List<DailyClosePrice> collectMonthlyClosePrices(Stream<String> dailyStockData);
    
    default void writeMonthlyClosePricesIntoFile(Path inputFile, Path outputFile) throws IOException {
        List<String> inputLines = Files.readAllLines(inputFile, Charsets.UTF_8);
        List<DailyClosePrice> monthlyClosePrices = collectMonthlyClosePrices(inputLines.stream().skip(1L));
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, Charsets.UTF_8, CREATE, WRITE)) {
            GSON.toJson(monthlyClosePrices, writer);
        }
    }
    
    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d-MMM-yy", Locale.US);
    
    JsonSerializer<LocalDate> LOCAL_DATE_JSON_SERIALIZER = (value, type, context) -> new JsonPrimitive(value.format(DATE_FORMAT));
    
    Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, LOCAL_DATE_JSON_SERIALIZER)
        .setPrettyPrinting()
        .create();
    
}
