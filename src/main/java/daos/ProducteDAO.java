package daos;

import model.Producte;
import model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProducteDAO {

    public void createProducte(Producte p) throws SQLException;

    void createSlot(Slot s) throws SQLException;

    public Producte readProducte() throws SQLException;

    public ArrayList<Producte> readProductes() throws SQLException;

    ArrayList<Slot> readSlots() throws SQLException;

    public void updateProducte(Producte prod) throws SQLException;

    public void deleteProducte(Producte p) throws SQLException;

    public void deleteProducte(String codiProducte) throws SQLException;

}
