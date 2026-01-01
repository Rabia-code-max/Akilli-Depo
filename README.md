# Akıllı Depo Otomasyon Sistemi

Bu proje, Java ve MySQL kullanılarak geliştirilmiş kapsamlı bir **Depo Stok Takip ve Sipariş Otomasyonu**dur. Nesne Tabanlı Programlama (OOP) prensiplerine uygun olarak; Yönetici, Müşteri ve Depo Çalışanı olmak üzere üç farklı kullanıcı rolü üzerine kurgulanmıştır.

## Projenin Amacı
Bir deponun stok durumunu anlık olarak takip etmek, azalan ürünler için otomatik sipariş oluşturmak ve müşterilerin kurallı adres girişleriyle sipariş vermesini sağlamaktır.

## Kullanılan Teknolojiler
* **Dil:** Java (JDK 21)
* **Arayüz (GUI):** Java Swing (WindowBuilder)
* **Veritabanı:** MySQL
* **Bağlantı:** JDBC (MySQL Connector)
* **IDE:** Eclipse

## Özellikler
### 1. Yönetici (Admin) Modülü
* Yeni ürün ekleyebilir (Ürün Adı, Stok, Raf Yeri vb.).
* ID numarası ile ürün silebilir.
* Tüm stok hareketlerini görebilir.

### 2. Müşteri (Customer) Modülü
* Ürünleri listeleyip sepete ekleyebilir.
* **Stok Kontrolü:** Stoktan fazla ürün eklenmesi engellenir.
* **Akıllı Telefon ve Adres Kontrolü:**
    * Telefon numarası +90 formatında ve 10 haneli olmalıdır.
    * Adres içinde "Mahalle", "Cadde / Sokak", "Bina No", "Daire No", "Ilçe" gibi ibareler zorunludur.
    * Sistem sadece **İstanbul** içi siparişleri kabul eder (Türkçe/İngilizce karakter duyarlılığı çözülmüştür).
* **Otomatik Sipariş:** Müşteri satın alım yaptığında stok düşer. Eğer stok kritik seviyenin (Min Stok) altına inerse, sistem otomatik olarak depoyu maksimum seviyeye tamamlayacak siparişi verir.

### 3. Depo Çalışanı (Worker) Modülü
* Anlık stok listesini yenileyebilir.
* **Kritik Stok Raporu:** Tek tuşla stoğu azalan ürünleri analiz edip listeler.

## Kurulum
1.  **Veritabanı:** * MySQL Workbench veya benzeri bir araçta `akilli_depo` isminde bir veritabanı oluşturun.
    * Proje içindeki SQL kodlarını çalıştırarak tabloları ve `UrunEkleme` prosedürünü oluşturun.
2.  **Bağlantı Ayarı:**
    * `src/tr/com/akillidepo/veritabani/Baglanti.java` dosyasındaki `KULLANICI_ADI` ve `SIFRE` alanlarını kendi MySQL bilgilerinizle güncelleyin.
3.  **Çalıştırma:**
    * `GirisEkrani.java` dosyasını çalıştırarak uygulamayı başlatın.

## Giriş Bilgileri

|     Rol      | Kullanıcı Adı | Şifre  |
| **Yönetici** |   `müdür`     | `2025` |
| **Çalışan**  |  `calisan`    | `2025` |
| **Müşteri**  |  `musteri`    | `2025` |


---
*Bu proje, Nesne Tabanlı Programlama dersi kapsamında geliştirilmiştir.*

### 🎥 Proje Tanıtım Videosu
[Videoyu İzlemek İçin Tıklayın](https://youtu.be/hQYeJPAXSrQ?si=ZukbsYyHtyS3zA6j)


Project :
https://github.com/Rabia-code-max/Akilli-Depo
Video :
https://www.youtube.com/watch?v=hQYeJPAXSrQ

Bu projeye katılanlar :
1-Rabia İLHAN 
2-Mohamed Ramadan Darwesh Ebrahem Darwesh
3-Sevda TANACI 
4-Can Efe Kaya

