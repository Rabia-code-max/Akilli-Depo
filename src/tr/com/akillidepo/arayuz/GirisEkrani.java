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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import tr.com.akillidepo.veritabani.Baglanti;

public class GirisEkrani extends JFrame {

    private JPanel contentPane;
    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;

    // Uygulamayı başlatan Main metodu
    public static void main(String[] args) {
        try {
            GirisEkrani frame = new GirisEkrani();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ekranın Tasarımı (Constructor)
    public GirisEkrani() {
        setTitle("Akıllı Depo Sistemi - Giriş");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245)); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null); 

        // Başlık
        JLabel lblBaslik = new JLabel("AKILLI DEPO GİRİŞİ");
        lblBaslik.setForeground(new Color(70, 130, 180));
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 22));
        lblBaslik.setBounds(110, 30, 250, 30);
        contentPane.add(lblBaslik);

        // Kullanıcı Adı Etiketi
        JLabel lblKullanici = new JLabel("Kullanıcı Adı:");
        lblKullanici.setFont(new Font("Arial", Font.PLAIN, 14));
        lblKullanici.setBounds(50, 100, 100, 20);
        contentPane.add(lblKullanici);

        // Kullanıcı Adı Kutusu
        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setFont(new Font("Arial", Font.PLAIN, 14));
        txtKullaniciAdi.setBounds(160, 95, 200, 30);
        contentPane.add(txtKullaniciAdi);
        txtKullaniciAdi.setColumns(10);

        // Şifre Etiketi
        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSifre.setBounds(50, 150, 100, 20);
        contentPane.add(lblSifre);

        // Şifre Kutusu
        txtSifre = new JPasswordField();
        txtSifre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSifre.setBounds(160, 145, 200, 30);
        contentPane.add(txtSifre);

        // GİRİŞ YAP BUTONU
        JButton btnGiris = new JButton("GİRİŞ YAP");
        btnGiris.setFont(new Font("Arial", Font.BOLD, 14));
        btnGiris.setBackground(new Color(60, 179, 113));
        btnGiris.setForeground(Color.RED);
        btnGiris.setBounds(160, 200, 200, 40);
        contentPane.add(btnGiris);

        
        btnGiris.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                girisKontrol();
            }
        });
    }

    private void girisKontrol() {
        String kadi = txtKullaniciAdi.getText();
        String sifre = new String(txtSifre.getPassword());

        try {
            Connection conn = Baglanti.getBaglanti();
            String sql = "SELECT * FROM kullanicilar WHERE kullanici_adi=? AND sifre=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kadi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                JOptionPane.showMessageDialog(null, "Giriş Başarılı! Hoşgeldiniz: " + rol);
                dispose();
                AnaSayfa anaSayfa = new AnaSayfa(rol);
                anaSayfa.setVisible(true);
                
              
            } else {
                JOptionPane.showMessageDialog(null, "Hatalı Kullanıcı Adı veya Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage());
        }
    }
}