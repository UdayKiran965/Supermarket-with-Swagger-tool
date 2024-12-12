package com.uday.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uday.Entity.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
	Offer findByItemName(String name);

}
