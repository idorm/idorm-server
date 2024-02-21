package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.dto.TeamResponse;

public interface TeamUseCase {

  void addTeamMember(AuthResponse authResponse, Long registerMemberId);

  void deleteMember(AuthResponse authResponse, Long memberId);

  void explodeTeam(AuthResponse authResponse);

  TeamResponse findTeam(AuthResponse authResponse);
}
