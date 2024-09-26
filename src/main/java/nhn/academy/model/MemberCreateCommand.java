package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberCreateCommand {
    private String id;
    private String name;
    private Integer age;
    @JsonProperty("class")
    private ClassType clazz = ClassType.B;
    private Role role;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setClazz(ClassType clazz) {
        this.clazz = clazz;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public ClassType getClazz() {
        return clazz;
    }

    public Role getRole() {
        return role;
    }
}
