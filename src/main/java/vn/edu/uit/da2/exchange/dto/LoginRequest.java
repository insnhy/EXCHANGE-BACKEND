package vn.edu.uit.da2.exchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Vui long nhap username hoac email")
    @Size(max = 150)
    private String usernameOrEmail;

    @NotBlank(message = "Mat khau khong duoc de trong")
    private String password;

    public LoginRequest() {
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
