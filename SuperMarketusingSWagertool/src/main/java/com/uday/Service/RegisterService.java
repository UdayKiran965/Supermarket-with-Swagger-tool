package com.uday.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.OFFER_TYPE;
import com.uday.Entity.Bill;
import com.uday.Entity.Offer;
import com.uday.Exception.CheckoutInProgressException;
import com.uday.Exception.NoCheckoutInProgressException;
import com.uday.Repository.BillRepository;
import com.uday.Repository.ItemRepository;
import com.uday.Repository.OfferRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegisterService {
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;
    private final BillRepository billRepository;

    private boolean checkoutInProgress = false;
    private Map<String, Integer> scannedItems = new HashMap<>(); // Store items with their quantity


    @Autowired
    public RegisterService(ItemRepository itemRepository, OfferRepository offerRepository, BillRepository billRepository) {
        this.itemRepository = itemRepository;
        this.offerRepository = offerRepository;
        this.billRepository = billRepository;
    }

    public void startCheckout() {
        if (checkoutInProgress) {
            throw new RuntimeException("Checkout already in progress.");
        }
        checkoutInProgress = true;
        scannedItems.clear(); // Clear scanned items when starting a new checkout
    }

    public void addItem(String itemName) {
        if (!checkoutInProgress) {
            throw new RuntimeException("Checkout not started.");
        }
        scannedItems.put(itemName, scannedItems.getOrDefault(itemName, 0) + 1);
    }

    public Bill finishCheckout() {
        if (!checkoutInProgress) {
            throw new RuntimeException("No checkout in progress.");
        }

        Map<String, Double> itemPrices = fetchItemPrices();  // Fetch item prices
        List<Offer> offers = offerRepository.findAll(); // Get all offers from the database

        double total = calculateTotal(scannedItems, itemPrices, offers);
        Bill bill = new Bill();
        bill.setItems(String.join(", ", scannedItems.keySet())); // Set the scanned item names
        bill.setTotalAmount(total);
        bill.setTimestamp(LocalDateTime.now());
        billRepository.save(bill);

        checkoutInProgress = false;
        return bill;
    }

    private Map<String, Double> fetchItemPrices() {
        Map<String, Double> itemPrices = new HashMap<>();
        itemRepository.findAll().forEach(item -> {
            itemPrices.put(item.getName(), item.getPrice()); // Assuming items have 'name' and 'price' attributes
        });
        return itemPrices;
    }

    public double calculateTotal(Map<String, Integer> scannedItems, Map<String, Double> itemPrices, List<Offer> offers) {
        double total = 0.0;
        Map<String, Offer> offerMap = new HashMap<>();
        
        for (Offer offer : offers) {
            offerMap.put(offer.getItemName(), offer);
        }

        for (Map.Entry<String, Integer> entry : scannedItems.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = itemPrices.getOrDefault(itemName, 0.0);

            if (offerMap.containsKey(itemName)) {
                Offer offer = offerMap.get(itemName);

                if (offer.getOfferType() == OFFER_TYPE.BUY_ONE_GET_ONE_FREE) {
                    int eligibleSets = quantity / offer.getN();
                    int remainingItems = quantity % offer.getN();
                    double discountedPrice = eligibleSets * offer.getN() * price * (1 - offer.getValue() / 100);
                    double regularPrice = remainingItems * price;
                    total += discountedPrice + regularPrice;
                } else if (offer.getOfferType() == OFFER_TYPE.BUY_ONE_GET_ONE_FREE) {
                    int eligibleSets = quantity / offer.getN();
                    int remainingItems = quantity % offer.getN();
                    double offerPrice = eligibleSets * offer.getValue();
                    double regularPrice = remainingItems * price;
                    total += offerPrice + regularPrice;
                }
            } else {
                total += quantity * price;
            }
        }

        return total;
    }


    public void scanItem(String itemName) {
        scannedItems.put(itemName, scannedItems.getOrDefault(itemName, 0) + 1);
        System.out.println("Scanned Items after scan: " + scannedItems); // Log here
    }
}
