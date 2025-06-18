# 📱 AccessoriesBox - Aplikasi Mobile E-Commerce

**AccessoriesBox** adalah aplikasi e-commerce mobile berbasis Android yang dibangun menggunakan **Jetpack Compose** dan arsitektur **MVVM (Model-View-ViewModel)**. Aplikasi ini memungkinkan pengguna untuk mendaftar, login, melihat daftar produk, serta bagi penjual untuk mengelola brand dan produk mereka.

---

## 🧰 Teknologi yang Digunakan

- Android Studio
- Kotlin (bahasa utama)
- Jetpack Compose (UI modern)
- MVVM Architecture
- Retrofit (HTTP Client)
- Room (SQLite database)
- Hilt (Dependency Injection)
- Flask (Backend API)
- PostgreSQL (Server-side database)

---

## 📱 Fitur Aplikasi

### Pengguna Umum (Customer)
- Register & Login dengan validasi
- Pencarian produk berdasarkan nama
- Lihat daftar dan detail produk

### Penjual (Seller)
- Membuat dan mengelola brand sendiri
- Menambahkan produk dan melihat produk miliknya

---

## 🚀 Cara Menjalankan Proyek Android + Flask (Urut & Lengkap)

---

### 1. Clone Repository Backend (Flask)
```
git clone https://github.com/mwph6d4/BE_MOBILE_TEAM
cd BE_MOBILE_TEAM
```

#### a. Jalankan server Flask
File utama bernama `run.py`, jalankan:
```
python run.py
```

Jika berhasil:
```
 * Running on http://127.0.0.1:5000/
```

#### b. Cek IP server backend untuk Android
- **Emulator:** gunakan `http://10.0.2.2:5000/`
- **HP asli:** cari IP lokal laptop:
  - `ipconfig` (Windows) atau `ifconfig` (Linux/Mac)
  - Misal IP laptop: `192.168.1.5` → Android akses: `http://192.168.1.5:5000/`

---

### 2. Clone Repository Android
```bash
git clone https://github.com/mwph6dp/FE_MOBILE_TEAM
cd BE_MOBILE_TEAM
```

#### a. Buka di Android Studio
- Jalankan Android Studio
- Klik **"Open"** lalu pilih folder `BE_MOBILE_TEAM`
- Tunggu sampai **Gradle sync** selesai

---

#### b. Atur BASE_URL API di Android
- Buka file `ApiClient.kt` 
- Ubah BASE_URL sesuai IP backend:

---

#### c. Jalankan Aplikasi Android
- Hubungkan emulator atau HP ke laptop
- Klik tombol **Run (▶️)** di Android Studio
- Aplikasi akan terkoneksi ke backend Flask

---


