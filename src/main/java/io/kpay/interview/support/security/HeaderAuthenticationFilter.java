package io.kpay.interview.support.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class HeaderAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    //X-USER-ID
    //X-ROOM-ID
    public static final String X_USER_ID="X-USER-ID";
    public static final String X_ROOM_ID="X-ROOM-ID";


    @Getter
    @Setter
    private AuthenticationEntryPoint authenticationEntryPoint;

    HeaderAuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }


    /**
     * 실지는 user / room 에 대한 정보 요청을 받아 예상되나, 이 과제에서는 유효성 체크를 위한 정도로만 구현.
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {

            Optional<Long> userIdParam = Optional.ofNullable(request.getHeader(X_USER_ID)).map(Long::parseLong);
            Optional<String> roomIdParam = Optional.ofNullable(request.getHeader(X_ROOM_ID));

            boolean exists = Stream.of(userIdParam, roomIdParam)
                    .allMatch(Optional::isPresent);

            if(!exists) throw new BadCredentialsException("필수 값이 없습니다.");

            HeaderAuthenticationToken authenticationToken = new HeaderAuthenticationToken(UserRoom.of(userIdParam.get(), roomIdParam.get()));
//            authResult = this.getAuthenticationManager().authenticate(authenticationToken);
//            logger.debug("Authentication success: " + authResult);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return authenticationToken;

        } catch (Exception failed) {
            SecurityContextHolder.clearContext();
            logger.error("Authentication request failed: " + failed);
            try {
                authenticationEntryPoint.commence(request, response,new BadCredentialsException(failed.getMessage(), failed));
            } catch (Exception e) {
                logger.error("authenticationEntryPoint handle error:{}", failed);
            }
        }

        return null;

    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.asList(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        });
    }

}
