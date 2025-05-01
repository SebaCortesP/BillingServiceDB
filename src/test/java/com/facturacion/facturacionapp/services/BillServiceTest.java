package com.facturacion.facturacionapp.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.facturacion.facturacionapp.dto.BillRequestDTO;
import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.models.VeterinaryService;
import com.facturacion.facturacionapp.repositories.BillRepository;
import com.facturacion.facturacionapp.repositories.ClientRepository;
import com.facturacion.facturacionapp.repositories.VeterinaryServiceRepository;


@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
 @InjectMocks
    private BillService billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private VeterinaryServiceRepository veterinaryServiceRepository;

    @Test
    public void createBillWithDetailsTest() {
        // Arrange
        Long clientId = 1L;
        Long serviceId = 10L;

        Client client = new Client();
        client.setId(clientId);
        client.setName("Pedro");

        VeterinaryService service = new VeterinaryService();
        service.setId(serviceId);
        service.setName("Consulta");
        service.setPrice(100);

        // Simular resultado de findById del cliente
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Simular resultado de findById del servicio
        when(veterinaryServiceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        // Simular que guardamos la boleta
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // DTO de entrada
        BillRequestDTO.ServiceItem item = new BillRequestDTO.ServiceItem();
        item.setServiceId(serviceId);
        item.setQuantity(2);

        BillRequestDTO dto = new BillRequestDTO();
        dto.setClientId(clientId);
        dto.setServices(List.of(item));

        // Act
        Optional<Bill> result = billService.createBillWithDetails(dto);

        // Assert
        assertTrue(result.isPresent());
        Bill bill = result.get();
        assertEquals(clientId, bill.getCliente().getId());
        assertEquals(200.0, bill.getTotal());
        assertEquals(1, bill.getDetails().size());
        assertEquals(2, bill.getDetails().get(0).getQuantity());
        assertEquals(200, bill.getDetails().get(0).getSubtotal());
    }
}
