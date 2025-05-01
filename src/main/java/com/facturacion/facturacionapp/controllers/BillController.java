package com.facturacion.facturacionapp.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.facturacion.facturacionapp.config.MessageResponse;
import com.facturacion.facturacionapp.dto.BillRequestDTO;
import com.facturacion.facturacionapp.dto.BillSummaryDTO;
import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.services.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("/bills")
    public ResponseEntity<CollectionModel<EntityModel<Bill>>> getAllBills() {
        List<Bill> bills = billService.getAllBills();

        // Convertir cada factura a EntityModel con enlaces
        List<EntityModel<Bill>> billResources = bills.stream()
                .map(bill -> EntityModel.of(bill,
                        linkTo(methodOn(BillController.class).getBillById(bill.getId())).withSelfRel(),
                        linkTo(methodOn(BillController.class).getAllBills()).withRel("all-bills")))
                .collect(Collectors.toList());

        // Crear CollectionModel con todos los enlaces
        CollectionModel<EntityModel<Bill>> collectionModel = CollectionModel.of(billResources,
                linkTo(methodOn(BillController.class).getAllBills()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // Obtener factura por ID con HATEOAS
    @GetMapping("/bills/{id}")
    public ResponseEntity<?> getBillById(@PathVariable("id") Long id) {
        Optional<Bill> bill = billService.getBillById(id);

        if (bill.isPresent()) {
            Bill foundBill = bill.get();

            // Crear el EntityModel con enlaces para la factura específica
            EntityModel<Bill> billResource = EntityModel.of(foundBill,
                    linkTo(methodOn(BillController.class).getBillById(foundBill.getId())).withSelfRel(),
                    linkTo(methodOn(BillController.class).getAllBills()).withRel("all-bills"));

            return ResponseEntity.ok(billResource);
        } else {
            // Retornar 404 si no se encuentra la factura
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill with ID " + id + " not found");
        }
    }


    @PostMapping("/bills/store")
    public ResponseEntity<?> createBill(@RequestBody @Valid BillRequestDTO dto) {
        try {
            Optional<Bill> created = billService.createBillWithDetails(dto);

            if (created.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cliente no encontrado o IDs de servicios inválidos.");
            }

            Bill saved = created.get();

            EntityModel<Bill> billResource = EntityModel.of(saved,
                    linkTo(methodOn(BillController.class).getBillById(saved.getId())).withSelfRel(),
                    linkTo(methodOn(BillController.class).getAllBills()).withRel("all-bills"),
                    linkTo(methodOn(BillController.class).getBillSummary(saved.getId())).withRel("summary"));

            return ResponseEntity
                    .created(linkTo(methodOn(BillController.class).getBillById(saved.getId())).toUri())
                    .body(billResource);

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

            BillSummaryDTO summary = summaryOpt.get();

            EntityModel<BillSummaryDTO> summaryResource = EntityModel.of(summary,
                    linkTo(methodOn(BillController.class).getBillById(id)).withRel("bill"),
                    linkTo(methodOn(BillController.class).getAllBills()).withRel("all-bills"),
                    linkTo(methodOn(BillController.class).payBill(id)).withRel("pay"));

            return ResponseEntity.ok(summaryResource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el resumen de la factura: " + e.getMessage());
        }
    }

    @PutMapping("/bills/{id}/pay")
    public ResponseEntity<EntityModel<MessageResponse>> payBill(@PathVariable("id") Long id) {
        Optional<String> response = billService.payBill(id);
        System.out.println("Respuesta del servicio payBill: " + response);
        MessageResponse message = new MessageResponse(response.get());

        EntityModel<MessageResponse> payResource = EntityModel.of(message);
        payResource.add(linkTo(methodOn(BillController.class).getAllBills()).withRel("all-bills"));

        if (response.get().contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payResource);
        }

        if (response.get().contains("ya se encuentra pagada")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(payResource);
        }
        payResource.add(linkTo(methodOn(BillController.class).getBillById(id)).withRel("bill"));
        return ResponseEntity.ok(payResource);
    }

    

}
