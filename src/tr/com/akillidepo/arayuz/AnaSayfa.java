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
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import tr.com.akillidepo.veritabani.Baglanti;

public class AnaSayfa extends JFrame {
	public static java.util.ArrayList<String> sepetListesi = new java.util.ArrayList<>();
	public static double toplamTutar = 0;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private String kullaniciRolu;

    // Test için main (Normalde Giriş Ekranından çağrılacak)
    public static void main(String[] args) {
        new AnaSayfa("Yonetici").setVisible(true);
    }

    public AnaSayfa(String rol) {
        this.kullaniciRolu = rol;
        
        setTitle("Akıllı Depo Sistemi - Ana Sayfa (" + rol + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600); // Geniş bir ekran
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255)); // Açık mavi/beyaz ferah ton
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- BAŞLIK ---
        JLabel lblBaslik = new JLabel("DEPO STOK DURUMU");
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 24));
        lblBaslik.setForeground(new Color(47, 79, 79));
        lblBaslik.setBounds(20, 20, 400, 30);
        contentPane.add(lblBaslik);
        
        // --- ÇIKIŞ BUTONU ---
        JButton btnCikis = new JButton("Çıkış Yap");
        btnCikis.setBackground(new Color(220, 20, 60));
        btnCikis.setForeground(Color.WHITE);
        btnCikis.setBounds(750, 20, 100, 30);
        contentPane.add(btnCikis);
        
        btnCikis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Kapat
                new GirisEkrani().setVisible(true); // Girişe dön
            }
        });

        // --- TABLO (LİSTE) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 840, 400);
        contentPane.add(scrollPane);

        table = new JTable();
        // Tablo başlıkları
        model = new DefaultTableModel();
        Object[] kolonlar = {"ID", "Ürün Adı", "Miktar", "Min", "Max", "Raf", "Raf No", "Bölüm"};
        model.setColumnIdentifiers(kolonlar);
        table.setModel(model);
        scrollPane.setViewportView(table);

        // --- ROLE GÖRE BUTONLAR ---
        butonlariAyarla();

        // --- VERİLERİ GETİR ---
        urunleriGetir();
    }
    
    // Veritabanından ürünleri çeken metod
    public void urunleriGetir() {
        model.setRowCount(0); // Tabloyu temizle
        try {
            Connection conn = Baglanti.getBaglanti();
            String sql = "SELECT * FROM urunler";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Object[] satir = {
                    rs.getInt("urun_id"),
                    rs.getString("urun_adi"),
                    rs.getInt("miktar"),
                    rs.getInt("min_stok"),
                    rs.getInt("max_stok"),
                    rs.getString("raf_bolumu"),
                    rs.getInt("raf_no"),
                    rs.getInt("bolum_no")
                };
                model.addRow(satir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Rol kontrolüne göre buton ekleme
    private void butonlariAyarla() {
        if(kullaniciRolu.equals("Yonetici")) {
            JButton btnEkle = new JButton("Ürün Ekle / Sil");
            btnEkle.setBounds(20, 490, 150, 40);
            contentPane.add(btnEkle);
            
            btnEkle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Yönetim ekranını aç ve bu sayfayı (AnaSayfa.this) ona gönder
                    UrunYonetimEkrani yonetim = new UrunYonetimEkrani(AnaSayfa.this);
                    yonetim.setVisible(true);
                }
            });
        } 
        else if (kullaniciRolu.equals("Musteri")) {
        	// --- SEPETE EKLE BUTONU (GÜNCELLENMİŞ GÜVENLİ HALİ) ---
            JButton btnSepet = new JButton("Sepete Ekle");
            btnSepet.setBounds(20, 490, 150, 40);
            btnSepet.setBackground(new Color(255, 165, 0)); // Turuncu
            contentPane.add(btnSepet);
            
            btnSepet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // 1. Tablodan seçili satırı al
                    int seciliSatir = table.getSelectedRow();
                    
                    if (seciliSatir == -1) {
                        JOptionPane.showMessageDialog(null, "Lütfen listeden bir ürün seçin!");
                        return;
                    }
                    
                    // 2. Ürün bilgilerini al
                    String urunAdi = model.getValueAt(seciliSatir, 1).toString();
                    int urunId = Integer.parseInt(model.getValueAt(seciliSatir, 0).toString());
                    int stok = Integer.parseInt(model.getValueAt(seciliSatir, 2).toString());
                    
                    // 3. Adet sor (HATA ÇÖZÜMÜ BURADA)
                    String adetStr = JOptionPane.showInputDialog("Kaç adet almak istiyorsunuz?");
                    
                    if(adetStr != null && !adetStr.isEmpty()) {
                        try {
                            // Girilen değeri sayıya çevirmeyi dene
                            int adet = Integer.parseInt(adetStr);
                            
                            // Negatif veya 0 girilirse engelle
                            if (adet <= 0) {
                                JOptionPane.showMessageDialog(null, "Lütfen 0'dan büyük bir değer giriniz!");
                                return;
                            }
                            
                            // Stok kontrolü
                            if(adet > stok) {
                                JOptionPane.showMessageDialog(null, "Yetersiz Stok! Mevcut: " + stok);
                            } else {
                                // Sepete ekle (Format: ID-Adet-Ad)
                                sepetListesi.add(urunId + "-" + adet + "-" + urunAdi);
                                JOptionPane.showMessageDialog(null, urunAdi + " sepete eklendi!");
                            }
                        } catch (NumberFormatException ex) {
                            // Eğer sayı yerine harf girilirse program çökmez, bu uyarıyı verir:
                            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir SAYI giriniz! (Örn: 1, 2, 5)");
                        }
                    }
                }
            });
            
            JButton btnSiparis = new JButton("Siparişi Tamamla");
            btnSiparis.setBounds(190, 490, 150, 40);
            btnSiparis.setBackground(new Color(60, 179, 113)); // Yeşil
            contentPane.add(btnSiparis);
            
            btnSiparis.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(sepetListesi.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Sepetiniz boş!");
                    } else {
                        // Sipariş Ekranını Aç
                        SiparisEkrani ekran = new SiparisEkrani();
                        ekran.setVisible(true);
                    }
                }
            });
        }
    }
}