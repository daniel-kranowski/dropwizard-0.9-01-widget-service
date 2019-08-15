package com.example.widget.resource;

import com.example.widget.model.api.Widget;
import com.example.widget.service.WidgetService;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * JAX-RS layer: checks security context, delegates business logic to an underlying service.
 */
public class WidgetResource implements WidgetResourceApi {

    private WidgetService widgetService;

    public WidgetResource(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @Override
    public Response getWidgets(SecurityContext sc) {
        return Response.ok(new GenericEntity<List<Widget>>(widgetService.getWidgets()) { })
                .build();
    }

    @Override
    public Response getWidget(SecurityContext sc, long id) {
        Widget widget = widgetService.getWidget(id);
        if (widget == null) {
            throw new NotFoundException();
        }
        return Response.ok(widget).build();
    }

    @Override
    public Response postWidget(SecurityContext sc, Widget widget) {
        widget.setId(0); //Ensure it is added
        long id = widgetService.addWidget(widget);
        return Response.ok(id).build();
    }

    @Override
    public Response deleteWidget(SecurityContext sc, long id) {
        Widget deleted = widgetService.deleteWidget(id);
        if (deleted == null) {
            return Response.noContent().build();
        }
        return Response.ok(deleted).build();
    }
}
