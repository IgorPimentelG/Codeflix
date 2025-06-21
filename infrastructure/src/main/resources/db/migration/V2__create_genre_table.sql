CREATE TABLE genres (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    deleted_at DATETIME(6) NULL,

    CONSTRAINT pk_genres PRIMARY KEY (id)
);

CREATE TABLE genres_categories (
    genre_id CHAR(36) NOT NULL,
    category_id CHAR(36) NOT NULL,

    CONSTRAINT idx_genre_category UNIQUE (genre_id, category_id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);