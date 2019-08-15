package com.example.widget.dao;

import com.example.widget.WidgetApplication;
import com.example.widget.WidgetConfiguration;
import com.example.widget.client.WidgetClient;
import com.example.widget.model.api.Widget;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integrates the WidgetClient and server-side WidgetResourceApi, using a running dropwizard instance.
 */
public class WidgetIntegrationTest {

    public static final String WIDGET_SENSOR_NAME = "Sensor";
    public static final String WIDGET_SENSOR_COLOR = "blue";
    public static final int WIDGET_SENSOR_SIZE = 5;

    public static final String WIDGET_MOTOR_NAME = "Motor";
    public static final String WIDGET_MOTOR_COLOR = "green";
    public static final int WIDGET_MOTOR_SIZE = 13;

    public static final String APP_BASE_URL = "http://localhost:8080";
    public static final String USERROLE_USERNAME = "fred";
    public static final String USERROLE_PASSWORD = "password";
    public static final String ADMINROLE_USERNAME = "root";
    public static final String ADMINROLE_PASSWORD = "root";
    public static final String NOBODY_USERNAME = "nobody";
    public static final String NOBODY_PASSWORD = "no-one";

    public static final Widget NEW_WIDGET_SENSOR = new Widget(WIDGET_SENSOR_NAME, WIDGET_SENSOR_COLOR, WIDGET_SENSOR_SIZE);
    public static final Widget NEW_WIDGET_MOTOR = new Widget(WIDGET_MOTOR_NAME, WIDGET_MOTOR_COLOR, WIDGET_MOTOR_SIZE);

    public static final long BOGUS_ID = 2000L;

    @ClassRule
    public static final DropwizardAppRule<WidgetConfiguration> WIDGET_APP_RULE =
            new DropwizardAppRule<>(WidgetApplication.class,
                    ResourceHelpers.resourceFilePath("widget-config-test.yml"));

    private static WidgetClient userWidgetClient;
    private static WidgetClient adminWidgetClient;
    private static WidgetClient nobodyWidgetClient;

    @BeforeClass
    public static void setup() {
        userWidgetClient = new WidgetClient(APP_BASE_URL, USERROLE_USERNAME, USERROLE_PASSWORD);
        adminWidgetClient = new WidgetClient(APP_BASE_URL, ADMINROLE_USERNAME, ADMINROLE_PASSWORD);
        nobodyWidgetClient = new WidgetClient(APP_BASE_URL, NOBODY_USERNAME, NOBODY_PASSWORD);
    }

    private List<Long> widgetsToDelete = new ArrayList<>();

    @After
    public void cleanup() {
        for (Long id : widgetsToDelete) {
            adminWidgetClient.deleteWidget(id);
        }
    }

    @Test
    public void testHealthCheck() {
        assertTrue(userWidgetClient.healthcheck());
    }

    @Test
    public void initiallyNoWidgets() {
        assertTrue(userWidgetClient.getWidgets().isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void getNonexistentWidgetThrowsNotFound() {
        userWidgetClient.getWidget(BOGUS_ID);
    }

    @Test(expected = NotAuthorizedException.class)
    public void getWidgetsAsNobodyThrowsNotAuthorized() {
        //Dropwizard Authenticator failure conveniently throws NotAuthorizedException instead of WebApplicationException(401)
        nobodyWidgetClient.getWidgets();
    }

    @Test(expected = ForbiddenException.class)
    public void postWidgetAsUserIsForbidden() {
        userWidgetClient.postWidget(NEW_WIDGET_SENSOR);
    }

    @Test
    public void postWidgetAsAdminPasses() {
        long id = adminWidgetClient.postWidget(NEW_WIDGET_MOTOR);
        Widget found = userWidgetClient.getWidget(id);
        assertEquals(WIDGET_MOTOR_NAME, found.getName());
        widgetsToDelete.add(id);
    }

    @Test
    public void postWidgetIgnoresNonzeroId() {
        Widget newWidget = new Widget(WIDGET_SENSOR_NAME, WIDGET_SENSOR_COLOR, WIDGET_SENSOR_SIZE);
        newWidget.setId(BOGUS_ID); //Server will ignore this id
        long id = adminWidgetClient.postWidget(newWidget);
        assertNotEquals(BOGUS_ID, id);
        Widget found = userWidgetClient.getWidget(id);
        assertEquals(WIDGET_SENSOR_NAME, found.getName());
        widgetsToDelete.add(id);
    }

    @Test
    public void deleteWidgetAsUserIsForbidden() {
        long id = adminWidgetClient.postWidget(NEW_WIDGET_MOTOR);
        widgetsToDelete.add(id);
        try {
            userWidgetClient.deleteWidget(id);
        }
        catch (WebApplicationException e) {
            //Dropwizard Authorizer failure throws WebApplicationException(403) instead of ForbiddenException...
            if (e.getResponse().getStatus() == Response.Status.FORBIDDEN.getStatusCode())
                return;
        }
        fail("Expected to get a WebApplicationException(403)");
    }

    @Test
    public void deleteWidgetAsAdminPasses() {
        long id = adminWidgetClient.postWidget(NEW_WIDGET_MOTOR);
        List<Widget> listBefore = userWidgetClient.getWidgets();
        Widget deleted = adminWidgetClient.deleteWidget(id);
        assertEquals(WIDGET_MOTOR_NAME, deleted.getName());
        List<Widget> listAfter = userWidgetClient.getWidgets();
        assertEquals(listBefore.size() - 1, listAfter.size());
        assertFalse(listAfter.contains(deleted));
    }

    @Test
    public void deleteNonexistentWidgetIsFine() {
        assertNull(adminWidgetClient.deleteWidget(BOGUS_ID));
    }
}
