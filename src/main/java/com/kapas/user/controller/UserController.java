package com.kapas.user.controller;

import com.kapas.user.annotation.PermissionScopeValidation;
import com.kapas.user.entity.User;
import com.kapas.user.model.LoggedInUser;
import com.kapas.user.model.Login;
import com.kapas.user.model.PermissionEnum;
import com.kapas.user.model.ScopeEnum;
import com.kapas.user.model.UserRequest;
import com.kapas.user.model.UserResponse;
import com.kapas.user.service.UserService;
import com.kapas.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoggedInUser> login(@Valid @RequestBody Login login) throws Exception {
        LoggedInUser loggedInUser = userService.loginWithPassword(login);
        return new ResponseEntity<>(loggedInUser, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String sessionId = request.getHeader(Constants.SESSION_ID);
        userService.deleteSession(sessionId);
        String message = "Session destroyed successfully.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/sessions")
    public ResponseEntity<String> deleteAllSessions(HttpServletRequest request) {
        User principal = (User) request.getAttribute(Constants.PRINCIPAL);
        userService.destroyAllUserSessions(principal);
        return new ResponseEntity<>("All sessions of this user are deleted", HttpStatus.OK);
    }

    @GetMapping(value = "/test")
    @PermissionScopeValidation(scope = ScopeEnum.TOKEN, permission = PermissionEnum.ADD)
    public ResponseEntity<String> test(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.PRINCIPAL);
        return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);
    }

    @PostMapping(value= "/register")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest, HttpServletRequest request) throws Exception {
        User principal = (User) request.getAttribute(Constants.PRINCIPAL);
        UserResponse userResponse = userService.createUser(principal, userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
