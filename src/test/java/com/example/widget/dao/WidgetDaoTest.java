package com.example.widget.dao;

import com.example.widget.model.api.Widget;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WidgetDaoTest {

    public static final String WIDGET_SENSOR_NAME = "Sensor";
    public static final String WIDGET_SENSOR_COLOR = "blue";
    public static final int WIDGET_SENSOR_SIZE = 5;

    public static final String WIDGET_MOTOR_NAME = "Motor";
    public static final String WIDGET_MOTOR_COLOR = "green";
    public static final int WIDGET_MOTOR_SIZE = 13;

    private WidgetDao widgetDao;

    @Before
    public void setup() {
        List<Widget> initialWidgets = new ArrayList<>();
        initialWidgets.add(new Widget(WIDGET_SENSOR_NAME, WIDGET_SENSOR_COLOR, WIDGET_SENSOR_SIZE));
        widgetDao = new WidgetDao(initialWidgets);
    }

    @Test
    public void getWidgets() {
        List<Widget> widgets = widgetDao.getWidgets();
        assertEquals(1, widgets.size());
        assertEquals(WIDGET_SENSOR_NAME, widgets.get(0).getName());
    }

    @Test
    public void getWidget() {
        Widget widget = widgetDao.getWidget(1);
        assertEquals(WIDGET_SENSOR_NAME, widget.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExistingWidgetNotAllowed() {
        List<Widget> widgets = widgetDao.getWidgets();
        widgetDao.addWidget(widgets.get(0));
    }

    @Test
    public void addWidget() {
        long id = widgetDao.addWidget(new Widget(WIDGET_MOTOR_NAME, WIDGET_MOTOR_COLOR, WIDGET_MOTOR_SIZE));
        List<Widget> widgets = widgetDao.getWidgets();
        Widget motor = widgets.get(1);
        assertEquals(id, motor.getId());
    }

}