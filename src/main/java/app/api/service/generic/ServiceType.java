package app.api.service.generic;

import app.api.generic.BaseType;

public enum ServiceType implements BaseType<ServiceType> {
    WEATHER,
    EVENTS,
    NEWS;
}
