package tr.com.akillidepo.arayuz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import tr.com.akillidepo.veritabani.Baglanti;

public class SiparisEkrani extends JFrame {

    private JPanel contentPane;
    private JTextField txtTelefon;
    private JTextArea txtAdres;
    
    public SiparisEkrani() {
        setTitle("Sipariş Onay Ekranı");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBaslik = new JLabel("TESLİMAT BİLGİLERİ");
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 18));
        lblBaslik.setBounds(150, 20, 250, 30);
        contentPane.add(lblBaslik);
        
        // --- TELEFON ---
        JLabel lblTel = new JLabel("Telefon:");
        lblTel.setBounds(30, 80, 100, 20);
        contentPane.add(lblTel);
        
        JLabel lblKod = new JLabel("+90");
        lblKod.setFont(new Font("Arial", Font.BOLD, 14));
        lblKod.setBounds(100, 80, 40, 30);
        contentPane.add(lblKod);
        
        txtTelefon = new JTextField();
        txtTelefon.setToolTipText("Başında 0 olmadan 10 hane giriniz");
        txtTelefon.setBounds(140, 80, 200, 30);
        contentPane.add(txtTelefon);
        txtTelefon.setColumns(10);
        
        JLabel lblUyari = new JLabel("(Başına 0 koymadan 10 haneli giriniz)");
        lblUyari.setFont(new Font("Arial", Font.ITALIC, 10));
        lblUyari.setForeground(Color.RED);
        lblUyari.setBounds(140, 110, 250, 15);
        contentPane.add(lblUyari);

        // --- ADRES ---
        JLabel lblAdres = new JLabel("Adres:");
        lblAdres.setBounds(30, 150, 100, 20);
        contentPane.add(lblAdres);
        
        txtAdres = new JTextArea();
        txtAdres.setLineWrap(true);
        
        JScrollPane scroll = new JScrollPane(txtAdres);
        scroll.setBounds(100, 150, 300, 150);
        contentPane.add(scroll);
        
        JLabel lblAdresUyari = new JLabel("<html>Zorunlu: Mahalle, Cadde/Sokak, Bina No, Daire, İlçe, İl<br>Not: Sadece İstanbul içi sipariş alınır.</html>");
        lblAdresUyari.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAdresUyari.setForeground(Color.BLUE);
        lblAdresUyari.setBounds(100, 310, 350, 40);
        contentPane.add(lblAdresUyari);

        // ONAY BUTONU 
        JButton btnOnay = new JButton("SİPARİŞİ ONAYLA");
        btnOnay.setBackground(new Color(60, 179, 113));
        btnOnay.setForeground(Color.GREEN);
        btnOnay.setFont(new Font("Arial", Font.BOLD, 14));
        btnOnay.setBounds(100, 370, 300, 50);
        contentPane.add(btnOnay);
        
        btnOnay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                siparisKontrolVeKayit();
            }
        });
    }
    
    // YARDIMCI METOD: Türkçe karakterleri İngilizceye çevirir (İ->I, Ç->C, Ş->S gibi)
    private String metniTemizle(String metin) {
        String temiz = metin.toUpperCase();
        temiz = temiz.replace("İ", "I");
        temiz = temiz.replace("ı", "I");
        temiz = temiz.replace("Ç", "C");
        temiz = temiz.replace("Ş", "S");
        temiz = temiz.replace("Ğ", "G");
        temiz = temiz.replace("Ü", "U");
        temiz = temiz.replace("Ö", "O");
        return temiz;
    }
    
    // Girdileri Kontrol Eden Metod
    private void siparisKontrolVeKayit() {
        String tel = txtTelefon.getText().trim();
        String temizAdres = metniTemizle(txtAdres.getText()); 
        
        // 1. KURAL: TELEFON
        if (tel.length() != 10) { hataGoster("Telefon numarası tam 10 haneli olmalıdır!"); return; }
        if (tel.startsWith("0")) { hataGoster("Telefon numarası 0 ile başlayamaz!"); return; }
        if (!tel.matches("[0-9]+")) { hataGoster("Telefon sadece rakam içermelidir!"); return; }
        
        // 2. KURAL: ADRES 
        if (!temizAdres.contains("MAHALLE")) { hataGoster("Adres 'Mahalle' bilgisi içermelidir!"); return; }
        if (!temizAdres.contains("CADDE") && !temizAdres.contains("SOKAK")) { hataGoster("Adres 'Cadde' veya 'Sokak' bilgisi içermelidir!"); return; }
        if (!temizAdres.contains("BINA") && !temizAdres.contains("NO")) { hataGoster("Bina Adı veya Numarası zorunludur!"); return; }
        if (!temizAdres.contains("DAIRE")) { hataGoster("Daire numarası zorunludur!"); return; }
        if (!temizAdres.contains("ILCE")) { hataGoster("İlçe bilgisi zorunludur!"); return; }
        if (!temizAdres.contains("IL")) { hataGoster("İl bilgisi zorunludur!"); return; }
        
        // 3. KURAL: ŞEHİR (Sadece İSTANBUL)
        if (!temizAdres.contains("ISTANBUL")) {
            hataGoster("Üzgünüz! Depomuz sadece İSTANBUL içine hizmet vermektedir.");
            return;
        }
        
        veritabaninaIsle();
    }
    
    private void hataGoster(String mesaj) {
        JOptionPane.showMessageDialog(null, mesaj, "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
    }
    
    private void veritabaninaIsle() {
        try {
            Connection conn = Baglanti.getBaglanti();
            
            for (String urun : AnaSayfa.sepetListesi) {
                String[] parcalar = urun.split("-");
                int id = Integer.parseInt(parcalar[0]);
                int satilanAdet = Integer.parseInt(parcalar[1]);
                
                
                String sqlBilgi = "SELECT miktar, min_stok, max_stok, urun_adi FROM urunler WHERE urun_id = ?";
                PreparedStatement psBilgi = conn.prepareStatement(sqlBilgi);
                psBilgi.setInt(1, id);
                ResultSet rs = psBilgi.executeQuery();
                
                if (rs.next()) {
                    int mevcutStok = rs.getInt("miktar");
                    int minStok = rs.getInt("min_stok");
                    int maxStok = rs.getInt("max_stok");
                    String urunAdi = rs.getString("urun_adi");
                    
                    // Stoktan düşüyoruz
                    int yeniStok = mevcutStok - satilanAdet;
                    
                    // A) Müşterinin Siparişini Kaydet
                    String sqlMusteriSiparis = "INSERT INTO siparisler (urun_id, adet, adres, telefon, durum) VALUES (?, ?, ?, ?, 'Müşteri Siparişi')";
                    PreparedStatement psMus = conn.prepareStatement(sqlMusteriSiparis);
                    psMus.setInt(1, id);
                    psMus.setInt(2, satilanAdet);
                    psMus.setString(3, txtAdres.getText());
                    psMus.setString(4, "+90" + txtTelefon.getText());
                    psMus.executeUpdate();
                    
                    // B) Ürün Stoğunu Güncelle (Düşür)
                    String sqlGuncelle = "UPDATE urunler SET miktar = ? WHERE urun_id = ?";
                    PreparedStatement psGuncelle = conn.prepareStatement(sqlGuncelle);
                    psGuncelle.setInt(1, yeniStok);
                    psGuncelle.setInt(2, id);
                    psGuncelle.executeUpdate();
                    
                    // C) KRİTİK KONTROL
                    // Eğer yeni stok, minimumun altına indiyse?
                    if (yeniStok < minStok) {
                        int siparisEdilecekMiktar = maxStok - yeniStok; // Depoyu max'a tamamlayacak miktar
                        
                        // Depoyu Geri Doldur (Max yap)
                        String sqlOtoDolum = "UPDATE urunler SET miktar = ? WHERE urun_id = ?";
                        PreparedStatement psOto = conn.prepareStatement(sqlOtoDolum);
                        psOto.setInt(1, maxStok);
                        psOto.setInt(2, id);
                        psOto.executeUpdate();
                        
                        
                        String sqlSistemSiparis = "INSERT INTO siparisler (urun_id, adet, adres, telefon, durum) VALUES (?, ?, ?, ?, 'SİSTEM OTO. TEDARİK')";
                        PreparedStatement psSis = conn.prepareStatement(sqlSistemSiparis);
                        psSis.setInt(1, id);
                        psSis.setInt(2, siparisEdilecekMiktar);
                        psSis.setString(3, "DEPO MERKEZ - OTOMATİK DOLUM");
                        psSis.setString(4, "SISTEM");
                        psSis.executeUpdate();
                        
                        
                        System.out.println("UYARI: " + urunAdi + " stoğu azaldı. Sistem otomatik olarak " + siparisEdilecekMiktar + " adet sipariş geçti.");
                    }
                }
            }
            
            JOptionPane.showMessageDialog(null, "Siparişiniz Başarıyla Alındı!\n(Stok kontrolleri yapıldı)");
            AnaSayfa.sepetListesi.clear(); 
            dispose(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Sistem Hatası: " + e.getMessage());
        }
    }
    
}