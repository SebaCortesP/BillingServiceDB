package com.facturacion.facturacionapp.controllers;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Long id) {
        Optional<Client> eventType = clientService.getClientById(id);

        if (eventType.isPresent()) {
            return ResponseEntity.ok(eventType.get());
        } else {
            return ResponseEntity.status(404).body("Event Type with ID " + id + " not found");
        }
    }

    @PostMapping("/clients/store")
    public ResponseEntity<?> storeClient(@RequestBody @Valid Client client, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<Client> saved = clientService.storeClient(client);
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    @PutMapping("/clients/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") Long id, @RequestBody @Valid Client client, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<Client> updated = clientService.updateClient(id, client);
            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.status(404).body("Client with ID " + id + " not found");
            }
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    @DeleteMapping("/clients/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) {
        Optional<?> result = clientService.deleteClient(id);
        if (result.isPresent()) {
            return ResponseEntity.status(201).body("Client with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Client with ID " + id + " not found");
        }
    }
}
