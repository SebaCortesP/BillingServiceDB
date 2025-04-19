package com.facturacion.facturacionapp.controllers;
import com.facturacion.facturacionapp.models.VeterinaryService;
import com.facturacion.facturacionapp.services.VeterinaryServiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/veterinary_service")
public class VeterinaryServiceController {
     private final VeterinaryServiceService veterinaryServiceService;

    public VeterinaryServiceController(VeterinaryServiceService veterinaryServiceService) {
        this.veterinaryServiceService = veterinaryServiceService;
    }

    // Obtener todos los servicios
    @GetMapping
    public List<VeterinaryService> getAllVeterinaryServices() {
        return veterinaryServiceService.getAllVeterinaryServices();
    }

    // Obtener un servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVeterinaryServiceById(@PathVariable("id") Long id) {
        Optional<VeterinaryService> service = veterinaryServiceService.getVeterinaryServiceById(id);

        if (service.isPresent()) {
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veterinary service with ID " + id + " not found");
        }
    }

    // Crear un nuevo servicio
    @PostMapping("/store")
    public ResponseEntity<?> storeVeterinaryService(@RequestBody @Valid VeterinaryService service, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<VeterinaryService> saved = veterinaryServiceService.storeVeterinaryService(service);
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    // Actualizar un servicio existente
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVeterinaryService(@PathVariable("id") Long id, @RequestBody @Valid VeterinaryService service, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<VeterinaryService> updated = veterinaryServiceService.updateVeterinaryService(id, service);
            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veterinary service with ID " + id + " not found");
            }
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    // Eliminar un servicio
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVeterinaryService(@PathVariable("id") Long id) {
        Optional<?> result = veterinaryServiceService.deleteVeterinaryService(id);
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("Veterinary service with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veterinary service with ID " + id + " not found");
        }
    }
}
