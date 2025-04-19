package com.facturacion.facturacionapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.facturacion.facturacionapp.models.Bill;

public interface BillRepository extends JpaRepository <Bill, Long>{

}
