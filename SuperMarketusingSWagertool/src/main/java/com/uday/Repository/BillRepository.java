package com.uday.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uday.Entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
