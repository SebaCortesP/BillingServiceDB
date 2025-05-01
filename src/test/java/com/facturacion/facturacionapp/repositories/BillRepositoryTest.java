package com.facturacion.facturacionapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
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
public class BillRepositoryTest {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VeterinaryServiceRepository veterinaryServiceRepository;

    @Test
    void storeClientAndBillTest() {
        // Crear cliente
        Client cl1 = new Client();
        cl1.setName("Seba");
        cl1.setAddress("Calle 1");
        cl1.setPhone("954323452");
        Client savedClient = clientRepository.save(cl1);
        assertNotNull(savedClient.getId());

        // Crear servicio veterinario
        VeterinaryService service = new VeterinaryService();
        service.setName("Consulta General");
        service.setDescription("Revisión completa");
        service.setPrice(100); // Float directo
        VeterinaryService savedService = veterinaryServiceRepository.save(service);
        assertNotNull(savedService.getId());

        // Crear factura
        Bill bill = new Bill();
        bill.setCliente(savedClient);
        bill.setPayed(false);
        bill.setTotal((double) 0); // Inicializamos total en 0
        Bill savedBill = billRepository.save(bill);
        assertNotNull(savedBill.getId());

        // Crear detalle
        BillDetail detail = new BillDetail();
        detail.setBill(savedBill);
        detail.setService(savedService);
        detail.setQuantity(2);
        detail.setSubtotal(savedService.getPrice() * detail.getQuantity()); // subtotal = 100 * 2 = 200
        BillDetail savedDetail = billDetailRepository.save(detail);
        assertNotNull(savedDetail.getId());

        // Asociar el detalle a la factura
        List<BillDetail> detalles = new ArrayList<>();
        detalles.add(savedDetail);
        savedBill.setDetails(detalles);

        // Calcular total
        float total = 0;
        for (BillDetail d : detalles) {
            total += d.getSubtotal();
        }
        savedBill.setTotal((double) total);

        // Guardar factura actualizada
        Bill updatedBill = billRepository.save(savedBill);

        // Verificaciones
        assertEquals(savedClient.getId(), updatedBill.getCliente().getId());
        assertEquals(1, updatedBill.getDetails().size());
        assertEquals(200f, updatedBill.getTotal()); // Total esperado
        assertFalse(updatedBill.isPayed());
    }

}
