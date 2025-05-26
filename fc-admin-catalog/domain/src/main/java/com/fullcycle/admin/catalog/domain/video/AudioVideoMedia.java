package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.ValueObject;
import lombok.Getter;

import java.util.Objects;

@Getter
public class AudioVideoMedia extends ValueObject {

    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioVideoMedia(
      final String checksum,
      final String name,
      final String rawLocation,
      final String encodedLocation,
      final MediaStatus status
    ) {
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
      final String checksum,
      final String name,
      final String rawLocation,
      final String encodedLocation,
      final MediaStatus status
    ) {
        return new AudioVideoMedia(checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioVideoMedia with(
      final String checksum,
      final String name,
      final String rawLocation
    ) {
        return new AudioVideoMedia(checksum, name, rawLocation, "", MediaStatus.PENDING);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AudioVideoMedia that = (AudioVideoMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation) && Objects.equals(encodedLocation, that.encodedLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation, encodedLocation);
    }
}
