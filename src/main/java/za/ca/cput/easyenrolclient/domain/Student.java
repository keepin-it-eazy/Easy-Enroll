package za.ca.cput.easyenrolclient.domain;

import java.io.Serializable;


public class Student implements Serializable{

    private int StudentId;
    private String name;
    private String email;
    private String password;

    public Student() {
    }

    public Student(int StudentId, String name, String email, String password) {
        this.StudentId = StudentId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int StudentId) {
        this.StudentId = StudentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Student{" + "StudentId=" + StudentId + ", name=" + name + ", email=" + email + ", password=" + password + '}';
    }

}
