package com.facturacion.facturacionapp.models;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "La dirección del cliente es obligatoria")
    private String address;

    @Column(name = "phone", nullable = true)
    private String phone; 

    public Client(@NotBlank(message = "El nombre del cliente es obligatorio") String name,
            @NotBlank(message = "La dirección del cliente es obligatoria") String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    
}
