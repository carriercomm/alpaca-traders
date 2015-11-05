CREATE TABLE item (
  title           varchar(120),
  image           bytea, 
  id              serial primary key
  );

CREATE TABLE server (
  -- game            serial,
  server_name     varchar(60),

  id              serial primary key
);

CREATE TABLE site_user (
  -- ip_address     cidr,
  user_name       varchar(60),
  password        varchar(60),
  prefs           json,

  id              serial primary key
);


CREATE TABLE store (
  user_id         serial references site_user(id),
  server_id       serial references server(id),
  id              serial primary key
);

CREATE TABLE listing (
  copper_price    int,
  quantity        int,
  submitted       timestamp,

  item_id         serial references item(id),
  server_id       serial references server(id),
  submitter_id    serial references site_user(id),

  id              serial primary key
  );