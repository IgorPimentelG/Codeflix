CREATE TABLE videos_video_media (
    checksum CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    encoded_path VARCHAR(500) NOT NULL,
    media_status VARCHAR(50) NOT NULL,

    CONSTRAINT pk_video_media PRIMARY KEY (checksum)
);

CREATE TABLE videos_image_media (
    checksum CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,

    CONSTRAINT pk_image_media PRIMARY KEY (checksum)
);

CREATE TABLE videos (
    id CHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    year_launched SMALLINT NOT NULL,
    opened BOOLEAN NOT NULL DEFAULT FALSE,
    published BOOLEAN NOT NULL DEFAULT FALSE,
    rating VARCHAR(10),
    duration DECIMAL(5, 2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,

    video_id CHAR(36) NULL,
    trailer_id CHAR(36) NULL,
    banner_id CHAR(36) NULL,
    thumbnail_id CHAR(36) NULL,
    thumbnail_half_id CHAR(36) NULL,

    CONSTRAINT pk_video PRIMARY KEY (id),
    CONSTRAINT fk_video_id FOREIGN KEY (video_id) REFERENCES videos_video_media (checksum) ON DELETE CASCADE,
    CONSTRAINT fk_trailer_id FOREIGN KEY (trailer_id) REFERENCES videos_video_media (checksum) ON DELETE CASCADE,
    CONSTRAINT fk_banner_id FOREIGN KEY (banner_id) REFERENCES videos_image_media (checksum) ON DELETE CASCADE,
    CONSTRAINT fk_thumbnail_id FOREIGN KEY (thumbnail_id) REFERENCES videos_image_media (checksum) ON DELETE CASCADE,
    CONSTRAINT fk_thumbnail_half_id FOREIGN KEY (thumbnail_half_id) REFERENCES videos_image_media (checksum) ON DELETE CASCADE
);

CREATE TABLE videos_categories (
    video_id CHAR(36) NOT NULL,
    category_id CHAR(36) NOT NULL,

    CONSTRAINT idx_video_category UNIQUE (video_id, category_id),
    CONSTRAINT fk_video_category_video_id FOREIGN KEY (video_id) REFERENCES videos (id),
    CONSTRAINT fk_video_vc_id FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE videos_genres (
    video_id CHAR(36) NOT NULL,
    genre_id CHAR(36) NOT NULL,

    CONSTRAINT idx_video_genre UNIQUE (video_id, genre_id),
    CONSTRAINT fk_video_genre_video_id FOREIGN KEY (video_id) REFERENCES videos (id),
    CONSTRAINT fk_video_vg_id FOREIGN KEY (genre_id) REFERENCES genres (id)
);

CREATE TABLE videos_cast_members (
    video_id CHAR(36) NOT NULL,
    cast_member_id CHAR(36) NOT NULL,

    CONSTRAINT idx_video_cast_member UNIQUE (video_id, cast_member_id),
    CONSTRAINT fk_video_cast_member_video_id FOREIGN KEY (video_id) REFERENCES videos (id),
    CONSTRAINT fk_video_vsm_id FOREIGN KEY (cast_member_id) REFERENCES cast_members (id)
);