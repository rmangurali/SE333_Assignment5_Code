package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based")
    void returnNullWhenOrderNull() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);
        PurchaseSummary purchaseSummary = bn.getPriceForCart(null);

        assertNull(purchaseSummary);
    }

    @Test
    @DisplayName("specification-based")
    void returnOnePrice() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book = new Book("123", 10, 5);

        when(bookDatabase.findByISBN("123")).thenReturn(book);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 5);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(50, purchaseSummary.getTotalPrice());
    }

    @Test
    @DisplayName("specification-based")
    void returnMultiplePrices() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book1 = new Book("123", 10, 5);
        Book book2 = new Book("456", 20, 15);

        when(bookDatabase.findByISBN("123")).thenReturn(book1);
        when(bookDatabase.findByISBN("456")).thenReturn(book2);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 5);
        order.put("456", 10);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(250, purchaseSummary.getTotalPrice());
    }

    @Test
    @DisplayName("structural-based")
    void zeroQuantityOrder() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book = new Book("123", 10, 3);

        when(bookDatabase.findByISBN("123")).thenReturn(book);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 0);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(0, purchaseSummary.getTotalPrice());
        assertTrue(purchaseSummary.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    void noQuantityOrdered() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Map<String, Integer> order = new HashMap<>();

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertNotNull(purchaseSummary);
        assertEquals(0, purchaseSummary.getTotalPrice());
        assertTrue(purchaseSummary.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    void bookQuantityLessThanRequested() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book = new Book("123", 10, 3);

        when(bookDatabase.findByISBN("123")).thenReturn(book);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 5);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(30, purchaseSummary.getTotalPrice());
        assertEquals(2, purchaseSummary.getUnavailable().get(book));
    }

    @Test
    @DisplayName("structural-based")
    void bookQuantityMatchesRequested() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book = new Book("123", 10, 3);

        when(bookDatabase.findByISBN("123")).thenReturn(book);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 3);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(30, purchaseSummary.getTotalPrice());
        assertTrue(purchaseSummary.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    void mixedSufficientAndInsufficientQuantity() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(bookDatabase, process);

        Book book1 = new Book("123", 10, 3);
        Book book2 = new Book("456", 12, 10);

        when(bookDatabase.findByISBN("123")).thenReturn(book1);
        when(bookDatabase.findByISBN("456")).thenReturn(book2);

        Map<String, Integer> order = new HashMap<>();
        order.put("123", 5);
        order.put("456", 8);

        PurchaseSummary purchaseSummary = bn.getPriceForCart(order);

        assertEquals(126, purchaseSummary.getTotalPrice());
        assertEquals(2, purchaseSummary.getUnavailable().get(book1));
    }

    @Test
    @DisplayName("structural-based")
    void bookClassCoverage() {
        Book book = new Book("123", 10, 1);
        Book bookCopy = new Book("123", 20, 2);
        Book bookDiff = new Book("456", 10, 1);

        assertTrue(book.equals(book));
        assertFalse(book.equals(null));
        assertFalse(book.equals("string"));
        assertTrue(book.equals(bookCopy));
        assertFalse(book.equals(bookDiff));
        assertEquals(book.hashCode(), bookCopy.hashCode());
    }

}