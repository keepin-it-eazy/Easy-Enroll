package za.ca.cput.easyenrolclient.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 240971051
 */
public class enrollment implements Serializable {

    private String studentEmail;
    private ArrayList<Course> courses;

    public enrollment() {
    }

    public enrollment(String studentEmail, ArrayList<Course> courses) {
        this.studentEmail = studentEmail;
        this.courses = courses;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "enrollment{" + "studentEmail=" + studentEmail + ", courses=" + courses + '}';
    }

}
