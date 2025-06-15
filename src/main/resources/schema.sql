DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS event_processed;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS inventory;

CREATE TABLE event
(
    id           BIGSERIAL PRIMARY KEY,
    topic        VARCHAR(255) NOT NULL,
    type         VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    data         JSONB        NOT NULL,
    metadata     JSONB     DEFAULT '{}'::jsonb,
    created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product
(
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    image_url   VARCHAR(255),
    category    VARCHAR(100),
    created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    extensions  JSONB     DEFAULT '{}'::jsonb
);

CREATE TABLE inventory
(
    product_id VARCHAR(255) PRIMARY KEY,
    quantity   BIGINT NOT NULL DEFAULT 0,
    updated    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    extensions JSONB           DEFAULT '{}'::jsonb
);
