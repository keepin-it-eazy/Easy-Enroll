/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 240971051
 */
public class enrollment implements Serializable {

    private int Studentid;

    private ArrayList<Course> courses;

    public enrollment(int Studentid, ArrayList<Course> courses) {
        this.Studentid = Studentid;
        this.courses = courses;
    }

    public int getStudentid() {
        return Studentid;
    }

    public void setStudentid(int Studentid) {
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
