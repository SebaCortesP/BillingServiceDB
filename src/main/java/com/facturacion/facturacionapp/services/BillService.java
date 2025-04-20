package com.facturacion.facturacionapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.facturacion.facturacionapp.dto.BillRequestDTO;
import com.facturacion.facturacionapp.dto.BillSummaryDTO;
import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;
import com.facturacion.facturacionapp.models.*;
import com.facturacion.facturacionapp.repositories.BillRepository;
import com.facturacion.facturacionapp.repositories.ClientRepository;
import com.facturacion.facturacionapp.repositories.VeterinaryServiceRepository;

import java.util.*;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ClientRepository clientRepository;
    private final VeterinaryServiceRepository veterinaryServiceRepository;

    @Autowired
    public BillService(BillRepository billRepository, ClientRepository clientRepository,
            VeterinaryServiceRepository veterinaryServiceRepository) {
        this.billRepository = billRepository;
        this.clientRepository = clientRepository;
        this.veterinaryServiceRepository = veterinaryServiceRepository;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public Map<String, Object> calculateTotal(Long billId) {
        Optional<Bill> billOpt = billRepository.findById(billId);

        if (billOpt.isEmpty()) {
            return null;
        }

        Bill bill = billOpt.get();
        List<BillDetail> details = bill.getDetails();

        int neto = details.stream()
                .mapToInt(detail -> detail.getService().getPrice())
                .sum();

        double iva = neto * 0.19;
        double total = neto + iva;

        Map<String, Object> response = new HashMap<>();
        response.put("bill_id", billId);
        response.put("neto", neto);
        response.put("iva", iva);
        response.put("total", total);

        return response;
    }

    public Optional<Bill> createBillWithDetails(BillRequestDTO dto) {
        Optional<Client> clienteOpt = clientRepository.findById(dto.getClientId());
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }

        Bill bill = new Bill();
        bill.setCliente(clienteOpt.get());
        bill.setPayed(false);
        bill.setTotal(0.0); // Inicializa el total

        double total = 0.0;

        for (BillRequestDTO.ServiceItem item : dto.getServices()) {
            Optional<VeterinaryService> serviceOpt = veterinaryServiceRepository.findById(item.getServiceId());
            if (serviceOpt.isPresent()) {
                VeterinaryService service = serviceOpt.get();

                // Calcular subtotal
                int lineSubtotal = item.getQuantity() * service.getPrice();
                total += lineSubtotal;

                // Crear detalle de factura
                BillDetail detail = new BillDetail();
                detail.setService(service);
                detail.setQuantity(item.getQuantity());
                detail.setSubtotal(lineSubtotal); // Calculado, no viene del DTO

                bill.addDetail(detail);
            } else {
                System.out.println("Servicio no encontrado: ID " + item.getServiceId());
            }
        }

        bill.setTotal(total);
        billRepository.save(bill);
        return Optional.of(bill);
    }

    public Optional<BillSummaryDTO> getBillSummary(Long billId) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        if (billOpt.isEmpty())
            return Optional.empty();

        Bill bill = billOpt.get();
        BillSummaryDTO summary = new BillSummaryDTO();
        summary.setBillId(bill.getId());
        summary.setClientName(bill.getCliente().getName());

        double[] subtotal = { 0.0 };

        List<BillSummaryDTO.ServiceSummary> summaries = bill.getDetails().stream().map(detail -> {
            BillSummaryDTO.ServiceSummary s = new BillSummaryDTO.ServiceSummary();
            s.setServiceName(detail.getService().getName());
            s.setUnitPrice((double) detail.getService().getPrice());
            s.setQuantity(detail.getQuantity());
            double lineSubtotal = detail.getQuantity() * detail.getService().getPrice();
            s.setSubtotal(lineSubtotal);
            subtotal[0] += lineSubtotal;
            return s;
        }).toList();

        double iva = subtotal[0] * 0.19;
        double totalBruto = subtotal[0] + iva;

        summary.setServices(summaries);
        summary.setSubtotal(subtotal[0]);
        summary.setIva(iva);
        summary.setTotalBruto(totalBruto);

        return Optional.of(summary);
    }

}
