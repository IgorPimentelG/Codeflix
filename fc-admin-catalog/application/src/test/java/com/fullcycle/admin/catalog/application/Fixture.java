package com.fullcycle.admin.catalog.application;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.video.Rating;
import com.fullcycle.admin.catalog.domain.video.Resource;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoPreview;
import com.github.javafaker.Faker;
import io.vavr.API;

import java.time.Instant;
import java.time.Year;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String title() {
        return FAKER.name().title();
    }

    public static String description() {
        return FAKER.lorem().paragraph();
    }

    public static Integer year() {
        return FAKER.number().numberBetween(1900, 2100);
    }

    public static Double duration() {
        return FAKER.number().randomDouble(2, 1, 100);
    }

    public static Boolean bool() {
        return FAKER.bool().bool();
    }

    public static Instant instant() {
        return FAKER.date().birthday().toInstant();
    }

    public static final class CastMembers {

        private static final CastMember CAST_MEMBER = CastMember.newMember(Fixture.name(), type());

        public static CastMemberType type() {
            return FAKER.options()
              .option(CastMemberType.values());
        }

        public static CastMember castMember() {
            return CastMember.with(CAST_MEMBER);
        }
    }

    public static final class Videos {

        public static Video video() {
            return Video.newVideo(
              Fixture.title(),
              Fixture.description(),
              Year.of(Fixture.year()),
              Fixture.duration(),
              Fixture.Videos.rating(),
              Fixture.bool(),
              Fixture.bool(),
              Set.of(Categories.category().getId()),
              Set.of(Genres.genre().getId()),
              Set.of(CastMembers.castMember().getId())
            );
        }

        public static VideoPreview videoPreview() {
            return new VideoPreview(
              UUID.randomUUID(),
              Fixture.title(),
              Fixture.description(),
              Fixture.instant(),
              Fixture.instant()
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = API.Match(type).of(
              Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
              Case($(), () -> "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();
            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }
    }

    public static final class Categories {

        private static final Category CATEGORY = Category.newCategory(Fixture.title(), Fixture.description(), true);

        public static Category category() {
            return CATEGORY;
        }
    }

    public static final class Genres {

        private static final Genre GENRE = Genre.newGenre(FAKER.name().name(), true);

        public static Genre genre() {
            return GENRE;
        }
    }
}
