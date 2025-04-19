package com.facturacion.facturacionapp.services;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    // Obtener todos los clientes
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Obtener cliente por ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    // Crear nuevo cliente
    public Optional<Client> storeClient(Client client) {
        return Optional.of(clientRepository.save(client));
    }

    // Actualizar cliente
    public Optional<Client> updateClient(Long id, Client client) {
        boolean exists = clientRepository.existsById(id);
        if (exists) {
            client.setId(id);
            return Optional.of(clientRepository.save(client));
        } else {
            return Optional.empty();
        }
    }

    // Eliminar cliente
    public Optional<?> deleteClient(Long id) {
        boolean exists = clientRepository.existsById(id);
        if (exists) {
            clientRepository.deleteById(id);
            return Optional.of("Deleted");
        } else {
            return Optional.empty();
        }
    }
}
