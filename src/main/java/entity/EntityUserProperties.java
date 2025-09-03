package entity;

import java.util.Objects;

public class EntityUserProperties<T> {
    private Integer id;
    private Long userId;
    private String name;
    private T value;
    private Class<T> type;

    public EntityUserProperties() {
    }

    public EntityUserProperties(Integer id, Long userId, String name, T value,Class<T> type) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityUserProperties<?> that = (EntityUserProperties<?>) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, value);
    }

    @Override
    public String toString() {
        return "EntityUserProperties{" +
                "value=" + value +
                ", id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
