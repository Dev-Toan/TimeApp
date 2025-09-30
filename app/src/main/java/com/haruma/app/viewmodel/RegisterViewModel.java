package com.haruma.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.BR;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.model.User;
import com.haruma.app.service.ExceptionHelper;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterViewModel extends BaseObservable {

    private String email;

    private String password;
    private String fullName;
    private String className;

    private String phoneNumber;

    private final Context context;

    private final Map<String, Callback> callback;

    private DatabaseHelper databaseHelper;

    public RegisterViewModel(Context context, Map<String, Callback> callback) {
        this.context = context;
        this.callback = callback;
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        notifyPropertyChanged(BR.className);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }
    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    private void makeToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

    public void onLogin() {
        try {
            Objects.requireNonNull(this.callback.get("onLogin")).run();
        } catch (Exception e) {
            makeToast("Lỗi xảy ra: " + e.getMessage());
        }
    }

    private boolean validateEmailAndPassword(String email, String password) throws Exception {
        if (password == null || password.isEmpty()) {
            throw new Exception("Mật khẩu không được rỗng");
        }
        if (email == null || email.isEmpty()) {
            throw new Exception("Email không được rỗng");
        }
        if (password.length() < 6) {
            throw new Exception("Mật khẩu không được ít hơn 6 kí tự");
        }
        if (!Pattern.compile("^.+@.+\\..+$").matcher(email).matches()) {
            throw new Exception("Email không hợp lệ");
        }
        return true;
    }

    private void showRegistrationError(String errorMessage) {
        Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show();
    }
    private boolean isEmailExists(String email) {
        // Giả sử bạn có một phương thức trong DatabaseHelper để kiểm tra email
        return this.databaseHelper.isEmailExists(email);
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra xem số điện thoại có phải là 10 chữ số hay không
        if (phoneNumber != null && phoneNumber.length() == 10) {
            try {
                Long.parseLong(phoneNumber); // Kiểm tra nếu là số
                return true;
            } catch (NumberFormatException e) {
                // Nếu không phải là số hợp lệ
                return false;
            }
        }
        return false; // Trả về false nếu không phải 10 chữ số
    }
    public void onRegister() {
        try {
            User user = new User(this.getEmail(), this.getPassword());
            if (this.getEmail() == null || this.getEmail().isEmpty()) {
                showRegistrationError("Email không được rỗng");
                return;
            }
            if (!Pattern.compile("^.+@.+\\..+$").matcher(this.getEmail()).matches()) {
                showRegistrationError("Email không hợp lệ");
                return;
            }
            if (isEmailExists(this.getEmail())) {
                showRegistrationError("Email đã tồn tại");
                return;
            }
            if (this.getPassword() == null || this.getPassword().isEmpty()) {
                showRegistrationError("Mật khẩu không được rỗng");
                return;
            }
            if (this.getPassword().length() < 6) {
                showRegistrationError("Mật khẩu không được ít hơn 6 kí tự");
                return;
            }

            if (this.getFullName() == null || this.getFullName().isEmpty()) {
                showRegistrationError("Họ và tên không được rỗng");
                return;}

            if (this.getClassName() == null || this.getClassName().isEmpty()) {
                showRegistrationError("Lớp không được rỗng");
                return;}

            if (this.getPhoneNumber() == null || this.getPhoneNumber().isEmpty()) {
                showRegistrationError("Số điện thoại không được rỗng");
                return;}


            if (!isValidPhoneNumber(this.getPhoneNumber())) {
                showRegistrationError("Số điện thoại phải có 10 số");
                return;
            }
            if (this.validateEmailAndPassword(user.getEmail(), user.getPassword())) {
                makeToast("Đăng ký thành công");
                boolean state = this.databaseHelper.registerUser(user.getEmail(), user.getPassword(), this.getFullName(), this.getClassName(), this.getPhoneNumber());
                ExceptionHelper.assertTest(state, "Đăng ký tài khoản thất bại");
                Objects.requireNonNull(this.callback.get("onRegister")).run();
            }
            else {
                makeToast("Đăng ký thất bại");
            }
        } catch (Exception e) {
            makeToast("Lỗi xảy ra: " + e.getMessage());
        }
    }

}
