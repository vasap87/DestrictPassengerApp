package ru.alexandrkotovfrombutovo.destrictpassengerapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by alexkotov on 09.01.18.
 */

public class Route implements Serializable{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user")
    private UserInfo user;
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
    @JsonProperty("location")
    private Integer location;

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

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

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
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
