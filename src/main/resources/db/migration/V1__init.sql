CREATE TABLE member
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime     NOT NULL,
    kakao_id      VARCHAR(255) NOT NULL,
    nickname      VARCHAR(255) NULL,
    profile_image VARCHAR(255) NULL,
    save_count    INT          NULL,
    badge_count   INT          NULL,
    auto_save     BIT(1)                NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

CREATE TABLE article
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime     NOT NULL,
    image            VARCHAR(255) NULL,
    title            VARCHAR(255) NULL,
    introduction     TEXT         NULL,
    content          TEXT         NULL,
    article_category VARCHAR(255) NULL,
    CONSTRAINT pk_article PRIMARY KEY (id)
);

CREATE TABLE articlecase
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    member_id  BIGINT   NOT NULL,
    article_id BIGINT   NOT NULL,
    CONSTRAINT pk_articlecase PRIMARY KEY (id)
);

CREATE TABLE badge
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime     NOT NULL,
    name          VARCHAR(255) NULL,
    description   TEXT       NULL,
    image         VARCHAR(255) NULL,
    progress_type VARCHAR(255) NULL,
    goal          INT          NULL,
    CONSTRAINT pk_badge PRIMARY KEY (id)
);

CREATE TABLE book
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime     NOT NULL,
    image         VARCHAR(255) NULL,
    title         VARCHAR(255) NULL,
    writer        VARCHAR(255) NULL,
    publisher     VARCHAR(255) NULL,
    size          VARCHAR(255) NULL,
    publish_date  date         NULL,
    page          INT          NULL,
    introduction  TEXT         NULL,
    content       TEXT         NULL,
    book_category VARCHAR(255) NOT NULL,
    subject       VARCHAR(255) NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE book_article
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    book_id    BIGINT   NOT NULL,
    article_id BIGINT   NOT NULL,
    CONSTRAINT pk_book_article PRIMARY KEY (id)
);

CREATE TABLE bookcase
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    member_id  BIGINT   NOT NULL,
    book_id    BIGINT   NOT NULL,
    CONSTRAINT pk_bookcase PRIMARY KEY (id)
);

CREATE TABLE member_badge
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    member_id  BIGINT   NOT NULL,
    badge_id   BIGINT   NOT NULL,
    CONSTRAINT pk_member_badge PRIMARY KEY (id)
);

CREATE TABLE recent_view_article
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    member_id  BIGINT   NOT NULL,
    article_id BIGINT   NOT NULL,
    view_date  datetime NULL,
    CONSTRAINT pk_recent_view_article PRIMARY KEY (id)
);

CREATE TABLE recent_view_book
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NOT NULL,
    member_id  BIGINT   NOT NULL,
    book_id    BIGINT   NOT NULL,
    view_date  datetime NULL,
    CONSTRAINT pk_recent_view_book PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_kakao UNIQUE (kakao_id);

ALTER TABLE articlecase
    ADD CONSTRAINT FK_ARTICLECASE_ON_ARTICLE FOREIGN KEY (article_id) REFERENCES article (id);

ALTER TABLE articlecase
    ADD CONSTRAINT FK_ARTICLECASE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE bookcase
    ADD CONSTRAINT FK_BOOKCASE_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE bookcase
    ADD CONSTRAINT FK_BOOKCASE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE book_article
    ADD CONSTRAINT FK_BOOK_ARTICLE_ON_ARTICLE FOREIGN KEY (article_id) REFERENCES article (id);

ALTER TABLE book_article
    ADD CONSTRAINT FK_BOOK_ARTICLE_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE member_badge
    ADD CONSTRAINT FK_MEMBER_BADGE_ON_BADGE FOREIGN KEY (badge_id) REFERENCES badge (id);

ALTER TABLE member_badge
    ADD CONSTRAINT FK_MEMBER_BADGE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE recent_view_article
    ADD CONSTRAINT FK_RECENT_VIEW_ARTICLE_ON_ARTICLE FOREIGN KEY (article_id) REFERENCES article (id);

ALTER TABLE recent_view_article
    ADD CONSTRAINT FK_RECENT_VIEW_ARTICLE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE recent_view_book
    ADD CONSTRAINT FK_RECENT_VIEW_BOOK_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE recent_view_book
    ADD CONSTRAINT FK_RECENT_VIEW_BOOK_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);