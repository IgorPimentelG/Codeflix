package com.fullcycle.admin.catalog.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        final var expectedChecksum = "checksum";
        final var expectedName = "name";
        final var expectedLocation = "/location";

        final var image = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        assertNotNull(image);
        assertEquals(expectedChecksum, image.getChecksum());
        assertEquals(expectedName, image.getName());
        assertEquals(expectedLocation, image.getLocation());
    }

    @Test
    public void givenTwoImagesWithSameLocation_whenCallsEquals_shouldReturnTrue() {
        final var expectedChecksum = "checksum";
        final var expectedLocation = "/location";

        final var image1 = ImageMedia.with(expectedChecksum, "any name 1", expectedLocation);
        final var image2 = ImageMedia.with(expectedChecksum, "any name 2", expectedLocation);

        assertEquals(image1, image2);
        assertNotSame(image1, image2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
       assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "any name", "/location"));
       assertThrows(NullPointerException.class, () -> ImageMedia.with("checksum", null, "/location"));
       assertThrows(NullPointerException.class, () -> ImageMedia.with("checksum", "any name", null));
    }
}
