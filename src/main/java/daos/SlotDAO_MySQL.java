package daos;

import model.Producte;
import model.Slot;

import java.sql.*;
import java.util.ArrayList;

public class SlotDAO_MySQL implements SlotDAO {

    //Dades de connexió a la base de dades
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "089800";

    private Connection conn = null;

    public SlotDAO_MySQL() {
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_ROUTE, DB_USER, DB_PWD);
            System.out.println("Conexió establerta satisfactoriament");
        } catch (Exception e) {
            System.out.println("S'ha produit un error en intentar connectar amb la base de dades. Revisa els paràmetres");
            System.out.println(e);
        }
    }

    @Override
    public void createSlot(Slot s) throws SQLException {

        PreparedStatement ps = conn.prepareStatement("INSERT INTO slot VALUES(?,?)");

        ps.setString(1, s.getCodi_producte());
        ps.setInt(2, s.getQuantitat());

        int rowCount = ps.executeUpdate();
    }

    @Override
    public Slot readSlot() throws SQLException {
        return null;
    }

    @Override
    public Producte readProducte() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<Slot> readSlots() throws SQLException {
        ArrayList<Slot> llistaSlots = new ArrayList<Slot>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Slot s = new Slot();

            /**
             p.setCodiProducte(rs.getString(codi_producte));
             p.setNom(rs.getString(nom));
             p.setDescripcio(rs.getString(descripcio));
             p.setPreuCompra(rs.getFloat(preu_compra));
             p.setPreuVenta(rs.getFloat(preu_venta));
             **/

            s.setCodi_producte(rs.getString(1));
            s.setQuantitat(rs.getInt(2));

            llistaSlots.add(s);
        }

        return llistaSlots;
    }

    @Override
    public void updateSlot(Slot s) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE slot SET posicio = ?, codi_producte = ?, quantitat = ? WHERE slot = ?");
        ps.setInt(1, s.getPosicio());
        ps.setString(2, s.getCodi_producte());
        ps.setInt(3, s.getQuantitat());
        ps.setInt(4, s.getPosicio());

        ps.executeUpdate();
    }


    @Override
    public void deleteSlot(Slot s) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM slot WHERE posicio = ?");
        ps.setInt(1, s.getPosicio());
        ps.executeUpdate();
    }

    @Override
    public void updateSlot(Producte p) throws SQLException {

    }
}
