# N05_QuanLyThoiGian — Ứng dụng Quản lý Thời gian (Android)

Ứng dụng Android giúp quản lý công việc, theo dõi tiến độ và tối ưu hoá thời gian học tập/làm việc. Dự án phục vụ bài tập môn học, có thể mở rộng cho nhu cầu thực tế.

## Tính năng chính
- Tạo/sửa/xoá công việc, nhóm công việc
- Theo dõi tiến độ, thời lượng thực hiện
- Lịch/nhắc việc (notification)
- Tổng hợp báo cáo theo ngày/tuần/tháng
  <img width="539" height="985" alt="image" src="https://github.com/user-attachments/assets/98687020-9778-4432-917f-3d57a80ffed2" />
  <img width="540" height="980" alt="image" src="https://github.com/user-attachments/assets/1c0d51bf-8a31-4d63-8aed-c2160f431dba" />
  <img width="545" height="987" alt="image" src="https://github.com/user-attachments/assets/f28a22ce-3fb6-4493-a7cb-1b022b851cad" />
  <img width="543" height="988" alt="image" src="https://github.com/user-attachments/assets/8fe3e171-671d-41d9-bf73-635aba35fd0a" />
  <img width="542" height="984" alt="image" src="https://github.com/user-attachments/assets/3bd128d8-b6d6-4e54-b4fa-03116cc5e81c" />
  <img width="544" height="990" alt="image" src="https://github.com/user-attachments/assets/4b9d165b-5e00-424c-b27d-e2dc3460451e" />
  <img width="546" height="987" alt="image" src="https://github.com/user-attachments/assets/e371befd-5dff-46e6-ab4e-2e0c13503a34" />
  <img width="544" height="982" alt="image" src="https://github.com/user-attachments/assets/a6a66003-8235-4a41-9cfc-beea1286802c" />
  <img width="538" height="984" alt="image" src="https://github.com/user-attachments/assets/4a76add6-82da-4163-836f-cecc69d7160a" />
  <img width="540" height="984" alt="image" src="https://github.com/user-attachments/assets/2589ad8e-5581-4f17-90aa-544309964b38" />











## Yêu cầu hệ thống
- Android Studio (Giraffe/Koala hoặc mới hơn)
- JDK 17 (theo yêu cầu phiên bản Android Gradle Plugin)
- Android SDK, Android Emulator hoặc thiết bị thật (API phù hợp)

## Cài đặt & chạy
1) Mở Android Studio → Open → chọn thư mục dự án `N05_QuanLyThoiGian`
2) Đợi Gradle sync hoàn tất
3) Chọn cấu hình chạy (device/emulator) → Run

Hoặc dòng lệnh (Windows):
```bash
./gradlew.bat assembleDebug
```
APK debug sẽ nằm tại `app/build/outputs/apk/debug/app-debug.apk`.

## Cấu trúc thư mục (chính)
```
app/
├─ src/main/
│  ├─ java/com/...     # Mã nguồn Kotlin/Java
│  ├─ res/             # Tài nguyên giao diện
│  └─ AndroidManifest.xml
├─ build.gradle.kts     # Cấu hình module app
gradle/                 # Wrapper và cấu hình Gradle
settings.gradle.kts     # Thiết lập multi-module
build.gradle.kts        # Cấu hình cấp project
```

## Kiểm thử, chất lượng mã (tuỳ chỉnh theo dự án)
```bash
./gradlew.bat test
./gradlew.bat lint
```

## Đóng góp
- Tạo nhánh từ `main`
- Commit message rõ ràng, gọn
- Mở Pull Request kèm mô tả, ảnh minh hoạ khi cần

## Giấy phép
Xem file `LICENSE` trong thư mục gốc dự án.
