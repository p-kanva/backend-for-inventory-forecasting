package com.example.inventory.llm;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MockLLMService implements LLMService {

    private static final Pattern QTY = Pattern.compile("(\\d{1,9})\\s*(units|pcs|pieces)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern CATEGORY = Pattern.compile("(category|cat)\\s*[:,]?\\s*([A-Za-z ]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern LOCATION = Pattern.compile("(to|at|in)\\s+the\\s+([A-Za-z ]+)\\s*(warehouse|store)?", Pattern.CASE_INSENSITIVE);


    @Override
    public ParsedData parse(String text) {
        ParsedData out = new ParsedData();
        Matcher m = QTY.matcher(text);
        if (m.find()) out.quantity = Integer.parseInt(m.group(1));
        m = CATEGORY.matcher(text);
        if (m.find()) out.category = m.group(2).trim();

        String lower = text.toLowerCase(Locale.ROOT);
        int soldIdx = lower.indexOf("sold");
        if (soldIdx >= 0) {
            int start = soldIdx + "sold".length();
            int onIdx = lower.indexOf(" on ");
            int commaIdx = lower.indexOf(",", start);
            int end = (onIdx > 0 ? onIdx : (commaIdx > 0 ? commaIdx : text.length()));
            out.product_name = text.substring(start, end)
                    .replaceAll("\\d", "")
                    .replaceAll("units|pcs|pieces", "")
                    .trim();
            if (out.product_name.startsWith("of")) {
                out.product_name = out.product_name.substring(2).trim();
            }
        }

        out.sale_date = tryParseDate(text);

        m = LOCATION.matcher(text);
        if (m.find()) out.location = (m.group(2) + " warehouse").trim();

        return out;
    }

    private String tryParseDate(String text) {
        String[] months = {"january","february","march","april","may","june","july","august","september","october","november","december"};
        String lower = text.toLowerCase(Locale.ROOT);
        for (int i = 0; i < months.length; i++) {
            String m = months[i];
            int idx = lower.indexOf(m);
            if (idx >= 0) {
                String rest = lower.substring(idx + m.length()).replaceAll("[^0-9 ]"," ").trim();
                String[] parts = rest.split("\s+");
                if (parts.length >= 1) {
                    try {
                        int day = Integer.parseInt(parts[0]);
                        int year = LocalDate.now().getYear();
                        if (parts.length >= 2 && parts[1].length() == 4) year = Integer.parseInt(parts[1]);
                        LocalDate d = LocalDate.of(year, Month.of(i+1), day);
                        return d.toString();
                    } catch (Exception ignored) {}
                }
            }
        }
        return null;
    }
}
