package com.example.eixo.marcasModelos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "modelos_veiculo")
@NoArgsConstructor
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo", nullable = false)
    private Long idModelo;

    @Column(name = "nome_modelo", nullable = false)
    private String nomeModelo;

    @ManyToOne
    @JoinColumn(name = "marcas_veiculo_id_marca", nullable = true)
    Marca marca;
}
