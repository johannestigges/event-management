create table ev_instrument (
    id              int             not null auto_increment,
    instrument      varchar(100)    not null,
    gruppe          varchar(100)    not null,
    primary key(id)
);

create table ev_user (
    id              int             not null auto_increment,
    version         int             not null,
    vorname         varchar(100)    not null,
    nachname        varchar(100)    not null,
    status          varchar(20)     not null,
    instrument_id   int             null,
    primary key(id),
    foreign key (instrument_id) references ev_instrument(id)
    );

create table ev_event (
    id              int             not null auto_increment,
    version         int             not null,
    name            varchar(200)    not null,
    start_at        datetime        not null,
    end_at          datetime        not null,
    primary key(id)
);

create table ev_participant (
    event_id        int             not null,
    user_id         int             not null,
    participate     boolean         not null,
    primary key(event_id, user_id),
    foreign key (event_id) references ev_event(id),
    foreign key (user_id) references ev_user(id)
);

create table ev_protocol (
    id              int             not null auto_increment,
    created_at      datetime        not null,
    type            varchar(20)     not null,
    entity_type     varchar(100)    not null,
    entity_id       int             not null,
    data            varchar(4000),
    primary key(id)
);
