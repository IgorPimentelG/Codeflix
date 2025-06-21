package com.fullcycle.admin.catalog.domain.castmember;

import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CastMemberTest {

    @Test
    public void givenValidParams_whenCallsNewMember_thenInstantiateCastMember() {
        final var expectedName = "any name";
        final var expectedType = CastMemberType.ACTOR;

        final var castMember = CastMember.newMember(expectedName, expectedType);

        assertNotNull(castMember);
        assertNotNull(castMember.getId());
        assertEquals(expectedName, castMember.getName());
        assertEquals(expectedType, castMember.getType());
        assertNotNull(castMember.getCreatedAt());
        assertNull(castMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallsNewMember_shouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var error = assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsNewMember_shouldReceiveNotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";

        final var error = assertThrows(
          NotificationException.class,
          () -> CastMember.newMember(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveNotification() {
        final var expectedName = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam hendrerit nec justo at mattis. Phasellus
            est nunc, iaculis eget vehicula in, semper et mi. Curabitur justo arcu, interdum sed facilisis vel,
            viverra at nibh. Sed fringilla tortor eu nulla finibus finibus. Integer quis lorem nec enim venenatis
            vulputate eget id magna. Morbi blandit erat eget mauris sagittis lobortis. Praesent tincidunt elit quis
            ex pellentesque malesuada. Aliquam erat volutpat. In cursus et diam id tincidunt. Integer non diam 
            vulputate, rhoncus risus at, luctus sapien. Donec pharetra feugiat commodo. Curabitur pretium porta 
            neque eu dictum. Aliquam erat volutpat.
         """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 1 and 255 characters";

        final var error = assertThrows(
          NotificationException.class,
          () -> CastMember.newMember(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNullType_whenCallsNewMember_shouldReceiveNotification() {
        final var expectedName = "any name";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type cannot be null";

        final var error = assertThrows(
          NotificationException.class,
          () -> CastMember.newMember(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdate_shouldReceiveUpdated() {
        final var expectedName = "any name updated";
        final var expectedType = CastMemberType.ACTOR;
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var updated = castMember.update(expectedName, expectedType);

        assertNotNull(updated);
        assertNotNull(updated.getId());
        assertEquals(expectedName, updated.getName());
        assertEquals(expectedType, updated.getType());
        assertNotNull(updated.getCreatedAt());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    public void givenInvalidNullType_whenCallsUpdate_shouldReceiveNotification() {
        final var expectedName = "any name updated";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type cannot be null";

        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var error = assertThrows(
          NotificationException.class,
          () -> castMember.update(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNullName_whenCallsUpdate_shouldReceiveNotification() {
        final String expectedName = null;
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var error = assertThrows(
          NotificationException.class,
          () -> castMember.update(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsUpdate_shouldReceiveNotification() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";

        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var error = assertThrows(
          NotificationException.class,
          () -> castMember.update(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthMoreThan255_whenCallsUpdate_shouldReceiveNotification() {
        final var expectedName = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam hendrerit nec justo at mattis. Phasellus
            est nunc, iaculis eget vehicula in, semper et mi. Curabitur justo arcu, interdum sed facilisis vel,
            viverra at nibh. Sed fringilla tortor eu nulla finibus finibus. Integer quis lorem nec enim venenatis
            vulputate eget id magna. Morbi blandit erat eget mauris sagittis lobortis. Praesent tincidunt elit quis
            ex pellentesque malesuada. Aliquam erat volutpat. In cursus et diam id tincidunt. Integer non diam 
            vulputate, rhoncus risus at, luctus sapien. Donec pharetra feugiat commodo. Curabitur pretium porta 
            neque eu dictum. Aliquam erat volutpat.
         """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 1 and 255 characters";

        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var error = assertThrows(
          NotificationException.class,
          () -> castMember.update(expectedName, expectedType)
        );

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }
}
