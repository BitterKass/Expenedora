package daos;

import model.Producte;
import model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
    public Slot readSlot(int pos) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot where posicio = ?");
        ps.setInt(1, pos);
        Slot s = new Slot();

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            s.setPosicio(rs.getInt(1));
            s.setQuantitat(rs.getInt(2));
            s.setCodi_producte(rs.getString(3));
        }
        return s;
    }

    @Override
    public Slot restarSlot(int pos) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot where posicio = ?");
        ps.setInt(1, pos);
        Slot s = new Slot();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            s.setPosicio(rs.getInt(1));
            s.setQuantitat(rs.getInt(2));
            s.setCodi_producte(rs.getString(3));
        }

        ps = conn.prepareStatement("UPDATE slot SET posicio = ?, quantitat = ?, codi_producte = ? where posicio = ?");
        ps.setInt(1, s.getPosicio());
        ps.setInt(2,  s.getQuantitat()-1);
        ps.setString(3, s.getCodi_producte());
        ps.setInt(4, s.getPosicio());

        ps.executeUpdate();

        return s;
    }

    @Override
    public void modificarStock(int pos) throws SQLException {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Introdueix el nou stock a introduir: ");
        int nouStock = Integer.parseInt(entrada.nextLine());

        if (nouStock < 0) {
            System.out.println("El nou stock no pot ser un nombre negatiu.");

        }else {
            PreparedStatement ps = conn.prepareStatement("UPDATE slot SET quantitat = ? WHERE slot = ?");
            ps.setInt(1, nouStock);
            ps.setInt(2, pos);
            ps.executeUpdate();
        }
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

            s.setPosicio(rs.getInt(1));
            s.setQuantitat(rs.getInt(2));
            s.setCodi_producte(rs.getString(3));

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
