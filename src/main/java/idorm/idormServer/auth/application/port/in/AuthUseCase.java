package idorm.idormServer.auth.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.application.port.in.dto.LoginRequest;

public interface AuthUseCase {

	AuthResponse login(final LoginRequest request);
}
