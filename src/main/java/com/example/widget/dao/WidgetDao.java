package com.example.widget.dao;

import com.example.widget.model.api.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Stub dao - an in-memory collection of widgets.
 */
public class WidgetDao {
    private long nextId = 1;
    private SortedMap<Long, Widget> widgets;

    public WidgetDao(List<Widget> initialWidgets) {
        this.widgets = new TreeMap<>();
        if (initialWidgets != null) {
            for (Widget widget: initialWidgets) {
                addWidget(widget);
            }
        }
    }

    public List<Widget> getWidgets() {
        return widgets==null ? new ArrayList<>() : new ArrayList<>(widgets.values());
    }

    /**
     * Returns the identified widget, or null if not found.
     */
    public Widget getWidget(long id) {
        return widgets.get(id);
    }

    /**
     * Adds a new Widget and returns the generated id.
     */
    public long addWidget(Widget widget) {
        if (widget.getId() > 0) {
            throw new IllegalArgumentException("Cannot add an existing widget: " + widget);
        }
        widget.setId(nextId++);
        widgets.put(widget.getId(), widget);
        return widget.getId();
    }

    /**
     * Removes the identified Widget from storage, and returns the Widget object (or null if not found).
     */
    public Widget deleteWidget(long id) {
        return widgets.remove(id);
    }
}
