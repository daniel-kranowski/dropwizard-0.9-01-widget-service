package com.example.widget.health;

import com.codahale.metrics.health.HealthCheck;

public class WidgetHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy("Alive and well");
    }
}
