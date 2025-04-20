    package com.facturacion.facturacionapp.dto;

    import java.util.List;

    import com.facturacion.facturacionapp.models.VeterinaryService;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @AllArgsConstructor
    @Getter
    @Setter
    public class BillRequestDTO {
        private Long clientId;
        private List<ServiceItem> services;


        public BillRequestDTO() {

        }

        @Getter @Setter
        public static class ServiceItem {
            private Long serviceId;
            private Integer quantity;
            private Double subtotal;
        }

        
    }
