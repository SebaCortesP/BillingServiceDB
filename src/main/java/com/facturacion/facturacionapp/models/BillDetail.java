package com.facturacion.facturacionapp.models;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bill_details")
public class BillDetail extends RepresentationModel<BillDetail>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con VeterinaryService
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "El servicio veterinario es obligatorio")
    private VeterinaryService service;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int quantity;

    @Column(name = "subtotal", nullable = false)
    @NotNull(message = "El subtotal no puede ser nulo")
    private Integer subtotal;

    // Relación inversa con Bill
    @ManyToOne
    @JoinColumn(name = "bill_id")
    @JsonBackReference
    private Bill bill;

    public BillDetail(VeterinaryService service, int quantity) {
        this.service = service;
        this.quantity = quantity;
        this.subtotal = service.getPrice() * quantity;
    }

    // Puede ser útil si querés recalcular el subtotal
    public void calculateSubtotal() {
        if (this.service != null) {
            this.subtotal = this.service.getPrice() * this.quantity;
        }
    }
}
