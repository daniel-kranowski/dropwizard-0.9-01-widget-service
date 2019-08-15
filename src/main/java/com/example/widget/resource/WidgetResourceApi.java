package com.example.widget.resource;

import com.example.widget.model.api.Role;
import com.example.widget.model.api.Widget;
import io.dropwizard.jersey.caching.CacheControl;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path(WidgetResourceApi.PATH_WIDGETS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CacheControl(noCache = true, noStore = true, mustRevalidate = true)
public interface WidgetResourceApi {

    String PATH_WIDGETS = "/widgets";
    String PATHPARAM_ID = "id";
    String PATH_ID = "/{" + PATHPARAM_ID + "}";

    /**
     * Returns the available widgets or an empty list (always HTTP 200).
     */
    @GET
    @RolesAllowed({Role.Names.USER, Role.Names.ADMIN})
    Response getWidgets(@Context SecurityContext sc);

    /**
     * Returns the identified widget (with HTTP 200), or throws NotFoundException.
     */
    @GET
    @Path(PATH_ID)
    @RolesAllowed({Role.Names.USER, Role.Names.ADMIN})
    Response getWidget(@Context SecurityContext sc, @PathParam(PATHPARAM_ID) long id);

    /**
     * Returns the new widget's server-generated id (with HTTP 200).  Ignores posted id.
     */
    @POST
    @RolesAllowed(Role.Names.ADMIN)
    Response postWidget(@Context SecurityContext sc, Widget widget);

    /**
     * Deletes and returns the identified widget (with HTTP 200), or null with HTTP 204 if not found.
     */
    @DELETE
    @Path(PATH_ID)
    @RolesAllowed(Role.Names.ADMIN)
    Response deleteWidget(@Context SecurityContext sc, @PathParam(PATHPARAM_ID) long id);
}
