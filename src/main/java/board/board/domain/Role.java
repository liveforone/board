package board.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {  //spring security 사용자 권한을 관리함

    GUEST("ROLE_GUEST", "손님"),
    USER("USER_USER", "일반 사용자");

    private final String key;
    private final String title;
}
