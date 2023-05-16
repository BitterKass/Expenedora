package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producte {
    private String codiProducte;
    private String nom;
    private String descripcio;
    private float preuCompra;
    private float preuVenta;
//fer constructor copia
    public Producte(String codiProducte, String nom, String descripcio, float preuCompra, float preuVenta) {
        this.codiProducte = codiProducte;
        this.nom = nom;
        this.descripcio = descripcio;
        this.preuCompra = preuCompra;
        this.preuVenta = preuVenta;
    }
}
