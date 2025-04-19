package com.facturacion.facturacionapp.services;

import com.facturacion.facturacionapp.models.VeterinaryService;
import com.facturacion.facturacionapp.repositories.VeterinaryServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinaryServiceService {
    private final VeterinaryServiceRepository veterinaryServiceRepository;

    public VeterinaryServiceService(VeterinaryServiceRepository veterinaryServiceRepository) {
        this.veterinaryServiceRepository = veterinaryServiceRepository;
    }

    // Obtener todos los servicios veterinarios
    public List<VeterinaryService> getAllVeterinaryServices() {
        return veterinaryServiceRepository.findAll();
    }

    // Obtener un servicio por ID
    public Optional<VeterinaryService> getVeterinaryServiceById(Long id) {
        return veterinaryServiceRepository.findById(id);
    }

    // Crear un nuevo servicio
    public Optional<VeterinaryService> storeVeterinaryService(VeterinaryService service) {
        return Optional.of(veterinaryServiceRepository.save(service));
    }

    // Actualizar un servicio
    public Optional<VeterinaryService> updateVeterinaryService(Long id, VeterinaryService service) {
        if (veterinaryServiceRepository.existsById(id)) {
            service.setId(id);
            return Optional.of(veterinaryServiceRepository.save(service));
        } else {
            return Optional.empty();
        }
    }

    // Eliminar un servicio
    public Optional<?> deleteVeterinaryService(Long id) {
        if (veterinaryServiceRepository.existsById(id)) {
            veterinaryServiceRepository.deleteById(id);
            return Optional.of("Deleted");
        } else {
            return Optional.empty();
        }
    }
}
