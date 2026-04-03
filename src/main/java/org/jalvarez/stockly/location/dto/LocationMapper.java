package org.jalvarez.stockly.location.dto;

import org.jalvarez.stockly.location.model.Location;
public class LocationMapper {
    public static LocationDto toDto(Location location) {
        if (location == null) return null;

        return new LocationDto(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCity(),
                location.getState(),
                location.getCountry(),
                location.getZip()
        );
    }

}
