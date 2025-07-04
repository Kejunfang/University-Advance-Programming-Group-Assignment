package studentbooking.bean;

import java.util.Objects;

/**
 * @ LiuXinran
 */

public class OperatorEntity {
    private int account;
    private String password;
    private String name;
    private String sex;
    private String phoneNum;

    public OperatorEntity() {}
    public OperatorEntity(int account, String password, String name, String sex, String phoneNum) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.phoneNum = phoneNum;
    }


    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperatorEntity that = (OperatorEntity) o;

        if (account != that.account) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(sex, that.sex)) return false;
        return Objects.equals(phoneNum, that.phoneNum);
    }

    public int hashCode() {
        int result = account;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        return result;
    }
}
