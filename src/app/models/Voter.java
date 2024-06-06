package app.models;

public class Voter {
    public String name;
    private String cnic;
    private String pin;
    public int age;

    public Voter(String name, String cnic, String pin, int age) {
        this.name = name;
        this.cnic = cnic;
        this.pin = pin;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getCnic() {
        return cnic;
    }

    public String getPin() {
        return pin;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
