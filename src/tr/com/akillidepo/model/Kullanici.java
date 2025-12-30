package tr.com.akillidepo.model;

public abstract class Kullanici implements IKullaniciIslemleri {
    
    protected int id;
    protected String kullaniciAdi;
    protected String sifre;
    protected String rol;

    public Kullanici(int id, String kullaniciAdi, String sifre, String rol) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.rol = rol;
    }
    
    // Ortak metodlar
    public String getKullaniciAdi() { 
    	return kullaniciAdi; 
    }
    
    @Override
    public String getYetki() { 
    	return rol; 
    }
    
    @Override
    public boolean girisYap(String kadi, String sifre) {
        return this.kullaniciAdi.equals(kadi) && this.sifre.equals(sifre);
    }
}