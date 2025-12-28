package tr.com.akillidepo.model;

public class Yonetici extends Kullanici {
    public Yonetici(int id, String kadi, String sifre) {
        super(id, kadi, sifre, "Yonetici");
    }
    // Yöneticiye özel ürün ekleme/silme metodları buraya gelecek
}