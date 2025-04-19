package com.facturacion.facturacionapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.facturacion.facturacionapp.models.Client;

public interface ClientRepository extends JpaRepository <Client, Long>{

}
