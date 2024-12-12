package com.uday.dto;

import lombok.Data;

@Data
    public class ItemDTO {
        private String name;
        private Double price;

        public ItemDTO(String name, Double price) {
            this.name = name;
            this.price = price;
        }
    }

