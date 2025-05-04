package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        final String expectedTitle = null;
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

       assertEquals(expectedErrorCount, error.getErrors().size());
       assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = """
              Lorem ipsum dolor sit amet. Aut veritatis nostrum in dolorem minus non porro obcaecati non vero velit
              et maiores laudantium est autem dolor et modi exercitationem. Ea architecto repellendus ut excepturi
              atque et nobis molestiae sit velit fuga et expedita iste! Et galisum dolore ea doloribus nesciunt
              non obcaecati numquam.
          """;
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 1 and 255 characters";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "any name";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Description cannot be empty";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }


    @Test
    public void givenDescriptionWithLengthGreater1000_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "any name";
        final var expectedDescription = """
            Nam quis nulla. Integer malesuada. In in enim a arcu imperdiet malesuada. Sed vel lectus. Donec odio urna,
            tempus molestie, porttitor ut, iaculis quis, sem. Phasellus rhoncus. Aenean id metus id velit ullamcorper
            pulvinar. Vestibulum fermentum tortor id mi. Pellentesque ipsum. Nulla non arcu lacinia neque faucibus
            fringilla. Nulla non lectus sed nisl molestie malesuada. Proin in tellus sit amet nibh dignissim sagittis.
            Vivamus luctus egestas leo. Maecenas sollicitudin. Nullam rhoncus aliquam metus. Etiam egestas wisi a
            erat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi gravida libero nec velit.
            Morbi scelerisque luctus velit. Etiam dui sem, fermentum vitae, sagittis id, malesuada in, quam.
            Proin mattis lacinia justo. Vestibulum facilisis auctor urna. Aliquam in lorem sit amet leo accumsan
            lacinia. Integer rutrum, orci vestibulum ullamcorper ultricies, lacus quam ultricies odio, vitae
            placerat pede sem sit amet enim. Phasellus et lorem id felis nonummy placerat. Fusce dui leo, imperdiet
            in, aliquam sit amet, feugiat eu, orci. Aenean vel massa quis mauris vehicula lacinia. Quisque
            tincidunt scelerisque libero. Maecenas libero. Etiam dictum tincidunt diam. Donec ipsum massa, ullamcorper
            in, auctor et, scelerisque sed, est. Suspendisse nisl. Sed convallis magna eu sem. Cras pede libero,
            dapibus nec, pretium sit amet, tempor quis, urna.  Nam quis nulla. Integer malesuada. In in enim a arcu imperdiet 
            malesuada. Sed vel lectus. Donec odio urna,
            tempus molestie, porttitor ut, iaculis quis, sem. Phasellus rhoncus. Aenean id metus id velit ullamcorper
            pulvinar. Vestibulum fermentum tortor id mi. Pellentesque ipsum. Nulla non arcu lacinia neque faucibus
            fringilla. Nulla non lectus sed nisl molestie malesuada. Proin in tellus sit amet nibh dignissim sagittis.
            Vivamus luctus egestas leo. Maecenas sollicitudin. Nullam rhoncus aliquam metus. Etiam egestas wisi a
            erat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi gravida libero nec velit.
            Morbi scelerisque luctus velit. Etiam dui sem, fermentum vitae, sagittis id, malesuada in, quam.
            Proin mattis lacinia justo. Vestibulum facilisis auctor urna. Aliquam in lorem sit amet leo accumsan
            lacinia. Integer rutrum, orci vestibulum ullamcorper ultricies, lacus quam ultricies odio, vitae
            placerat pede sem sit amet enim. Phasellus et lorem id felis nonummy placerat. Fusce dui leo, imperdiet
            in, aliquam sit amet, feugiat eu, orci. Aenean vel massa quis mauris vehicula lacinia. Quisque
            tincidunt scelerisque libero. Maecenas libero. Etiam dictum tincidunt diam. Donec ipsum massa, ullamcorper
            in, auctor et, scelerisque sed, est. Suspendisse nisl. Sed convallis magna eu sem. Cras pede libero,
            dapibus nec, pretium sit amet, tempor quis, urna.
          """;
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Description must be between 1 and 1000 characters";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "any name";
        final var expectedDescription = "any description";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "LaunchedAt cannot be null";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "any name";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Rating cannot be null";

        final var video = Video.newVideo(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        final var validator = new VideoValidator(video, new ThrowsValidationHandler());

        final var error = assertThrows(DomainException.class, validator::validate);

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

}
