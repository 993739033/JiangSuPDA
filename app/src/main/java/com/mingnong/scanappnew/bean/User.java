package com.mingnong.scanappnew.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2016-01-13.
 *
 * 用户类
 */
public class User extends BaseMsg implements Serializable {
    private Integer USERID;
    private String UserNO;
    private String Userpass;
    private String UserObject;
    private String USERNAME;
    private String UNITUSTRID;
    private String RNAME;
    private String USEENAME;
    private String UNAME;
    private String UPHONE;
    private String M1;

    public User() {
    }

    public String getM1() {
        return M1;
    }

    public void setM1(String m1) {
        M1 = m1;
    }

    public String getRNAME() {
        return RNAME;
    }

    public void setRNAME(String RNAME) {
        this.RNAME = RNAME;
    }

    public String getUNAME() {
        return UNAME;
    }

    public void setUNAME(String UNAME) {
        this.UNAME = UNAME;
    }

    public String getUNITUSTRID() {
        return UNITUSTRID;
    }

    public void setUNITUSTRID(String UNITUSTRID) {
        this.UNITUSTRID = UNITUSTRID;
    }

    public String getUPHONE() {
        return UPHONE;
    }

    public void setUPHONE(String UPHONE) {
        this.UPHONE = UPHONE;
    }

    public String getUSEENAME() {
        return USEENAME;
    }

    public void setUSEENAME(String USEENAME) {
        this.USEENAME = USEENAME;
    }

    public Integer getUSERID() {
        return USERID;
    }

    public void setUSERID(Integer USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getUserNO() {
        return UserNO;
    }

    public void setUserNO(String userNO) {
        UserNO = userNO;
    }

    public String getUserObject() {
        return UserObject;
    }

    public void setUserObject(String userObject) {
        UserObject = userObject;
    }

    public String getUserpass() {
        return Userpass;
    }

    public void setUserpass(String userpass) {
        Userpass = userpass;
    }
}