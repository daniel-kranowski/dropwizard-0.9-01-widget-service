package com.example.widget.service;

import com.example.widget.dao.WidgetDao;
import com.example.widget.model.api.Widget;

import java.util.List;

/**
 * Service layer: performs business logic, unconcerned with authorization.
 *
 * Not really any business logic here...
 * In a normal-sized application there is typically some logic which should be kept separate from JAX-RS and DAO concerns.
 */
public class WidgetService {
    private WidgetDao widgetDao;

    public WidgetService(WidgetDao widgetDao) {
        this.widgetDao = widgetDao;
    }

    public List<Widget> getWidgets() {
        return widgetDao.getWidgets();
    }

    public Widget getWidget(long id) { return widgetDao.getWidget(id); }

    public long addWidget(Widget widget) {
        return widgetDao.addWidget(widget);
    }

    public Widget deleteWidget(long id) {
        return widgetDao.deleteWidget(id);
    }
}
