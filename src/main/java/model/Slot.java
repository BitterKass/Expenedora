package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    private int posicio;
    private int quantitat;
    private String codi_producte;

    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }

    public String getCodi_producte() {
        return codi_producte;
    }

    public void setCodi_producte(String codi_producte) {
        this.codi_producte = codi_producte;
    }
}
