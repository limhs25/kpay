package io.kpay.interview.support.security;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;


public class HeaderAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Getter
    private final UserRoom userRoom;

    public HeaderAuthenticationToken(UserRoom userRoom) {
        super(null);
        this.userRoom = userRoom;
        setAuthenticated(false);
    }


    @Override
    public Object getPrincipal() {
        return this.userRoom;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    @SneakyThrows
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}
