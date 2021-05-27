package de.fxnm.web.components.category;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public class Category implements Comparable<Category> {
    @Getter
    private final int id;
    private final String name;

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
