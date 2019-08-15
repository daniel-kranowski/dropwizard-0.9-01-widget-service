package com.example.widget;

import com.example.widget.auth.MyAuthenticator;
import com.example.widget.auth.MyAuthorizer;
import com.example.widget.dao.UserDao;
import com.example.widget.dao.WidgetDao;
import com.example.widget.health.WidgetHealthCheck;
import com.example.widget.model.api.User;
import com.example.widget.resource.WidgetResource;
import com.example.widget.service.WidgetService;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class WidgetApplication extends Application<WidgetConfiguration> {

    public static void main(String[] args) throws Exception {
        new WidgetApplication().run(args);
    }

    @Override
    public String getName() {
        return "The Widget Service";
    }

    @Override
    public void run(WidgetConfiguration widgetConfiguration, Environment environment) {
        WidgetDao widgetDao = new WidgetDao(widgetConfiguration.getWidgets());
        WidgetService widgetService = new WidgetService(widgetDao);
        environment.jersey().register(makeAuthFilter(widgetConfiguration));
        environment.jersey().register(RolesAllowedDynamicFeature.class); //Enable @RolesAllowed
        environment.jersey().register(new WidgetResource(widgetService));
        environment.healthChecks().register(WidgetHealthCheck.class.getSimpleName(), new WidgetHealthCheck());
    }

    /**
     * Instantiates a builtin dropwizard auth filter, based on basic authentication.
     */
    private Object makeAuthFilter(WidgetConfiguration widgetConfiguration) {
        UserDao userDao = new UserDao(widgetConfiguration.getUsersAndPasswords());
        return new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new MyAuthenticator(userDao))
                    .setAuthorizer(new MyAuthorizer())
                    .buildAuthFilter()
        );
    }
}
