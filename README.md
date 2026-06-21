# 🏨 Ứng dụng Quản lý Khách Sạn

## 📋 Mô tả dự án
Ứng dụng Java Desktop quản lý khách sạn đầy đủ với kiến trúc Client-Server, hỗ trợ:
- Quản lý phòng, khách hàng, đặt phòng
- Hệ thống đăng nhập với phân quyền (Admin, Receptionist, Manager)
- Bảo mật: Hash password (BCrypt), mã hoá dữ liệu nhạy cảm (AES)
- Import/Export dữ liệu (XML, CSV)
- Thread Pool xử lý nhiều client đồng thời
- Ghi log hoạt động
- Dashboard thống kê
- Phân trang dữ liệu

---

## 🛠️ Công nghệ sử dụng
- **Java 11+**
- **MySQL (XAMPP)**
- **Java Swing** (giao diện desktop)
- **Socket TCP/IP** (client-server communication)
- **JDBC + DAO Pattern**
- **BCrypt** (password hashing)
- **AES** (data encryption)
- **Log4j** (logging)

---

## 📁 Cấu trúc Project
```
hotel-management/
├── server-app/
│   └── src/main/java/com/hotel/server/
│       ├── Server.java                 # Main server
│       ├── ClientHandler.java          # Xử lý client
│       ├── dao/                        # Data Access Object
│       ├── service/                    # Business logic
│       ├── model/                      # Entity models
│       ├── security/                   # Bảo mật
│       └── util/                       # Tiện ích
│
├── client-app/
│   └── src/main/java/com/hotel/client/
│       ├── ClientMain.java             # Main client
│       ├── ui/                         # Giao diện Swing
│       ├── service/                    # Client service
│       ├── model/                      # DTO models
│       └── util/                       # Tiện ích
│
├── database/
│   └── hotel_management.sql            # SQL schema
│
└── README.md

```

---

## 🚀 Hướng dẫn cài đặt

### 1️⃣ Chuẩn bị Database
**Yêu cầu:** Đã cài XAMPP với MySQL chạy

```bash
# Bước 1: Truy cập XAMPP Control Panel
# - Bấm "Start" cho MySQL

# Bước 2: Mở phpMyAdmin
# Truy cập: http://localhost/phpmyadmin

# Bước 3: Tạo database
# - Bấm "New"
# - Tên database: hotel_management
# - Collation: utf8mb4_unicode_ci

# Bước 4: Import SQL
# - Chọn tab "SQL"
# - Copy nội dung từ database/hotel_management.sql
# - Paste vào rồi bấm "Go"
```

### 2️⃣ Cấu hình Database Connection
**File:** `server-app/src/main/java/com/hotel/server/util/DatabaseConnection.java`

```java
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_management";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mật khẩu XAMPP (thường để trống)
    // ...
}
```

### 3️⃣ Chạy Server
```bash
cd server-app
# Biên dịch
javac -d bin src/main/java/com/hotel/server/*.java

# Chạy
java -cp bin com.hotel.server.Server
```

**Output mong đợi:**
```
[2026-05-15 10:30:22] Server started on port 9000
[2026-05-15 10:30:22] Waiting for clients...
```

### 4️⃣ Chạy Client (mở terminal khác)
```bash
cd client-app
# Biên dịch
javac -d bin src/main/java/com/hotel/client/*.java

# Chạy
java -cp bin com.hotel.client.ClientMain
```

---

## 🔐 Tài kho��n đăng nhập mẫu

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | Admin | admin@hotel.com |
| receptionist1 | rec123 | Receptionist | receptionist@hotel.com |
| manager1 | mgr123 | Manager | manager@hotel.com |

**Ghi chú:** Password được hash bằng BCrypt trong database

---

## ✨ Các chức năng chính

### 1. 🔑 Đăng nhập & Phân quyền
- ✅ Đăng nhập an toàn (hash password)
- ✅ Phân quyền 3 loại (Admin, Receptionist, Manager)
- ✅ Khoá tài khoản sau 5 lần đăng nhập sai

### 2. 🚪 Quản lý Phòng (CRUD)
- ✅ Thêm/Sửa/Xoá phòng
- ✅ Xem danh sách phòng theo loại (Single, Double, Suite)
- ✅ Xem trạng thái phòng (Trống, Đang dùng, Bảo trì)
- ✅ Tìm kiếm phòng
- ✅ Sắp xếp theo giá, loại

