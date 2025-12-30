package tr.com.akillidepo.model;

public class Yonetici extends Kullanici {
    
    public Yonetici(int id, String kadi, String sifre) {
        super(id, kadi, sifre, "Yonetici");
    }

    
    public void urunEklemeYetkisi() {
        System.out.println("Yönetici: Ürün ekleme yetkisine sahiptir.");
    }

    public void urunSilmeYetkisi() {
        System.out.println("Yönetici: Ürün silme yetkisine sahiptir.");
    }
    
    @Override
    public String toString() {
        return "Yönetici Rolü: " + getKullaniciAdi();
    }
}