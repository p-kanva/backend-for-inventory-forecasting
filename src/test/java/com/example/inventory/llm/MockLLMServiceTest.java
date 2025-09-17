package com.example.inventory.llm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MockLLMServiceTest {
    @Test
    void testParse() {
        MockLLMService svc = new MockLLMService();
        String text = "Sold 45 units of blue office chairs, Furniture category, on July 15th to the West warehouse.";
        ParsedData out = svc.parse(text);
        assertEquals(45, out.quantity);
        assertNotNull(out.sale_date);
        assertEquals("Furniture", out.category);
        assertTrue(out.product_name.toLowerCase().contains("blue office chairs"));
        assertTrue(out.location.toLowerCase().contains("west"));
    }
}
