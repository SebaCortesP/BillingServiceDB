package com.facturacion.facturacionapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.models.BillDetail;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.models.VeterinaryService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void createClientTest() {
        // Crear cliente
        Client client = new Client();
        client.setName("Ana");
        client.setAddress("Av. Siempre Viva 123");
        client.setPhone("987654321");
        Client savedClient = clientRepository.save(client);

        // Verificar que fue guardado correctamente
        assertNotNull(savedClient.getId());
        assertEquals("Ana", savedClient.getName());
        assertEquals("Av. Siempre Viva 123", savedClient.getAddress());
        assertEquals("987654321", savedClient.getPhone());
    }

    @Test
    void updateClientTest() {
        // Crear cliente
        Client client = new Client();
        client.setName("Luis");
        client.setAddress("Calle Antigua");
        client.setPhone("111222333");
        Client savedClient = clientRepository.save(client);

        // Actualizar datos
        savedClient.setAddress("Calle Nueva 456");
        savedClient.setPhone("999888777");
        Client updatedClient = clientRepository.save(savedClient);

        // Verificar actualización
        assertEquals(savedClient.getId(), updatedClient.getId());
        assertEquals("Calle Nueva 456", updatedClient.getAddress());
        assertEquals("999888777", updatedClient.getPhone());
    }

}
