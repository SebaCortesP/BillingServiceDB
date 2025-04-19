package com.facturacion.facturacionapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.facturacion.facturacionapp.models.BillDetail;

public interface BillDetailRepository extends JpaRepository <BillDetail, Long>{

}
