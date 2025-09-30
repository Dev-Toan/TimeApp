# N05_QuanLyThoiGian — Ứng dụng Quản lý Thời gian (Android)

Ứng dụng Android giúp quản lý công việc, theo dõi tiến độ và tối ưu hoá thời gian học tập/làm việc. Dự án phục vụ bài tập môn học, có thể mở rộng cho nhu cầu thực tế.

## Tính năng chính
- Tạo/sửa/xoá công việc, nhóm công việc
- Theo dõi tiến độ, thời lượng thực hiện
- Lịch/nhắc việc (notification)
- Tổng hợp báo cáo theo ngày/tuần/tháng

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
