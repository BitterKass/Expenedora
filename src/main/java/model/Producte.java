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

    public Producte(Producte p){
        this.codiProducte = p.codiProducte;
        this.nom = p.nom;
        this.descripcio = p.descripcio;
        this.preuCompra = p.preuCompra;
        this.preuVenta = p.preuVenta;
    }
}
