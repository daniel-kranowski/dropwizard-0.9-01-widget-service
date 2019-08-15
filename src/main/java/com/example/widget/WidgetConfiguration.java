package com.example.widget;

import com.example.widget.model.api.UserAndPassword;
import com.example.widget.model.api.Widget;
import io.dropwizard.Configuration;

import java.util.List;
import java.util.Map;

public class WidgetConfiguration extends Configuration {
    private Map<String, UserAndPassword> usersAndPasswords;
    private List<Widget> widgets;

    public Map<String, UserAndPassword> getUsersAndPasswords() {
        return usersAndPasswords;
    }

    public void setUsersAndPasswords(Map<String, UserAndPassword> usersAndPasswords) {
        this.usersAndPasswords = usersAndPasswords;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }
}
