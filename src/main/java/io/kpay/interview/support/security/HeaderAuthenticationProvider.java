package io.kpay.interview.support.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class HeaderAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        HeaderAuthenticationToken headerAuthenticationToken = (HeaderAuthenticationToken) authentication;
        final Long userId = headerAuthenticationToken.getUserRoom().getUserId();
        final String roomId = headerAuthenticationToken.getUserRoom().getRoomId();
        // todo 사용자 가 그 방에 있는지 확인 부분
        // 추가적인 유효성 체크 로직
        return headerAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HeaderAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
