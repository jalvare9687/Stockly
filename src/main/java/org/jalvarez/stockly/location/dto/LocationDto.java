package org.jalvarez.stockly.location.dto;

public record LocationDto(
        Long id,
        String name,
        String address,
        String city,
        String state,
        String country,
        String zip
) {
}
