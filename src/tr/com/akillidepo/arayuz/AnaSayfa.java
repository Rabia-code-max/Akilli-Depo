package tr.com.akillidepo.arayuz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
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

    public static void main(String[] args) {
        new AnaSayfa("Yonetici").setVisible(true);
    }

    public AnaSayfa(String rol) {
        this.kullaniciRolu = rol;
        
        setTitle("Akıllı Depo Sistemi - Ana Sayfa (" + rol + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600); 
        
        // ARKA PLAN RESMİ
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                ImageIcon icon = new ImageIcon("images/ana_bg.jpg"); 
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // BAŞLIK
        JLabel lblBaslik = new JLabel("DEPO STOK DURUMU");
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 24));
        
        lblBaslik.setForeground(new Color(200, 200, 200)); 
        lblBaslik.setBounds(20, 20, 400, 30);
        contentPane.add(lblBaslik);
        
        // ÇIKIŞ BUTONU
        JButton btnCikis = new JButton("Çıkış Yap");
        btnCikis.setBackground(new Color(220, 20, 60));
        btnCikis.setForeground(Color.RED);
        btnCikis.setBounds(750, 20, 100, 30);
        contentPane.add(btnCikis);
        
        btnCikis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new GirisEkrani().setVisible(true);
            }
        });

        // TABLO (LİSTE)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 840, 400);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        contentPane.add(scrollPane);

        // TABLO AYARLARI 
        table = new JTable() {
            
            @Override
            public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                java.awt.Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof javax.swing.JComponent) {
                    ((javax.swing.JComponent) c).setOpaque(false);
                }
                return c;
            }
            
            
            @Override
            protected void paintComponent(Graphics g) {
               
                g.setColor(new Color(255, 255, 255, 200)); 
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        
        table.setOpaque(false);
        
        // Tablo başlıkları
        model = new DefaultTableModel();
        Object[] kolonlar = {"ID", "Ürün Adı", "Miktar", "Min", "Max", "Raf", "Raf No", "Bölüm"};
        model.setColumnIdentifiers(kolonlar);
        table.setModel(model);
        scrollPane.setViewportView(table);

        // Butonları ayarla
        butonlariAyarla();

        // Verileri getir
        urunleriGetir();
    }
    
    public void urunleriGetir() {
        model.setRowCount(0);
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
    
    private void butonlariAyarla() {
        if(kullaniciRolu.equals("Yonetici")) {
            JButton btnEkle = new JButton("Ürün Ekle / Sil");
            btnEkle.setBounds(20, 490, 150, 40);
            contentPane.add(btnEkle);
            
            btnEkle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UrunYonetimEkrani yonetim = new UrunYonetimEkrani(AnaSayfa.this);
                    yonetim.setVisible(true);
                }
            });
        } 
        else if (kullaniciRolu.equals("Musteri")) {
            JButton btnSepet = new JButton("Sepete Ekle");
            btnSepet.setBounds(20, 490, 150, 40);
            btnSepet.setBackground(new Color(255, 165, 0)); 
            contentPane.add(btnSepet);
            
            btnSepet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int seciliSatir = table.getSelectedRow();
                    if (seciliSatir == -1) { JOptionPane.showMessageDialog(null, "Lütfen listeden bir ürün seçin!"); return; }
                    
                    String urunAdi = model.getValueAt(seciliSatir, 1).toString();
                    int urunId = Integer.parseInt(model.getValueAt(seciliSatir, 0).toString());
                    int stok = Integer.parseInt(model.getValueAt(seciliSatir, 2).toString());
                    
                    String adetStr = JOptionPane.showInputDialog("Kaç adet almak istiyorsunuz?");
                    if(adetStr != null && !adetStr.isEmpty()) {
                        try {
                            int adet = Integer.parseInt(adetStr);
                            if (adet <= 0) { JOptionPane.showMessageDialog(null, "Lütfen 0'dan büyük bir değer giriniz!"); return; }
                            if(adet > stok) { JOptionPane.showMessageDialog(null, "Yetersiz Stok! Mevcut: " + stok); } 
                            else { sepetListesi.add(urunId + "-" + adet + "-" + urunAdi); JOptionPane.showMessageDialog(null, urunAdi + " sepete eklendi!"); }
                        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(null, "Lütfen geçerli bir SAYI giriniz!"); }
                    }
                }
            });
            
            JButton btnSiparis = new JButton("Siparişi Tamamla");
            btnSiparis.setBounds(190, 490, 150, 40);
            btnSiparis.setBackground(new Color(60, 179, 113)); 
            contentPane.add(btnSiparis);
            
            btnSiparis.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(sepetListesi.isEmpty()) { JOptionPane.showMessageDialog(null, "Sepetiniz boş!"); } 
                    else { SiparisEkrani ekran = new SiparisEkrani(); ekran.setVisible(true); }
                }
            });
        }
        else if (kullaniciRolu.equals("DepoCalisani")) {
            JButton btnYenile = new JButton("Listeyi Yenile");
            btnYenile.setBounds(20, 490, 150, 40);
            btnYenile.setBackground(new Color(100, 149, 237)); 
            contentPane.add(btnYenile);
            
            btnYenile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    urunleriGetir(); 
                    JOptionPane.showMessageDialog(null, "Liste güncellendi.");
                }
            });
            
            JButton btnRapor = new JButton("Kritik Stoklar");
            btnRapor.setBounds(190, 490, 150, 40);
            btnRapor.setBackground(new Color(255, 69, 0));
            btnRapor.setForeground(Color.RED); 
            contentPane.add(btnRapor);
            
            btnRapor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stokRaporuVer();
                }
            });
        }
    }

    private void stokRaporuVer() {
        StringBuilder rapor = new StringBuilder("KRİTİK STOK LİSTESİ:\n\n");
        boolean kritikVar = false;
        
        for(int i=0; i < model.getRowCount(); i++) {
            String urunAdi = model.getValueAt(i, 1).toString();
            int miktar = Integer.parseInt(model.getValueAt(i, 2).toString());
            int minStok = Integer.parseInt(model.getValueAt(i, 3).toString());
            
            if(miktar <= minStok) {
                rapor.append("- ").append(urunAdi)
                     .append(" (Stok: ").append(miktar)
                     .append(" / Min: ").append(minStok).append(")\n");
                kritikVar = true;
            }
        }
        
        if(kritikVar) {
            JOptionPane.showMessageDialog(null, rapor.toString(), "Dikkat Edilecek Ürünler", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Harika! Kritik seviyede ürün yok.", "Stok Durumu", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}