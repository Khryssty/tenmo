package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

   private UserDao userDao;

   public UserController(UserDao userDao) {
      this.userDao = userDao;
   }

   @RequestMapping(path = "/users", method = RequestMethod.GET)
   public List<User> getAllUsers() {
      return userDao.findAll();
   }

   @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
   public User getUserById(@PathVariable int id) {
      return userDao.getUserById(id);
   }

   @RequestMapping(path = "/users/{name}", method = RequestMethod.GET)
   public int findIdByName(@PathVariable String name) {
      return userDao.findIdByUsername(name);
   }

   @RequestMapping(path = "/users/user/{name}", method = RequestMethod.GET)
   public User findUserByName(@PathVariable String name) {
      return userDao.findByUsername(name);
   }

}
