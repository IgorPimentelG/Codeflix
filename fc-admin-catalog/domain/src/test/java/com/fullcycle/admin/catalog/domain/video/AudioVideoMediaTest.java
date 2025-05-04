package com.fullcycle.admin.catalog.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance() {
        final var expectedChecksum = "checksum";
        final var expectedName = "name";
        final var expectedEncodedLocation = "/location/encoded";
        final var expectedRawLocation = "/location";
        final var expectedMediaStatus = MediaStatus.PENDING;

        final var audioVideo = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedMediaStatus);

        assertNotNull(audioVideo);
        assertEquals(expectedName, audioVideo.getName());
        assertEquals(expectedChecksum, audioVideo.getChecksum());
        assertEquals(expectedEncodedLocation, audioVideo.getEncodedLocation());
        assertEquals(expectedRawLocation, audioVideo.getRawLocation());
        assertEquals(expectedMediaStatus, audioVideo.getStatus());
    }

    @Test
    public void givenTwoImagesWithSameLocation_whenCallsEquals_shouldReturnTrue() {
        final var expectedChecksum = "checksum";
        final var expectedName = "name";
        final var expectedEncodedLocation = "/location/encoded";
        final var expectedRawLocation = "/location";
        final var expectedMediaStatus = MediaStatus.PENDING;

        final var audioVideo1 = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedMediaStatus);
        final var audioVideo2 = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedMediaStatus);

        assertEquals(audioVideo1, audioVideo2);
        assertNotSame(audioVideo1, audioVideo2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, "any name", "/location", "/location/encoded", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", null, "/location", "/location/encoded", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "any name", null, "/location/encoded", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "any name", "/location", null, MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "any name", "/location", "/location/encoded", null));
    }
}
