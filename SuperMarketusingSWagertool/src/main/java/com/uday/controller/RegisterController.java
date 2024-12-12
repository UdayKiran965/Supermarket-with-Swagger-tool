package com.uday.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uday.Service.CheckoutService;
import com.uday.dto.ItemDTO;
import com.uday.dto.BillDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private CheckoutService checkoutService;

    private List<ItemDTO> currentItems = new ArrayList<>(); // Store items temporarily
    private Double totalAmount = 0.0; // Store the total amount

    // Start a new bill (first in Swagger UI)
    @Operation(summary = "Start a new bill", description = "Initializes a new bill by clearing existing items and resetting the total amount.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bill started successfully")
    })
    @PostMapping("/start")
    public ResponseEntity<String> start() {
        currentItems.clear(); // Clear any previous items
        totalAmount = 0.0; // Reset the total amount
        return ResponseEntity.ok("Bill started. Ready to add items.");
    }

    // Add items to the bill (second in Swagger UI)
    @Operation(summary = "Add items to the bill", description = "Adds a list of items to the current bill and updates the total amount.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Items added successfully"),
        @ApiResponse(responseCode = "400", description = "Error adding items")
    })
    @PostMapping("/addItem")
    public ResponseEntity<String> addItem(@RequestBody List<ItemDTO> items) {
        try {
            currentItems.addAll(items); // Add the list of items to the current items
            for (ItemDTO item : items) {
                totalAmount += item.getPrice(); // Add the price of each item to the total
            }
            return ResponseEntity.ok("Items added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding items: " + e.getMessage());
        }
    }

    // Finalize the bill (last in Swagger UI)
    @Operation(summary = "Finalize the bill", description = "Applies offers to the items and calculates the final bill.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Checkout completed successfully",
            content = @Content(schema = @Schema(implementation = BillDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error during checkout",
            content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/finishCheckout")
    public ResponseEntity<Map<String, Object>> finishCheckout() {
        try {
            // Use the current items list directly for checkout
            double totalAmount = currentItems.stream().mapToDouble(ItemDTO::getPrice).sum();

            // Call the service method to apply offers and get checkout details
            Map<String, Object> checkoutDetails = checkoutService.applyOffersToCart(currentItems, totalAmount);

            // Prepare the response with all details
            Map<String, Object> response = Map.of(
                "items", checkoutDetails.get("items"),
                "totalAmount", totalAmount,
                "appliedOffers", checkoutDetails.get("appliedOffers"),
                "finalAmount", checkoutDetails.get("finalAmount")
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error during checkout: " + e.getMessage()));
        }
    }
}
