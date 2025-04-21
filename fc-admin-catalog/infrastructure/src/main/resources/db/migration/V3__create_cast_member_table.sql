CREATE TABLE cast_members (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,

    CONSTRAINT pk_cast_members PRIMARY KEY (id)
);