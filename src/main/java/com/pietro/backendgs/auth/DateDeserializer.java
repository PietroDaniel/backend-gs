package com.pietro.backendgs.auth;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@JsonComponent
@Slf4j
public class DateDeserializer extends JsonDeserializer<LocalDate> {

    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ISO_DATE
    );

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        String dateText = parser.getText();
        log.info("Trying to deserialize date: {}", dateText);
        
        if (dateText == null || dateText.isEmpty()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(dateText, formatter);
                log.info("Successfully parsed date with format {}: {}", formatter, date);
                return date;
            } catch (DateTimeParseException e) {
                // Continue trying other formats
                log.debug("Failed to parse date {} with format {}", dateText, formatter);
            }
        }
        
        log.error("Could not parse date: {}", dateText);
        throw new IOException("Invalid date format. Expected formats: DD/MM/YYYY, YYYY-MM-DD");
    }
} 