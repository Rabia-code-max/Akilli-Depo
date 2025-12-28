package tr.com.akillidepo.model;

public class Musteri extends Kullanici {
    // Müşterinin ekstra özellikleri olacak
    private String adres;
    private String telefon;

    public Musteri(int id, String kadi, String sifre) {
        super(id, kadi, sifre, "Musteri");
    }
    
    // Getter ve Setter metodları
    public String getAdres() { 
    	return adres; 
    	}
    public void setAdres(String adres) { 
    	this.adres = adres; 
    	}
    
    public String getTelefon() { 
    	return telefon; 
    	}
    public void setTelefon(String telefon) { 
    	this.telefon = telefon; 
    	}
}