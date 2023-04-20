create SCHEMA IF NOT EXISTS subscription;
drop table if exists subscription.countries;
create table subscription.countries
(
    idcountries          int primary key,
    iso varchar2 not null,
    name varchar2 not null,
    displayname varchar2 not null,
    iso3 varchar2,
    numcode              smallint,
    phonecode            int,
    region               varchar,
    sub_region           varchar,
    population           bigint,
    internet_users       bigint,
    internet_penetration double,
    piracy_rate          double,
    active               tinyint,
    accessibletier varchar2 not null
);

insert into subscription.countries (idcountries, iso, name, displayname, iso3, numcode, phonecode,
                                    region, sub_region, population, internet_users,
                                    internet_penetration, piracy_rate, active, accessibletier)
values (1, 'AF', 'AFGHANISTAN', 'Afghanistan', 'AFG', 4, 93, 'Asia', 'Central Asia', 35530081,
        6003183, 16.9, 0, 1, 'Enterprise'),
       (2, 'AL', 'ALBANIA', 'Albania', 'ALB', 8, 355, 'Europe', 'Southeast Europe', 2930187,
        1932024, 65.94, 0, 1, 'Enterprise'),
       (3, 'DZ', 'ALGERIA', 'Algeria', 'DZA', 12, 213, 'Africa', 'Northern Africa', 41318142,
        18580000, 44.97, 0, 1, 'Enterprise'),
       (4, 'AS', 'AMERICAN SAMOA', 'American Samoa', 'ASM', 16, 1684, 'Oceania', 'Oceania', 55641,
        22000, 39.54, 0, 0, 'Enterprise'),
       (5, 'AD', 'ANDORRA', 'Andorra', 'AND', 20, 376, 'Europe', 'Southern Europe', 76965, 67305,
        87.45, 0, null, 'Enterprise'),
       (6, 'AO', 'ANGOLA', 'Angola', 'AGO', 24, 244, 'Africa', 'Central Africa', 29784193, 5951453,
        19.98, 0, 0, 'Enterprise'),
       (7, 'AI', 'ANGUILLA', 'Anguilla', 'AIA', 660, 1264, 'Caribbean', 'Leeward Islands', 14909,
        11557, 77.52, 0, 0, 'Enterprise'),
       (8, 'AQ', 'ANTARCTICA', 'Antarctica', 'HLD', 0, 0, 'Oceania', 'Oceania', 2700, 2700, 100, 0,
        null, 'Enterprise'),
       (9, 'AG', 'ANTIGUA AND BARBUDA', 'Antigua and Barbuda', 'ATG', 28, 1268, 'Caribbean',
        'Leeward Islands', 102012, 81545, 79.94, 0, null, 'Enterprise'),
       (10, 'US', 'USA', 'United States', 'USA', 32, 54, 'Latin America', 'South America', 44271041,
        41586960, 93.94, 0, 1, 'Enterprise');

drop table if exists subscription.subscriptions;

create table subscription.subscriptions
(
    idaccount         int unsigned  not null,
    subscriptiontype varchar2 not null,
    subscriptionrefid int unsigned  not null,
    subscriptionstart date,
    state             int default 1 not null,
    primary key (idaccount, subscriptionrefid, subscriptiontype)
);

insert into subscription.subscriptions (idaccount, subscriptiontype, subscriptionrefid,
                                        subscriptionstart, state)
values (21, 'market', 2, '2015-04-15', 1),
       (22, 'market', 3, '2015-04-15', 1),
       (23, 'market', 10, '2015-04-15', 1),
       (23, 'market', 13, '2015-04-15', 1),
       (23, 'market', 14, '2015-04-15', 1),
       (99, 'market', 1, '2015-04-15', 1),
       (99, 'market', 10, '2017-07-12', 1),
       (11, 'tvtitle', 123456, '2015-04-15', 1),
       (99, 'tvtitle', 123456, '2015-04-15', 1),
       (99, 'tvtitle', 3333.0, '2015-04-15', 1),
       (20, 'movie', 6, '2015-04-15', 1);