package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;


@Getter
public class MemberEntity {
    private String id;
    private String name;
    private Integer age;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("class")
    private ClassType clazz;
    private Role role;
    private String password;

    public MemberEntity() {
    }

    public MemberEntity(MemberCreateCommand command, String encodePassword) {
        this.id = command.getId();
        this.name = command.getName();
        this.age = command.getAge();
        this.clazz = command.getClazz();
        this.role = command.getRole();
        this.password = encodePassword;
    }


}