### 3. 👥 Quản lý Khách hàng (CRUD)
- ✅ Thêm/Sửa/Xoá khách hàng
- ✅ Xem danh sách khách hàng
- ✅ Tìm kiếm theo tên, CMND, SĐT
- ✅ Mã hoá: Số điện thoại, Địa chỉ, CMND

### 4. 📅 Quản lý Đặt phòng (CRUD)
- ✅ Tạo đặt phòng (Transaction: cập nhật trạng thái phòng)
- ✅ Check-in, Check-out
- ✅ Xem lịch sử đặt phòng
- ✅ Tìm kiếm theo khách, phòng, ngày

### 5. 🍽️ Quản lý Dịch vụ (CRUD)
- ✅ Thêm/Sửa/Xoá dịch vụ
- ✅ Xem danh sách dịch vụ
- ✅ Gán dịch vụ cho đặt phòng

### 6. 💰 Quản lý Hóa đơn
- ✅ Tạo hóa đơn từ đặt phòng + dịch vụ
- ✅ Xem danh sách hóa đơn
- ✅ Tính tổng tiền tự động

### 7. 📊 Dashboard Thống kê
- ✅ Tổng doanh thu (ngày, tháng, năm)
- ✅ Tỷ lệ chiếm phòng (occupancy rate)
- ✅ Số khách hôm nay
- ✅ Phòng sắp hết hạn check-out
- ✅ Biểu đồ doanh thu

### 8. 📤 Import/Export
- ✅ Export danh sách phòng → XML/CSV
- ✅ Export danh sách khách → XML/CSV
- ✅ Export hóa đơn → XML/CSV
- ✅ Import dữ liệu từ file
- ✅ Kiểm tra lỗi định dạng

### 9. 📝 Ghi log hoạt động
- ✅ Log đăng nhập/đăng xuất
- ✅ Log CRUD operations
- ✅ Log import/export
- ✅ Log thanh toán
- ✅ Lưu vào database + file text

### 10. 🔒 Bảo mật
- ✅ Hash password BCrypt
- ✅ Mã hoá AES: SĐT, Email, Địa chỉ, CMND
- ✅ Phân quyền truy cập
- ✅ Không hiển thị sensitive data bừa bãi

---

## 🧵 Thread & Performance
- ✅ Server dùng **ExecutorService (ThreadPool)** xử lý 10+ client cùng lúc
- ✅ Client dùng **SwingWorker** để giao diện không treo
- ✅ Hỗ trợ phân trang dữ liệu (20 dòng/trang)

---

## 📦 Thư viện sử dụng
```xml
<!-- Trong pom.xml (nếu dùng Maven) -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>

<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

---

## ✅ Checklist hoàn thành

- [x] Giao diện Swing (Login, Main, Dashboard)
- [x] Đăng nhập + Phân quyền
- [x] 5 CRUD chính (Phòng, Khách, Đặt phòng, Dịch vụ, Hóa đơn)
- [x] Client-Server Socket TCP/IP
- [x] Thread Pool (10 clients)
- [x] Database MySQL (XAMPP)
- [x] DAO Pattern + JDBC
- [x] Hash Password (BCrypt)
- [x] Mã hoá dữ liệu (AES)
- [x] Import/Export (XML, CSV)
- [x] Tìm kiếm/Lọc/Sắp xếp
- [x] Ghi log hoạt động
- [x] Dashboard thống kê
- [x] Phân trang dữ liệu
- [x] Transaction (đặt phòng)
- [x] SwingWorker (giao diện không treo)

---

## 🐛 Gỡ lỗi
Nếu gặp lỗi:
1. **Kết nối Database thất bại**
   - Kiểm tra MySQL chạy trong XAMPP
   - Kiểm tra hostname, port (3306)
   - Kiểm tra database name: `hotel_management`

2. **Server không kết nối được**
   - Kiểm tra port 9000 không bị sử dụng
   - Firewall có chặn không?

3. **Import/Export lỗi**
   - Kiểm tra file định dạng XML/CSV đúng
   - Kiểm tra quyền ghi file

---

## 📞 Liên hệ & Hỗ trợ
Nếu có vấn đề, kiểm tra:
- Log file: `logs/system.log`
- Database connection
- Port 9000 available

---

**Phiên bản:** 1.0  
**Ngày cập nhật:** 2026-05-15  
**Tác giả:** Nhóm phát triển
