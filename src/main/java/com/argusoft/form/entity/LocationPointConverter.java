package com.argusoft.form.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocationPointConverter implements AttributeConverter<LocationPoint, String> {

  @Override
  public String convertToDatabaseColumn(LocationPoint locationPoint) {
    if (locationPoint == null) {
      return null;
    }
    return "(" + locationPoint.getLatitude() + "," + locationPoint.getLongitude() + ")";
  }

  @Override
  public LocationPoint convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    String[] coordinates = dbData.replace("(", "").replace(")", "").split(",");
    double latitude = Double.parseDouble(coordinates[0]);
    double longitude = Double.parseDouble(coordinates[1]);
    return new LocationPoint(latitude, longitude);
  }
}
