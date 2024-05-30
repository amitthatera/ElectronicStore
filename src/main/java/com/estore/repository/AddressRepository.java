package com.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
