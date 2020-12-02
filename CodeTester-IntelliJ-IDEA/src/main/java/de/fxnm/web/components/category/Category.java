package de.fxnm.web.components.category;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public class Category implements Comparable<Category> {
    private final String name;
    @Getter
    private final int id;

    public Category(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String errorDetails() {
        return "Category{" +
                "name='" + this.name + '\'' +
                ", id=" + this.id +
                '}';
    }

    @Override
    public int compareTo(@NotNull final Category o) {
        return Integer.compare(o.id, this.id);
    }
}
