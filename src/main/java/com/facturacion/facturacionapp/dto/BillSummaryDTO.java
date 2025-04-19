package com.facturacion.facturacionapp.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillSummaryDTO {
    private Long billId;
    private String clientName;

    private List<ServiceSummary> services;

    private Double subtotal;
    private Double iva;
    private Double totalBruto;

    @Getter
    @Setter
    public static class ServiceSummary {
        private String serviceName;
        private Double unitPrice;
        private Integer quantity;
        private Double subtotal;
    }
}
