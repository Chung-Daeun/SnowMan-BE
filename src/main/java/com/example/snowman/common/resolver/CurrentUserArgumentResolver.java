package com.example.snowman.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.snowman.entity.User;
import com.example.snowman.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class)
			&& parameter.getParameterType().equals(User.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);


		// 1. 쿠키에서 JSESSIONID를 찾고 값이 "test"인지 확인합니다.
		String jsessionId = null;
		if (request.getCookies() != null) {
			for (var cookie : request.getCookies()) {
				if ("JSESSIONID".equals(cookie.getName())) {
					jsessionId = cookie.getValue();
					break;
				}
			}
		}

		// 2. 만약 값이 "test"라면? 아묻따 1번 유저!
		if ("test".equals(jsessionId)) {
			return userRepository.findById(1L).orElseGet(() ->
					userRepository.save(User.create("test-fallback-sub"))
			);
		}

		// 3. "test"가 아니면 정상적인 구글 로그인 로직 수행
		Authentication authentication =
			SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
			throw new IllegalStateException("로그인되지 않은 사용자입니다.");
		}

		String googleSub = oAuth2User.getAttribute("sub");
		if (googleSub == null) {
			throw new IllegalStateException("구글 사용자 식별자(sub)를 찾을 수 없습니다.");
		}

		return userRepository.findByGoogleSub(googleSub)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
	}
}
