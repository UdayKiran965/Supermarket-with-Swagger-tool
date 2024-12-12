package com.uday.dto;

import lombok.Data;
import java.util.List;


    @Data
    public class BillDTO {
        private String items;
        private double totalAmount;
        private double finalAmount; // After applying offers and discounts
        private String appliedOffers; // Details of applied offers

        public BillDTO(String items, double totalAmount, double finalAmount, String appliedOffers) {
            this.items = items;
            this.totalAmount = totalAmount;
            this.finalAmount = finalAmount;
            this.appliedOffers = appliedOffers;
        }
		}

