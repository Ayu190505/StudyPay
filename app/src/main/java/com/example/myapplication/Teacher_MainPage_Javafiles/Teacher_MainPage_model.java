package com.example.myapplication.Teacher_MainPage_Javafiles;

public class Teacher_MainPage_model {

    String uid, owner, approved,title,Description, image, teacherName;

    public Teacher_MainPage_model(){
    }

    public Teacher_MainPage_model(String uid, String owner, String approved, String title, String Description, String image, String teacherName){
        this.uid = uid;
        this.owner = owner;
        this.approved = approved;
        this.title = title;
        this.Description = Description;
        this.image = image;
        this.teacherName = teacherName;
    }

    public String getUid() {
        return this.uid;
    }

    public String getOwner() {
        return this.owner;
    }


    public String getTeacherName() {
        return this.teacherName;
    }


    public String getApproved() {
        return this.approved;
    }



    public String getTitle() {
        return this.title;
    }


    public String getDescription() {
        return this.Description;
    }




    public String getImage() {
        return this.image;
    }
}
