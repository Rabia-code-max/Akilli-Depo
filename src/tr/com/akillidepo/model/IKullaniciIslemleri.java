package tr.com.akillidepo.model;

public interface IKullaniciIslemleri {
    // Her kullanıcı giriş yapabilmeli
    boolean girisYap(String kadi, String sifre);
    
    // Her kullanıcının yetkisi sorgulanabilmeli
    String getYetki();
}