package entity;

import java.util.Objects;

public class EntityUser {
    private Long id;
    private String name;
    private Long userId;
    private Long chatId;

    public EntityUser() {
    }

    public EntityUser(Long id, String name, Long userId, Long chatId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityUser that = (EntityUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, chatId);
    }

    @Override
    public String toString() {
        return "EntityUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", chatId=" + chatId +
                '}';
    }
}
