package daos;

import model.Producte;
import model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SlotDAO {
    public static boolean existeixSlotAmbPosicio(int posicio) {
        return false;
    }

    public void createSlot(Slot s) throws SQLException;

    Slot readSlot(int pos) throws SQLException;

    Slot restarSlot(int s) throws SQLException;
    void modificarStock(int pos) throws  SQLException;

    Producte readProducte() throws SQLException;

    public ArrayList<Slot> readSlots() throws SQLException;

    public void updateSlot(Slot s) throws SQLException;

    public void deleteSlot(Slot s) throws SQLException;

    void updateSlot(Producte p) throws SQLException;
}
