package com.argusoft.form.entity;

public class LocationPoint {
  private double longitude;
  private double latitude;

  public LocationPoint(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public LocationPoint() {
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  @Override
  public String toString() {
    return "LocationPoint [longitude=" + longitude + ", latitude=" + latitude + "]";
  }

}
