package com.kapas.user.service;

import com.kapas.user.entity.Sessions;
import com.kapas.user.entity.User;
import com.kapas.user.model.UserSession;
import com.kapas.user.repository.SessionsRepository;
import com.kapas.util.AppUtils;
import com.kapas.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class SessionManagerService {

    private static final Logger LOGGER = LogManager.getLogger(SessionManagerService.class);
    private static final String SALT_SPLITTER = "~";
    private static final int EXPIRES_AT_WEBAPP = 60 * 60 * 24 * 2; // 2 days

    private final SessionsRepository sessionOfUserRepository;

    private static String encryptUserSession(String sessionSalt) throws Exception {
        return EncryptionUtil.encrypt(sessionSalt);
    }

    private static String createSessionSalt(String userId) {
        return userId + SALT_SPLITTER + AppUtils.generateRandomNumber(1, 20) + System.currentTimeMillis();
    }

    @Transactional(rollbackFor = Throwable.class)
    public UserSession createSession(User user) throws Exception {

        final LocalDateTime currentTime = LocalDateTime.now();

        String sessionSalt = createSessionSalt(user.getId().toString());
        String sessionId = encryptUserSession(sessionSalt);

        UserSession userSession = new UserSession(sessionId, currentTime.plusSeconds(EXPIRES_AT_WEBAPP), currentTime);
        Sessions sessionsOfUser = new Sessions(user, userSession);
        sessionOfUserRepository.persist(sessionsOfUser);
        return userSession;
    }

    @Transactional(readOnly = true)
    public User validateSession(@NotBlank(message = "Missing Session-Id header in request.") String sessionId) throws Exception {
        Optional<Sessions> sessions = sessionOfUserRepository.findBySessionId(sessionId);
        Sessions session = sessions.orElseThrow(()-> new Exception("Provided Session Id is not valid"));
        if(session.getExpiresAt().compareTo(LocalDateTime.now()) <= 0 ) {
            throw new Exception("Session Id expired.");
        }
        return session.getUser();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteSession(String sessionId) {
        sessionOfUserRepository.deleteBySessionId(sessionId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteForUser(User userId) {
        sessionOfUserRepository.deleteForUser(userId);
    }
}
