# EXCHANGE-BACKEND

**Đồ án 2 – Xây dựng website trao đổi và thanh lý đồ dùng đã qua sử dụng tích hợp hệ thống gợi ý thông minh**

## 1. Giới thiệu

`EXCHANGE-BACKEND` là phần Backend của website trao đổi và thanh lý đồ dùng đã qua sử dụng. Hệ thống hướng đến việc hỗ trợ người dùng đăng tin sản phẩm, tìm kiếm đồ dùng phù hợp và thực hiện các nghiệp vụ mua hoặc trao đổi sản phẩm.

Dự án được phát triển trong khuôn khổ Đồ án 2 tại Trường Đại học Công nghệ Thông tin – ĐHQG TP.HCM (UIT).

## 2. Công nghệ sử dụng

- **Ngôn ngữ:** Java 21
- **Backend framework:** Spring Boot
- **Quản lý thư viện và build:** Maven
- **Cơ sở dữ liệu:** PostgreSQL
- **ORM:** Spring Data JPA, Hibernate
- **Bảo mật:** Spring Security, JWT
- **Mã hóa mật khẩu:** BCrypt
- **Kiểm thử API:** Trình duyệt và PowerShell

## 3. Các chức năng đã triển khai

### 3.1. Quản lý tài khoản và xác thực

- Đăng ký tài khoản.
- Đăng nhập và cấp JWT.
- Xác thực yêu cầu bằng JWT.
- Mã hóa mật khẩu bằng BCrypt.

### 3.2. Quản lý danh mục

- Lấy danh sách danh mục.
- Tạo danh mục mới.
- Cập nhật thông tin danh mục.
- Xóa danh mục khi không có sản phẩm tham chiếu.
- Kiểm tra dữ liệu đầu vào và tên danh mục trùng lặp.

### 3.3. Quản lý sản phẩm

- Đăng tin sản phẩm.
- Xem danh sách sản phẩm đang khả dụng.
- Xem chi tiết sản phẩm.
- Cập nhật thông tin sản phẩm.
- Ẩn tin đăng.
- Tìm kiếm theo từ khóa.
- Lọc sản phẩm theo danh mục.
- Sắp xếp theo giá, tiêu đề hoặc thời gian đăng.
- Kiểm tra quyền sở hữu khi cập nhật hoặc ẩn sản phẩm.

### 3.4. Quản lý hình ảnh

- Tải ảnh lên sản phẩm.
- Lưu thông tin hình ảnh và đường dẫn trong bảng `product_images`.
- Lưu file ảnh trong thư mục upload của ứng dụng.
- Kiểm tra định dạng và dung lượng file.
- Kiểm tra quyền sở hữu khi tải ảnh lên sản phẩm.

### 3.5. Kiểm thử

- Kiểm tra quá trình biên dịch bằng Maven.
- Kiểm thử các API và kiểm tra dữ liệu trong PostgreSQL.
- Kiểm thử quyền sở hữu bằng hai tài khoản.
- Xác nhận hệ thống từ chối một số thao tác trái quyền bằng HTTP 403 Forbidden.

## 4. Cấu trúc dự án

