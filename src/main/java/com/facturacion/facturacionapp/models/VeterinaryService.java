package com.facturacion.facturacionapp.models;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "veterinary_services")
public class VeterinaryService extends RepresentationModel<VeterinaryService>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @NotNull(message = "El precio del servicio es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Integer price;

    public VeterinaryService(@NotBlank(message = "El nombre del servicio es obligatorio") String name,
            String description,
            @NotNull(message = "El precio del servicio es obligatorio") @Min(value = 1, message = "El precio debe ser mayor a 0") Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    
}
