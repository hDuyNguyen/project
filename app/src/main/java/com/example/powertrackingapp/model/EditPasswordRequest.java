package com.example.powertrackingapp.model;

public class EditPasswordRequest {
    private String token;
    private String realDeviceId;
    private String password;
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealDeviceId() {
        return realDeviceId;
    }

    public void setRealDeviceId(String realDeviceId) {
        this.realDeviceId = realDeviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
