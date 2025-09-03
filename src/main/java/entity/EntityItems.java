package entity;

import java.util.Objects;

public class EntityItems {
    public enum Rank{A,B,S}
    public enum Type{WEAPON,CHARACTER}
    private Long id;
    private String name;
    private Type itemType;
    private Rank itemRank;
    private Integer categoryId;

    public EntityItems() {
    }

    public EntityItems(Long id, String name, Type itemType, Rank itemRank, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.itemRank = itemRank;
        this.categoryId = categoryId;
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

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(Type itemType) {
        this.itemType = itemType;
    }

    public Rank getItemRank() {
        return itemRank;
    }

    public void setItemRank(Rank itemRank) {
        this.itemRank = itemRank;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityItems that = (EntityItems) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                itemType == that.itemType &&
                itemRank == that.itemRank &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, itemType, itemRank, categoryId);
    }
}
