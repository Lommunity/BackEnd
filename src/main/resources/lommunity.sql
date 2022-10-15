create table regions (
       code bigint not null,
        fullname varchar(255),
        level bigint,
        parent_code bigint,
        primary key (code)
    ) engine=InnoDB;

create table users (
       user_id bigint not null auto_increment,
        nickname varchar(255),
        profile_image_url varchar(255),
        provider varchar(255),
        provider_id varchar(255),
        registered bit not null,
        role integer,
        region_code bigint,
        primary key (user_id)
    ) engine=InnoDB;

alter table users
       add constraint PROVIDER_PROVIDERID_UNIQUE unique (provider, provider_id);

alter table users
       add constraint FKteu8yx1rqd4601bkvqjf2nypi
       foreign key (region_code)
       references regions (code);

create table posts (
       post_id bigint not null auto_increment,
        created_date datetime,
        last_modified_date datetime,
        created_by bigint,
        last_modified_by bigint,
        content varchar(255),
        post_image_urls varchar(500),
        topic_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

alter table posts
       add constraint FK5lidm6cqbc7u4xhqpxm898qme
       foreign key (user_id)
       references users (user_id);

create table comments (
       comment_id bigint not null auto_increment,
        created_date datetime,
        last_modified_date datetime,
        content varchar(255),
        post_id bigint,
        user_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

alter table comments
       add constraint FKh4c7lvsc298whoyd4w9ta25cr
       foreign key (post_id)
       references posts (post_id);

alter table comments
       add constraint FK8omq0tc18jd43bu5tjh6jvraq
       foreign key (user_id)
       references users (user_id);

create table likes (
       like_id bigint not null auto_increment,
        target_id bigint,
        target_type varchar(255),
        user_id bigint,
        primary key (like_id)
    ) engine=InnoDB;

alter table likes
       add constraint FKnvx9seeqqyy71bij291pwiwrg
       foreign key (user_id)
       references users (user_id);