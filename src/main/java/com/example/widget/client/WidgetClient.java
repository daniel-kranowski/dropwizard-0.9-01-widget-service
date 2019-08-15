package com.example.widget.client;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.servlets.AdminServlet;
import com.example.widget.health.WidgetHealthCheck;
import com.example.widget.model.api.Widget;
import com.example.widget.resource.WidgetResourceApi;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WidgetClient {
    private WebTarget webTarget;
    private WebTarget adminTarget;

    public WidgetClient(String baseUrl, String username, String password) {
        Client client = ClientBuilder.newClient();
        client.register(HttpAuthenticationFeature.basic(username, password));
        client.register(new LoggingFilter(Logger.getLogger(getClass().getName()), true));
        this.webTarget = client.target(baseUrl)
                .path(WidgetResourceApi.PATH_WIDGETS);
        this.adminTarget = client.target(baseUrl)
                .path("/admin"); //Default set in dropwizard SimpleServerFactory
    }

    /**
     * This class exists simply to get the results of calling the dropwizard healthcheck endpoint.
     * Would like to do invoke(...Map&lt;String, HealthCheck.Result>...) but Jackson refuses since
     * com.codahale.metrics.health.HealthCheck.Result has no default constructor.  Can't extend
     * HealthCheck.Result here for the same reason.
     */
    private static class HealthCheckResult {
        private boolean healthy;
        private String message;
        private Throwable error;
        private Map<String, Object> details;

        public boolean isHealthy() {
            return healthy;
        }

        public void setHealthy(boolean healthy) {
            this.healthy = healthy;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Throwable getError() {
            return error;
        }

        public void setError(Throwable error) {
            this.error = error;
        }

        public Map<String, Object> getDetails() {
            return details;
        }

        public void setDetails(Map<String, Object> details) {
            this.details = details;
        }
    }

    /**
     * Returns true if the WidgetHealthCheck is healthy.
     */
    public boolean healthcheck() {
        Map<String, HealthCheckResult> results = adminTarget.path(AdminServlet.DEFAULT_HEALTHCHECK_URI)
                .request(MediaType.APPLICATION_JSON)
                .buildGet()
                .invoke(new GenericType<Map<String, HealthCheckResult>>() { /*empty*/ });
        HealthCheckResult result = results.get(WidgetHealthCheck.class.getSimpleName());
        return result != null && result.isHealthy();
    }

    public List<Widget> getWidgets() {
        return webTarget.request(MediaType.APPLICATION_JSON)
                .buildGet()
                .invoke(new GenericType<List<Widget>>() { /*empty*/ });
    }

    public Widget getWidget(long id) {
        return webTarget.path(WidgetResourceApi.PATH_ID)
                .resolveTemplate(WidgetResourceApi.PATHPARAM_ID, id)
                .request(MediaType.APPLICATION_JSON)
                .buildGet()
                .invoke(Widget.class);
    }

    public long postWidget(Widget widget) {
        return webTarget.request(MediaType.APPLICATION_JSON)
                .buildPost(Entity.entity(widget, MediaType.APPLICATION_JSON))
                .invoke(Long.class);
    }

    public Widget deleteWidget(long id) {
        Response response = webTarget.path(WidgetResourceApi.PATH_ID)
                .resolveTemplate(WidgetResourceApi.PATHPARAM_ID, id)
                .request(MediaType.APPLICATION_JSON)
                .buildDelete()
                .invoke();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Widget.class);
        }
        else if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            return null; //This is fine, from the perspective that DELETE means "ensure not exists".
        }
        else {
            throw new WebApplicationException(response);
        }
    }
}
