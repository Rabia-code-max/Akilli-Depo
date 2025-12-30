package tr.com.akillidepo.model;

public interface IKullaniciIslemleri {
    
    boolean girisYap(String kadi, String sifre);
    
    String getYetki();
}