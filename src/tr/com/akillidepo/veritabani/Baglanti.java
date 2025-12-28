package tr.com.akillidepo.veritabani;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Baglanti {
    
    // Veritabanı bilgileri
    private static final String KULLANICI_ADI = "root";
    private static final String SIFRE = "root2507"; // <-- MySQL Şifrenle değiştir
    private static final String DB_ISMI = "akilli_depo";
    // Türkçe karakter ve saat dilimi ayarları eklenmiş bağlantı adresi
    private static final String HOST = "jdbc:mysql://localhost:3306/" + DB_ISMI + "?useSSL=false&serverTimezone=Turkey&useUnicode=true&characterEncoding=UTF-8";
    
    private static Connection connection = null;

    // Bağlantıyı getiren metod (Singleton yapısı)
    public static Connection getBaglanti() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(HOST, KULLANICI_ADI, SIFRE);
                System.out.println("Veritabanı bağlantısı BAŞARILI!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return connection;
    }
}