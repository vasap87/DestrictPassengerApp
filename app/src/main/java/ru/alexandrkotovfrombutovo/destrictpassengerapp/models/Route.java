package ru.alexandrkotovfrombutovo.destrictpassengerapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by alexkotov on 09.01.18.
 */

public class Route{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("userUuid")
    private String userUuid;
    @JsonProperty("fromRoute")
    private String fromRoute;
    @JsonProperty("toRoute")
    private String toRoute;
    @JsonProperty("startDateTime")
    private Long startDateTime;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("isDriver")
    private Boolean isDriver;

    public Boolean getDriver() {
        return isDriver;
    }

    public void setDriver(Boolean driver) {
        isDriver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getFromRoute() {
        return fromRoute;
    }

    public void setFromRoute(String fromRoute) {
        this.fromRoute = fromRoute;
    }

    public String getToRoute() {
        return toRoute;
    }

    public void setToRoute(String toRoute) {
        this.toRoute = toRoute;
    }

    public Long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
