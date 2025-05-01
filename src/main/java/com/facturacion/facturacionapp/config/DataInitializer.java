package com.facturacion.facturacionapp.config;

import com.facturacion.facturacionapp.models.Bill;
import com.facturacion.facturacionapp.models.BillDetail;
import com.facturacion.facturacionapp.models.Client;
import com.facturacion.facturacionapp.models.VeterinaryService;
import com.facturacion.facturacionapp.repositories.BillRepository;
import com.facturacion.facturacionapp.repositories.ClientRepository;
import com.facturacion.facturacionapp.repositories.VeterinaryServiceRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class DataInitializer {
        @Bean
        CommandLineRunner initData(
            VeterinaryServiceRepository serviceRepo,
            ClientRepository clientRepository,
            BillRepository billRepo
        ) {
            return args -> {
                if (clientRepository.count() > 0) {
                    System.out.println("Ya existen datos. Saltando inicialización.");
                    return;
                }
        
                System.out.println("Inicializando datos...");
        
                Client c1 = new Client(null, "Fabian Nitan Ayuda", "Calle 123", "123456789");
                Client c2 = new Client(null, "Rodrigo Guerrero", "Av. Libertador 456", "987654321");
                Client c3 = new Client(null, "Pepe Tapia", "Calle Falsa 123", null);
                Client c4 = new Client(null, "Sebastian Soto", "Ruta 40", "555555555");
                Client c5 = new Client(null, "Braulio Perez", "Calle Sarmiento", null);
                clientRepository.saveAll(List.of(c1, c2, c3, c4, c5));
        
                VeterinaryService s1 = new VeterinaryService(null, "Consulta general", "Revisión completa", 20000);
                VeterinaryService s2 = new VeterinaryService(null, "Vacunación", "Aplicación de vacunas", 15000);
                VeterinaryService s3 = new VeterinaryService(null, "Desparasitación", "Eliminación de parásitos", 10000);
                VeterinaryService s4 = new VeterinaryService(null, "Radiografía", "Diagnóstico por imagen", 30000);
                VeterinaryService s5 = new VeterinaryService(null, "Cirugía menor", "Intervención quirúrgica", 50000);
                serviceRepo.saveAll(List.of(s1, s2, s3, s4, s5));
        
                Bill bill1 = new Bill();
                bill1.setCliente(c1);
                bill1.setPayed(false);
                bill1.addDetail(new BillDetail(s1, 1));
                bill1.addDetail(new BillDetail(s2, 2));
        
                Bill bill2 = new Bill();
                bill2.setCliente(c2);
                bill2.setPayed(true);
                bill2.addDetail(new BillDetail(s5, 1));
        
                billRepo.saveAll(List.of(bill1, bill2));
        
                System.out.println("Datos iniciales cargados.");
            };
        }
             
}
