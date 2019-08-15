package com.example.widget.model.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

public class Widget {

    private long id;

    @NotEmpty
    private String name;

    private String color;

    private int size;

    public Widget() {
    }

    public Widget(String name, String color, int size) {
        //No id yet.  The widgetDao will set it.
        this.name = name;
        this.color = color;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
