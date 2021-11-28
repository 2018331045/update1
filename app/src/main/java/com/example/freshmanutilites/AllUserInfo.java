package com.example.freshmanutilites;

public class AllUserInfo {
    // declare the string variable then - 0 arg cons - getter - setter - all arg const
    String name , dept , batch , mobNo , bloodGrp , userId , url ;

    public AllUserInfo() {

    }

    public AllUserInfo(String name, String dept, String batch, String mobNo, String bloodGrp, String userId, String url) {
        this.name = name;
        this.dept = dept;
        this.batch = batch;
        this.mobNo = mobNo;
        this.bloodGrp = bloodGrp;
        this.userId = userId;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
