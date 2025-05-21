package nhn.academy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    private String id;
    private String password;

}
