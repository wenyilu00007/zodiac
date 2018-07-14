package com.wyl.zodiac.demo.domain;

import com.wyl.zodiac.core.entity.IUser;

import javax.persistence.Column;
import javax.persistence.Table;

/**
* @Title: User 
* @Package com.wyl.zodiac.demo.domain
* @Description: 用户
* @author
* @date 2017/8/9 17:37
* @version V1.0   
*/
@Table(name = "t_bse_user")
public class User implements IUser {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "emp_name")
    private String empName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
