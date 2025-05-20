package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN, MEMBER;

    // Enum 역직렬화 지정 (= Json 문자열을 Enum 값으로 변환할 때 사용)
    @JsonCreator
    public static Role fromString(String str){
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }
        //default
        return MEMBER;
    }

    // Enum -> JSON 직렬화 지정
    @JsonValue
    public String toJson(){
        return this.name().toLowerCase();
    }


}
