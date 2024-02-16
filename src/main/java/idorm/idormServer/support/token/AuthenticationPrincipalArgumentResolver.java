package idorm.idormServer.support.token;

import idorm.idormServer.auth.dto.AuthInfo;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenManager tokenManager;

    public AuthenticationPrincipalArgumentResolver(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = AuthorizationExtractor.extractAccessToken(Objects.requireNonNull(request));
        if (token == null) {
            return new AuthInfo(null, null, null);
        }
        return tokenManager.getParsedClaims(token);
    }
}
