/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.domain;

import java.util.ArrayList;

/**
 *
 * @author 240971051
 */
public class enrollment {

    private String Studentid;

    private ArrayList<Course> courses;

    public enrollment(String Studentid, ArrayList<Course> courses) {
        this.Studentid = Studentid;

        this.courses = courses;
    }

    public String getStudentid() {
        return Studentid;
    }

    public void setStudentid(String Studentid) {
        this.Studentid = Studentid;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "enrollment{" + "Studentid=" + Studentid + ", courses=" + courses + '}';
    }

}
