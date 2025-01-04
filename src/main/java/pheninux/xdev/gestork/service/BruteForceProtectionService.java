package pheninux.xdev.gestork.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionService {
    private static final int MAX_FAILED_LOGINS = 5;
    private static final long LOCKOUT_PERIOD = 30_000; // 30 secondes

    private final Map<String, FailedLoginAttempt> failedLogins = new ConcurrentHashMap<>();

    public boolean validateCredentials(String username, String password) {
        FailedLoginAttempt attempt = failedLogins.computeIfAbsent(username, k -> new FailedLoginAttempt());

        if (attempt.attemptCount >= MAX_FAILED_LOGINS) {
            long lockoutTime = attempt.lockoutTime + LOCKOUT_PERIOD;
            if (System.currentTimeMillis() < lockoutTime) {
                return false; // Compte à rebours avant de débloquer
            }

            // Débloquer l'utilisateur
            failedLogins.remove(username);
            return true;
        }

        if (attempt.attemptCount == MAX_FAILED_LOGINS - 1) {
            // Dernière tentative avant le blocage
            attempt.lockoutTime = System.currentTimeMillis() + LOCKOUT_PERIOD;
        }

        attempt.attemptCount++;
        return true; // Authentification réussie
    }

    private static class FailedLoginAttempt {
        int attemptCount = 0;
        long lockoutTime = 0;
    }
}
