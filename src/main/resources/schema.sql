CREATE TABLE event
(
    event_id     BIGSERIAL PRIMARY KEY,
    topic        VARCHAR(255) NOT NULL,
    event_type   VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_data   JSONB        NOT NULL,
    created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_offset
(
    consumer VARCHAR(100) NOT NULL,
    topic    VARCHAR(100) NOT NULL,
    offset   BIGINT       NOT NULL,
    PRIMARY KEY (consumer, topic)
);

CREATE TABLE product
(
    product_id  VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    image_url   VARCHAR(255),
    category    VARCHAR(100),
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