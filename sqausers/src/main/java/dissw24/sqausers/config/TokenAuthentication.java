package dissw24.sqausers.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import dissw24.sqausers.model.User;

import java.util.Collection;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private final User user;

    public TokenAuthentication(User user) {
        super(null);
        this.user = user;
        setAuthenticated(true);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }
}
