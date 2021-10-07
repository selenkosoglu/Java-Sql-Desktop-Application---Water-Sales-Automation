package appPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import props.Customer;
import props.Orders;

/**
 *
 * @author selenkosoglu
 */
public class DB {

    private final String driver = "org.sqlite.JDBC";
    private final String url = "jdbc:sqlite:db/susatis.db";

    private Connection conn = null;
    private PreparedStatement pre = null;

    public static Customer customer = new Customer();
    public static Orders orders = new Orders();

    int id = 0;

    public DB() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Success");
        } catch (Exception e) {
            System.err.println("Connection Error : " + e);
        }

    }

    // ADD
    public int müsteriEkleme(String adi, String soyadi, String telefon, String adres) {
        int status = 0;
        try {
            String sql = " insert into musteriler values ( null, ?, ?, ? , ? ) ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, adi);
            pre.setString(2, soyadi);
            pre.setString(3, telefon);
            pre.setString(4, adres);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("nottInsert Error : " + e);
            if (e.toString().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                status = -1;
            }
        }
        return status;
    }

    // UPDATE
    public int müsteriDüzenle(String adi, String soyadi, String telefon, String adres, int id) {
        int status = 0;
        try {
            String sql = " update musteriler set adi = ?, soyadi = ?, telefon = ?, adres = ? where id = ? ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, adi);
            pre.setString(2, soyadi);
            pre.setString(3, telefon);
            pre.setString(4, adres);
            pre.setInt(5, id);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("müsteriUpdate Error : " + e);
            if (e.toString().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                status = -1;
            }
        }
            
        
        return status;
    }

    // DELETE
    public int müsteriSil(int id) {
        int status = 0;
        try {
            String sql = "Delete From musteriler Where id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("müsteriDelete Error : " + e);
        }
        return status;
    }

    // SEARCH
    public DefaultTableModel müsteriAra(String name, String surname) {
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("ID");
        dtm.addColumn("Adı");
        dtm.addColumn("Soyadı");
        dtm.addColumn("Telefon");
        dtm.addColumn("Adres");
        try {
            String sql = "Select * From musteriler Where adi = ? COLLATE NOCASE";
            String sql2 = "Select * From musteriler Where soyadi = ? COLLATE NOCASE";
            String sql3 = "Select * From musteriler Where adi = ? COLLATE NOCASE and soyadi = ? COLLATE NOCASE";
            if (surname.equals("")) {
                pre = conn.prepareStatement(sql);
                pre.setString(1, name);
            } else if (name.equals("")) {
                pre = conn.prepareStatement(sql2);
                pre.setString(1, surname);
            } else {
                pre = conn.prepareStatement(sql3);
                pre.setString(1, name);
                pre.setString(2, surname);
            }
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String adi = rs.getString("adi");
                String soyadi = rs.getString("soyadi");
                String telefon = rs.getString("telefon");
                String adres = rs.getString("adres");
                Object[] row = {id, adi, soyadi, telefon, adres};
                dtm.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("searchCustomer Error : " + e);
        }
        return dtm;
    }

    // TO LIST ALL THE CUSTOMERS
    public DefaultTableModel allMusteri() {
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("ID");
        dtm.addColumn("Adı");
        dtm.addColumn("Soyadı");
        dtm.addColumn("Telefon");
        dtm.addColumn("Adres");
        try {
            String sql = "select * from musteriler";
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("adi");
                String surname = rs.getString("soyadi");
                String telefon = rs.getString("telefon");
                String address = rs.getString("adres");
                Object[] row = {id, name, surname, telefon, address};
                dtm.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("müşteriTable Error : " + e);
        }
        return dtm;
    }

    // TO LIST ALL THE ORDERS
    public DefaultTableModel allSiparis() {
        DefaultTableModel dtm = new DefaultTableModel();
        // add Cloumn
        dtm.addColumn("ID");
        dtm.addColumn("Müşteri Adı");
        dtm.addColumn("Müşteri Soyadı");
        dtm.addColumn("Durum");
        dtm.addColumn("Adres");
        dtm.addColumn("Tutar");
        // add Rows
        try {
            String sql = "select * from siparisler";
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("musteri_adi");
                String surname = rs.getString("musteri_soyadi");
                String state = rs.getString("durum");
                String address = rs.getString("adres");
                int price = rs.getInt("tutar");
                Object[] row = {id, name, surname, state, address, price};
                dtm.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("siparişTable Error : " + e);
        }
        return dtm;
    }

    // DELETE ORDER
    public int siparisSil(int id) {
        System.out.println("id: "+ id);
        int status = 0;
        try {
            String sql = "Delete From siparisler Where id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("siparisDelete Error : " + e);
        }
        return status;
    }

    // ADD ORDER
    public int siparisEkleme(String musteri_adi, String musteri_soyadi, String durum, String adres, int tutar) {
        int status = 0;
        try {
            String sql = " insert into siparisler values ( null, ?, ?, ? , ?, ? ) ";
            pre = conn.prepareStatement(sql);
            pre.setString(1, customer.getAdi());
            pre.setString(2, customer.getSoyadi());
            pre.setString(3, durum);
            pre.setString(4, adres);
            pre.setInt(5, tutar);

           

            status = pre.executeUpdate();

           
        } catch (Exception e) {
            System.err.println("nottInsert Error : " + e);
            if (e.toString().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                status = -1;
            }
        }
        return status;
    }

    // DELETE ALL ORDERS
    public int siparisleriSil(int id) {
        int status = 0;
        try {
            String sql = "Delete From siparisler";
            pre = conn.prepareStatement(sql);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("siparislerDelete Error : " + e);
        }
        return status;
    }

    //ON THE ROAD
    public int siparisYolda(String durum, int id) {
        int status = 0;
        try {
            String sql = "update siparisler set durum = ? where id = ?";
            pre = conn.prepareStatement(sql);
            pre.setString(1, durum);
            pre.setInt(2, id);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("siparisYolda Error : " + e);
        }
        return status;
    }

    //ORDER IS DELIVERED
    public int siparisTeslimEdildi(String durum, int id) {
        int status = 0;
        try {
            String sql = "update siparisler set durum = ? where id = ?";
            pre = conn.prepareStatement(sql);
            pre.setString(1, durum);
            pre.setInt(2, id);
            status = pre.executeUpdate();
        } catch (Exception e) {
            System.err.println("siparisTeslimEdildi Error : " + e);
        }
        return status;
    }
    
    //TODAYS DELIVERY
       public DefaultTableModel bugununSiparisi() {
        DefaultTableModel dtm = new DefaultTableModel();
        // add Cloumn
        dtm.addColumn("ID");
        dtm.addColumn("Müşteri Adı");
        dtm.addColumn("Müşteri Soyadı");
        dtm.addColumn("Durum");
        dtm.addColumn("Adres");
        dtm.addColumn("Tutar");
        // add Rows
        try {
            String sql = "select * from siparisler where durum = 'Hazırlanıyor'";
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("musteri_adi");
                String surname = rs.getString("musteri_soyadi");
                String state = rs.getString("durum");
                String address = rs.getString("adres");
                int price = rs.getInt("tutar");
                Object[] row = {id, name, surname, state, address, price};
                dtm.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("siparişTable Error : " + e);
        }
        return dtm;
    }
    

    // CLOSE METHOD
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            if (pre != null) {
                pre.close();
            }
        } catch (Exception e) {
            System.err.println("Close Error : " + e);
        }
    }
}
