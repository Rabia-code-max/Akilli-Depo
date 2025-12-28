package tr.com.akillidepo.arayuz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import tr.com.akillidepo.veritabani.Baglanti;

public class UrunYonetimEkrani extends JFrame {

    private JPanel contentPane;
    private JTextField txtUrunAdi, txtMiktar, txtMin, txtMax, txtRafBolum, txtRafNo, txtBolumNo, txtSilId;
    private AnaSayfa anaSayfaRef; // Ana sayfayı yenilemek için referans

    public UrunYonetimEkrani(AnaSayfa anaSayfa) {
        this.anaSayfaRef = anaSayfa;
        
        setTitle("Ürün Yönetim Paneli");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sadece bu pencereyi kapat
        setBounds(100, 100, 400, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBaslik = new JLabel("YENİ ÜRÜN EKLE");
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 16));
        lblBaslik.setBounds(120, 10, 200, 20);
        contentPane.add(lblBaslik);

        // --- FORM ALANLARI ---
        txtUrunAdi = formElemaniEkle("Ürün Adı:", 40);
        txtMiktar = formElemaniEkle("Miktar:", 80);
        txtMin = formElemaniEkle("Min Stok:", 120);
        txtMax = formElemaniEkle("Max Stok:", 160);
        txtRafBolum = formElemaniEkle("Raf Bölümü (A,B,C):", 200);
        txtRafNo = formElemaniEkle("Raf No:", 240);
        txtBolumNo = formElemaniEkle("Bölüm No:", 280);

        // --- EKLE BUTONU ---
        JButton btnEkle = new JButton("ÜRÜNÜ SİSTEME EKLE");
        btnEkle.setBackground(new Color(60, 179, 113));
        btnEkle.setForeground(Color.WHITE);
        btnEkle.setBounds(50, 330, 280, 40);
        contentPane.add(btnEkle);
        
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                urunEkle();
            }
        });

        // --- AYIRAÇ ---
        JLabel lblAyirac = new JLabel("---------------------------------------------------------------");
        lblAyirac.setBounds(20, 380, 350, 14);
        contentPane.add(lblAyirac);
        
        // --- SİLME BÖLÜMÜ ---
        JLabel lblSil = new JLabel("ÜRÜN SİL (ID Giriniz):");
        lblSil.setFont(new Font("Arial", Font.BOLD, 14));
        lblSil.setBounds(20, 410, 150, 20);
        contentPane.add(lblSil);
        
        txtSilId = new JTextField();
        txtSilId.setBounds(180, 410, 150, 30);
        contentPane.add(txtSilId);
        
        JButton btnSil = new JButton("SİL");
        btnSil.setBackground(Color.RED);
        btnSil.setForeground(Color.WHITE);
        btnSil.setBounds(180, 450, 150, 30);
        contentPane.add(btnSil);
        
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                urunSil();
            }
        });
    }
    
    // Pratik text field oluşturucu
    private JTextField formElemaniEkle(String etiket, int y) {
        JLabel lbl = new JLabel(etiket);
        lbl.setBounds(20, y, 150, 20);
        contentPane.add(lbl);
        
        JTextField txt = new JTextField();
        txt.setBounds(180, y, 150, 30);
        contentPane.add(txt);
        return txt;
    }

    // --- VERİTABANI İŞLEMLERİ ---
    
    private void urunEkle() {
        try {
            Connection conn = Baglanti.getBaglanti();
            String sql = "INSERT INTO urunler (urun_adi, miktar, min_stok, max_stok, raf_bolumu, raf_no, bolum_no) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, txtUrunAdi.getText());
            ps.setInt(2, Integer.parseInt(txtMiktar.getText()));
            ps.setInt(3, Integer.parseInt(txtMin.getText()));
            ps.setInt(4, Integer.parseInt(txtMax.getText()));
            ps.setString(5, txtRafBolum.getText());
            ps.setInt(6, Integer.parseInt(txtRafNo.getText()));
            ps.setInt(7, Integer.parseInt(txtBolumNo.getText()));
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ürün Başarıyla Eklendi!");
            
            // Ana sayfadaki tabloyu yenile
            anaSayfaRef.urunleriGetir(); 
            dispose(); // Pencereyi kapat
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }
    }
    
    private void urunSil() {
        try {
            Connection conn = Baglanti.getBaglanti();
            String sql = "DELETE FROM urunler WHERE urun_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtSilId.getText()));
            
            int sonuc = ps.executeUpdate();
            if(sonuc > 0) {
                JOptionPane.showMessageDialog(null, "Ürün Silindi!");
                anaSayfaRef.urunleriGetir(); // Tabloyu yenile
                txtSilId.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Bu ID ile ürün bulunamadı.");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }
    }
}