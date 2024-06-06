package app.models;

public class Admin {
    private String adminID;
    private String pin;
    private String adminName;

    public Admin(String adminID, String pin, String adminName) {
        this.adminID = adminID;
        this.pin = pin;
        this.adminName = adminName;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
