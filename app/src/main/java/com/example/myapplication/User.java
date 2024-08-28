package com.example.myapplication;

public class User {
    public String name, age, email, profile_Image, balance, debt, shops;

    public User(){
    }

    public User(String name, String age, String email, String profile_Image, String balance, String debt, String shops){
        this.name = name;
        this.age = age;
        this.email = email;
        this.profile_Image = profile_Image;
        this.balance = balance;
        this.debt = debt;
        this.shops = shops;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_Image() {
        return profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        this.profile_Image = profile_Image;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getShops() {
        return shops;
    }

    public void setShops(String shops) {
        this.shops = shops;
    }

}
