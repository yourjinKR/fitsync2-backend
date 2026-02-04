package app.fitsync.global.security;

import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityFacade {

    public Optional<String> getLoginIdOptional() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return Optional.ofNullable(auth.getName());
    }

    public String getLoginIdOrThrow() {
        return getLoginIdOptional()
                .orElseThrow(() -> new IllegalStateException("Unauthenticated request"));
    }
}
