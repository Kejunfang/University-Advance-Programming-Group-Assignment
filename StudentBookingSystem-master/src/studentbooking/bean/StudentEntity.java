package studentbooking.bean;

import java.util.Objects;

/**
 * @ LiuXinran
 */
public class StudentEntity {
    private String name;
    private String sex;
    private String password;
    private int age;
    private String studentId;
    private String university;
    private String faculty;
    private String profession;
    private String phoneNum;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentEntity that = (StudentEntity) o;

        if (age != that.age) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(sex, that.sex)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(studentId, that.studentId)) return false;
        if (!Objects.equals(university, that.university)) return false;
        if (!Objects.equals(faculty, that.faculty)) return false;
        if (!Objects.equals(profession, that.profession)) return false;
        if (!Objects.equals(phoneNum, that.phoneNum)) return false;
        return Objects.equals(address, that.address);
    }

    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (university != null ? university.hashCode() : 0);
        result = 31 * result + (faculty != null ? faculty.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
