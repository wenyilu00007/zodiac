package com.wyl.zodiac.demo.controller;

import com.wyl.zodiac.demo.domain.User;
import com.wyl.zodiac.demo.service.impl.IUserService;
import com.wyl.zodiac.web.controller.BasicController;
import com.wyl.zodiac.web.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* @Title: HelloController 
* @Package com.wyl.zodiac.demo.controller
* @Description: 
* @author
* @date 2017/8/9 17:33
* @version V1.0   
*/
@RestController
@RequestMapping("/hello")
public class HelloController extends BasicController {

    @Autowired
    private IUserService<User> userService;

    @RequestMapping(value = "/user/{userName}", method = RequestMethod.GET)
    public Response getUser(@PathVariable String userName) {
        User user = new User();
        user.setUserName(userName);
        return returnSuccess(userService.selectOne(user));
    }

    @RequestMapping(value = "/userFromCache/{userName}", method = RequestMethod.GET)
    public Response getUserFromCache(@PathVariable String userName) {
        return returnSuccess(userService.selectUserFromCache(userName));
    }
}