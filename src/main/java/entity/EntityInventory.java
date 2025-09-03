package entity;

import java.util.Objects;

public class EntityInventory {
    private Long id;
    private Long userId;
    private Long itemId;
    private Long amount;

    public EntityInventory() {
    }

    public EntityInventory(Long id, Long userId, Long itemId, Long amount) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityInventory that = (EntityInventory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, itemId, amount);
    }
}
