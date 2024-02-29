package idorm.idormServer.common.controller;

import static idorm.idormServer.auth.adapter.out.AuthResponseCode.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import idorm.idormServer.auth.adapter.out.exception.AccessDeniedAdminException;
import idorm.idormServer.auth.adapter.out.exception.UnAuthorizedAccessTokenException;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class ErrorController {

	@GetMapping("/api/error")
	public void error(HttpServletRequest request) {
		String exception = (String)request.getAttribute("exception");

		if (UNAUTHORIZED_ACCESS_TOKEN.name().equals(exception)) {
			throw new UnAuthorizedAccessTokenException();
		}
		if (ACCESS_DENIED_ADMIN.name().equals(exception)) {
			throw new AccessDeniedAdminException();
		}
	}
}
