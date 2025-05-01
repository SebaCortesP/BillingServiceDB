package com.facturacion.facturacionapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.facturacion.facturacionapp.config.SecurityConfig;
import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.services.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BillController.class)
@WithMockUser
@Import(SecurityConfig.class)
public class BillControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BillService billServiceMock;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getBillByIdTest() throws Exception {
        // Arrange
        Client client = new Client(1L, "Juan", "Calle Falsa 123", "123456789");

        Bill bill = new Bill();
        bill.setId(1L);
        bill.setPayed(false);
        bill.setTotal(100.0);
        bill.setCliente(client);

        when(billServiceMock.getBillById(1L)).thenReturn(Optional.of(bill));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/bills/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.is(100.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payed", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cliente.name", Matchers.is("Juan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.all-bills.href").exists());
    }

    @Test
    public void payBill_SuccessTest() throws Exception {
        // Arrange
        Long billId = 1L;
        String message = "Factura pagada correctamente";
        when(billServiceMock.payBill(billId)).thenReturn(Optional.of(message));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/bills/" + billId + "/pay"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.all-bills.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.bill.href").exists());
    }

}
