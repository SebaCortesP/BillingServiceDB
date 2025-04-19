package com.facturacion.facturacionapp.models;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bills")
public class Bill {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Client
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    private Client cliente;

    @Column(name = "total", nullable = false)
    @NotNull(message = "El monto total es obligatorio")
    private Double total = 0.0;

    @Column(name = "payed", nullable = false)
    private boolean payed = false;

    // Relación con detalles de la factura
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BillDetail> details;
    
    public Bill(Long id, Client cliente) {
        this.id = id;
        this.cliente = cliente;
        this.details = new ArrayList<>();
    }

    public void addDetail(BillDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        detail.setBill(this);
        this.details.add(detail);
        this.total += detail.getSubtotal();
    }
    
    public void pay() {
        this.payed = true;
    }
}
