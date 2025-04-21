package com.facturacion.facturacionapp.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.facturacion.facturacionapp.dto.BillRequestDTO;
import com.facturacion.facturacionapp.dto.BillSummaryDTO;
import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.services.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("/bills")
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<?> getBillById(@PathVariable("id") Long id) {
        Optional<Bill> bill = billService.getBillById(id);

        if (bill.isPresent()) {
            return ResponseEntity.ok(bill.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill with ID " + id + " not found");
        }
    }

    @GetMapping("/bills/total/{id}")
    public ResponseEntity<?> calculateTotal(@PathVariable("id") Long id) {
        Map<String, Object> totalData = billService.calculateTotal(id);
        if (totalData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill with ID " + id + " not found");
        }
        return ResponseEntity.ok(totalData);
    }

    @PostMapping("/bills/store")
    public ResponseEntity<?> createBill(@RequestBody @Valid BillRequestDTO dto) {
        try {
            Optional<Bill> created = billService.createBillWithDetails(dto);
            if (created.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cliente no encontrado o IDs de servicios inválidos.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(created.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la factura: " + e.getMessage());
        }
    }

    @GetMapping("/bills/{id}/summary")
    public ResponseEntity<?> getBillSummary(@PathVariable Long id) {
        try {
            Optional<BillSummaryDTO> summaryOpt = billService.getBillSummary(id);
            if (summaryOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Factura no encontrada con ID: " + id);
            }
            return ResponseEntity.ok(summaryOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el resumen de la factura: " + e.getMessage());
        }
    }

    @PutMapping("/bills/{id}/pay")
    public ResponseEntity<String> payBill(@PathVariable Long id) {
        Optional<String> response = billService.payBill(id);

        return response
            .map(msg -> ResponseEntity.ok(msg))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Factura no encontrada."));
    }

}
