package com.argusoft.form.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class LocationType {

  private double lat;
  private double lng;

  public LocationType() {
  }

  public LocationType(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  @Override
  public String toString() {
    return "LocationType [lat=" + lat + ", lng=" + lng + "]";
  }
  

}
