package com.uday.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.OFFER_TYPE;
import com.uday.Entity.Offer;
import com.uday.Repository.OfferRepository;
import com.uday.dto.ItemDTO;

@Service
public class CheckoutService {

    @Autowired
    private OfferRepository offerRepository;

    // Apply offers to items in the cart and return detailed information about applied offers
    public Map<String, Object> applyOffersToCart(List<ItemDTO> items, double totalAmount) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> itemDetails = new ArrayList<>();

        double discountedAmount = totalAmount; // Start with the original total amount
        StringBuilder appliedOffers = new StringBuilder(); // To track applied offers

        // Apply offers to each item
        for (ItemDTO item : items) {
            Offer offer = offerRepository.findByItemName(item.getName()); // Fetch the offer for the item
            double itemDiscount = 0;

            if (offer != null) {
                // Apply appropriate offer based on the type
                if (offer.getOfferType() == OFFER_TYPE.DISCOUNT) {
                    itemDiscount = (item.getPrice() * offer.getValue()) / 100;
                    if (appliedOffers.length() > 0) {
                        appliedOffers.append("\n");
                    }
                    appliedOffers.append("Discount on ").append(item.getName()).append(": ").append(offer.getValue()).append("% off");
                } else if (offer.getOfferType() == OFFER_TYPE.BUY_ONE_GET_ONE_FREE) {
                    itemDiscount = item.getPrice(); // Full price discount for Buy 1 Get 1 Free offer
                    if (appliedOffers.length() > 0) {
                        appliedOffers.append("\n");
                    }
                    appliedOffers.append("Buy 1 Get 1 Free on ").append(item.getName());
                }
            }

            // Deduct the discount for the item
            discountedAmount -= itemDiscount;

            // Add the item details to the response
            Map<String, Object> itemInfo = new HashMap<>();
            itemInfo.put("name", item.getName());
            itemInfo.put("price", item.getPrice());

            itemDetails.add(itemInfo);
        }

        // Apply an additional 10% discount on total purchase
        double totalDiscount = discountedAmount * 0.10; // 10% discount
        discountedAmount -= totalDiscount;

        // Add the total purchase discount to the applied offers
        if (appliedOffers.length() > 0) {
            appliedOffers.append("\n");
        }
        appliedOffers.append("Additional 10% off on total purchase");

        // Add final results to the response
        result.put("items", itemDetails);
        result.put("totalAmount", totalAmount); // Original total
        result.put("finalAmount", discountedAmount); // Final amount after all discounts
        result.put("appliedOffers", appliedOffers.toString()); // Detailed offers

        return result;
    }
}
