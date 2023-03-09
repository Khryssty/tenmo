package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
public class UserController {

   private UserDao userDao;

   public UserController(UserDao userDao) {
      this.userDao = userDao;
   }

   @RequestMapping(method = RequestMethod.GET)
   public List<User> getAllUsers() {
      return userDao.findAll();
   }

   @RequestMapping(path = "/{id}/user", method = RequestMethod.GET)
   public User getUserById(@PathVariable int id) {
      return userDao.getUserById(id);
   }

   @RequestMapping(path = "/{name}", method = RequestMethod.GET)
   public int findIdByName(@PathVariable String name) {
      return userDao.findIdByUsername(name);
   }

   @RequestMapping(path = "/{name}/user", method = RequestMethod.GET)
   public User findUserByName(@PathVariable String name) {
      return userDao.findByUsername(name);
   }

   @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
   public String findUsernamebyAccountId (@PathVariable int id) {
      return userDao.findByAccountId(id);
   }

}
