package vn.edu.uit.da2.exchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Ten dang nhap khong duoc de trong")
    @Size(min = 3, max = 50,
          message = "Ten dang nhap phai tu 3 den 50 ky tu")
    private String username;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    @Size(max = 150, message = "Email qua dai")
    private String email;

    @NotBlank(message = "Mat khau khong duoc de trong")
    @Size(min = 8, message = "Mat khau phai co it nhat 8 ky tu")
    private String password;

    @Size(max = 150, message = "Ho ten qua dai")
    private String fullName;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
