CREATE TABLE users
(
    userid       VARCHAR(255) NOT NULL,
    user_name    VARCHAR(255),
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    address      VARCHAR(255),
    phone_number VARCHAR(255),
    email        VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (userid)
);