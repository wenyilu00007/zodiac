package com.hoau.zodiac.web.context;


import com.hoau.zodiac.core.entity.IUser;

/**
* @Title: UserContext 
* @Package com.hoau.leo.common.context 
* @Description: 可以全局存取当前登录用户
* @author 陈宇霖  
* @date 2017/8/2 19:06
* @version V1.0   
*/
public class UserContext {

    /**
     * 用threadLocal存储当前登陆的用户
     */
    private static final ThreadLocal<IUser> USER_HOLDER = new ThreadLocal<IUser>();

    /**
     * 获取当前登录用户
     * @return
     * @author 陈宇霖
     * @date 2017年08月02日19:11:11
     */
    public static IUser getCurrentUser() {
        return USER_HOLDER.get();
    }

    /**
     * 设置当前登陆用户
     * @param user
     * @author 陈宇霖
     * @date 2017年08月02日20:31:00
     */
    public static void setCurrentUser(IUser user) {
        USER_HOLDER.set(user);
    }

    /**
     * 清除用户
     * @author 陈宇霖
     * @date 2017年08月02日20:30:54
     */
    public static void remove() {
        USER_HOLDER.remove();
    }
}
