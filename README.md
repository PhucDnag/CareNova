# BTL Android - Uni Care

`BTL_Android` là một ứng dụng Android dùng để hỗ trợ quản lý y tế cho bệnh nhân và bác sĩ. Dự án được xây dựng bằng **Java**, sử dụng **AndroidX**, **Material Components**, và lưu trữ dữ liệu cục bộ bằng **SQLite** thông qua `DatabaseHelper`.

## Tổng quan

Ứng dụng có các nhóm chức năng chính:

- Đăng nhập theo vai trò: bệnh nhân / bác sĩ
- Trang chủ hiển thị các dịch vụ y tế nổi bật
- Đặt lịch khám và xem lịch hẹn
- Quản lý hồ sơ cá nhân
- Theo dõi lịch sử khám bệnh và bệnh án
- Quản lý phác đồ điều trị
- Thông báo, nhắc nhở và các tiện ích liên quan
- Hỗ trợ đổi ngôn ngữ `Tiếng Việt` / `English`

## Công nghệ sử dụng

- **Ngôn ngữ**: Java
- **UI**: XML Layout, Material Design
- **Kiến trúc**: Activity / Fragment / Adapter theo từng màn hình
- **Lưu trữ dữ liệu**: SQLite nội bộ qua `DatabaseHelper`
- **Xử lý ảnh**: Glide
- **Tải dữ liệu web / API**: Jsoup, OkHttp, JSON
- **Đa ngôn ngữ**: `values/strings.xml` và `values-vi/strings.xml`

## Cấu trúc thư mục chính

```text
app/
├── src/main/java/com/example/btl_android/
│   ├── Adapter/              # RecyclerView adapters
│   ├── Database/             # SQLite helper và truy vấn dữ liệu
│   ├── Object/               # Model / entity classes
│   ├── ...Activity.java      # Các màn hình chức năng
│   ├── ...Fragment.java      # Các fragment UI
│   └── LanguageManager.java  # Xử lý đổi ngôn ngữ
├── src/main/res/
│   ├── layout/               # Giao diện XML
│   ├── drawable/             # Icon, shape, image resource
│   ├── values/               # String mặc định, màu, style
│   └── values-vi/            # Chuỗi tiếng Việt
```

## Cách lưu trữ dữ liệu

Dự án không dùng Firebase hay server riêng cho dữ liệu nghiệp vụ chính. Dữ liệu được lưu **cục bộ trên thiết bị** bằng SQLite:

- `DatabaseHelper` tạo bảng và quản lý truy vấn.
- Các model trong `app/src/main/java/com/example/btl_android/Object/` đại diện cho từng thực thể như:
  - bệnh nhân
  - lịch hẹn
  - thông báo
  - bệnh án
  - phác đồ điều trị
- Các `Adapter` hiển thị danh sách dữ liệu lên `RecyclerView`.

### Luồng lưu trữ điển hình

1. Người dùng nhập dữ liệu từ màn hình UI.
2. Activity / Fragment gọi `DatabaseHelper` để thêm hoặc cập nhật dữ liệu.
3. Dữ liệu được đọc lại từ SQLite.
4. Adapter nhận danh sách và render lên giao diện.

## Cách hoạt động

### 1. Điều hướng màn hình

- Mỗi chức năng chính được tách thành một `Activity` hoặc `Fragment` riêng.
- Từ màn hình chính, người dùng đi đến các màn hình chi tiết như lịch hẹn, hồ sơ, bệnh án, phác đồ, thông báo, nhắc nhở...

### 2. Phân quyền theo vai trò

- Ứng dụng có luồng riêng cho **bệnh nhân** và **bác sĩ**.
- Tùy vai trò, giao diện và chức năng sẽ khác nhau.

### 3. Đa ngôn ngữ

- Chuỗi tiếng Anh nằm trong `app/src/main/res/values/strings.xml`
- Chuỗi tiếng Việt nằm trong `app/src/main/res/values-vi/strings.xml`
- `LanguageManager` dùng `AppCompatDelegate.setApplicationLocales(...)` để đổi locale và khởi động lại ứng dụng.

### 4. Dữ liệu động

- Các danh sách như lịch hẹn, thông báo, bệnh án được hiển thị bằng `RecyclerView`.
- Adapter chịu trách nhiệm bind dữ liệu từ model sang UI.

## Cấu hình dự án

### Yêu cầu môi trường

- **Android Studio** mới
- **JDK 11**
- **Android SDK 36**
- **Min SDK**: 24

### Build script

- Dự án dùng Gradle Kotlin DSL:
  - `build.gradle.kts`
  - `app/build.gradle.kts`
- Dependency versions được quản lý trong `gradle/libs.versions.toml`

### Biến môi trường / API key

Dự án có khai báo:

- `GROQ_API_KEY`

Giá trị này được lấy từ Gradle property:

```properties
GROQ_API_KEY=your_api_key_here
```

Nếu không có key, ứng dụng vẫn có thể build, nhưng những tính năng cần API này có thể không hoạt động đầy đủ.

## Chạy dự án

1. Clone repository về máy.
2. Mở bằng Android Studio.
3. Đồng bộ Gradle.
4. Nếu cần, khai báo `GROQ_API_KEY` trong file `gradle.properties`.
5. Chạy `app` trên emulator hoặc thiết bị thật.

## Ghi chú cho GitHub

Khi đẩy lên GitHub, nên:

- Không commit các file build tạm thời trong `app/build/`
- Không commit thư mục `.gradle/`
- Không commit file `.idea/workspace.xml` nếu không cần thiết
- Không đưa API key thật lên repository công khai

### Nên giữ lại

- `app/src/main/**`
- `gradle/**`
- `build.gradle.kts`
- `settings.gradle.kts`
- `README.md`
- `proguard-rules.pro`

## Tình trạng hiện tại

Dự án đang được phát triển thêm các phần:

- hoàn thiện chuyển ngôn ngữ đồng bộ hơn
- chuẩn hóa dữ liệu hiển thị theo locale
- tối ưu giao diện một số màn hình

## Tác giả

Hồng Phúc
