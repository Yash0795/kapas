package com.kapas.user.service;

import com.kapas.user.entity.User;
import com.kapas.user.mapper.UserMapper;
import com.kapas.user.model.LoggedInUser;
import com.kapas.user.model.Login;
import com.kapas.user.model.UserRequest;
import com.kapas.user.model.UserResponse;
import com.kapas.user.model.UserSession;
import com.kapas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionManagerService sessionManagerService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public LoggedInUser loginWithPassword(Login login) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByUserName(login.getUserName());
        User user = optionalUser.orElseThrow(() -> new Exception("Login failed. Provided data may not be correct."));
        if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            UserSession userSession = sessionManagerService.createSession(user);
            return new LoggedInUser(user, userSession);
        } else {
            throw new Exception("The password was incorrect.");
        }
    }

    public void deleteSession(String sessionId) {
        sessionManagerService.deleteSession(sessionId);
    }

    public void destroyAllUserSessions(User principal) {
        sessionManagerService.deleteForUser(principal);
    }

    @Transactional(rollbackFor = Throwable.class)
    public UserResponse createUser(User loggedInUser, UserRequest userRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByEmail(userRequest.getEmail());
        if (optionalUser.isEmpty()) {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            User saveUser = userMapper.userRequestToUser(userRequest, loggedInUser);
            userRepository.persist(saveUser);
            return userMapper.userToUserResponse(saveUser);
        } else {
            throw new Exception("User with " + userRequest.getEmail() + " email already exist");
        }
    }
}