```text
exchange-backend/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/vn/edu/uit/da2/exchange/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## 5. Yêu cầu môi trường

Trước khi chạy dự án, cần chuẩn bị:

- JDK 21 trở lên.
- PostgreSQL.
- Git (nếu muốn lấy mã nguồn bằng cách clone repository).

Maven Wrapper được cung cấp cùng dự án, vì vậy có thể sử dụng `mvnw.cmd` trên Windows mà không cần cài Maven riêng.

## 6. Cấu hình cơ sở dữ liệu

Tạo cơ sở dữ liệu PostgreSQL với tên:

```sql
CREATE DATABASE used_goods_db;
```

Ứng dụng hiện được cấu hình để kết nối đến:

| Thuộc tính | Giá trị |
|---|---|
| Database | `used_goods_db` |
| Host | `localhost` |
| Port | `5432` |
| Username | `exchange_user` |
| Password | Lấy từ biến môi trường `DB_PASSWORD` |

Nếu chưa có tài khoản PostgreSQL `exchange_user`, hãy tạo tài khoản và cấp quyền truy cập cơ sở dữ liệu theo cấu hình môi trường của bạn.

## 7. Cấu hình biến môi trường

Ứng dụng sử dụng biến môi trường để cung cấp thông tin kết nối cơ sở dữ liệu và JWT secret.

Trên Windows PowerShell, thiết lập hai biến trong phiên Terminal chạy ứng dụng:

```powershell
$env:DB_PASSWORD = "YOUR_DATABASE_PASSWORD"
$env:JWT_SECRET = "YOUR_LONG_RANDOM_SECRET"
```

Thay các giá trị mẫu bằng thông tin cấu hình của môi trường chạy thực tế.

**Lưu ý:** Không commit mật khẩu, JWT secret hoặc access token thật lên repository công khai.

## 8. Cách chạy Backend

Clone repository:

```powershell
git clone https://github.com/insnhy/EXCHANGE-BACKEND.git
cd EXCHANGE-BACKEND
```

Sau khi cấu hình PostgreSQL và các biến môi trường, chạy ứng dụng:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend sử dụng cổng `8080` theo cấu hình hiện tại.

Để biên dịch và kiểm tra dự án:

```powershell
.\mvnw.cmd clean compile
```

## 9. Các API chính

Các API được tổ chức theo nhóm chức năng như sau:

| Phương thức | Endpoint | Chức năng |
|---|---|---|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập |
| GET | `/api/categories` | Lấy danh sách danh mục |
| POST | `/api/categories` | Tạo danh mục |
| PUT | `/api/categories/{categoryId}` | Cập nhật danh mục |
| DELETE | `/api/categories/{categoryId}` | Xóa danh mục |
| GET | `/api/products` | Lấy danh sách sản phẩm |
| GET | `/api/products/{productId}` | Xem chi tiết sản phẩm |
| GET | `/api/products/search` | Tìm kiếm, lọc và sắp xếp |
| POST | `/api/products` | Đăng sản phẩm |
| PUT | `/api/products/{productId}` | Cập nhật sản phẩm |
| DELETE | `/api/products/{productId}` | Ẩn tin đăng |
| POST | `/api/products/{productId}/images` | Tải ảnh lên sản phẩm |

Các thao tác ghi dữ liệu được bảo vệ bằng JWT theo cấu hình bảo mật hiện tại. Quyền truy cập cụ thể cần tuân theo cấu hình Security và kiểm tra quyền sở hữu trong từng nghiệp vụ.

### Ví dụ tìm kiếm và sắp xếp

Tìm sản phẩm có từ khóa `Java`:

```text
GET /api/products/search?keyword=Java
```

Sắp xếp theo giá giảm dần:

```text
GET /api/products/search?sortBy=price&direction=desc
```

Lọc theo danh mục:

```text
GET /api/products/search?categoryId=YOUR_CATEGORY_UUID
```

## 10. Trạng thái phát triển

Backend đã triển khai các chức năng nền tảng về xác thực, quản lý danh mục, quản lý sản phẩm và hình ảnh.

Các giai đoạn tiếp theo tập trung vào:

- Phát triển giao diện Frontend bằng ReactJS.
- Tích hợp giao diện với các API Backend.
- Hoàn thiện nghiệp vụ đề nghị mua và đề nghị trao đổi.
- Phát triển và đánh giá thuật toán Smart Exchange Matching.
- Kiểm thử tích hợp và hoàn thiện hệ thống.

## 11. Repository

GitHub: https://github.com/insnhy/EXCHANGE-BACKEND

---

*Dự án được thực hiện phục vụ mục đích học tập và nghiên cứu trong khuôn khổ Đồ án 2.*