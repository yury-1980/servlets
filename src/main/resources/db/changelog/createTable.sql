create table if not exists public.client
(
    id
    serial
    primary
    key
    unique,
    client_name
    varchar
(
    10
) not null,
    family_name varchar
(
    20
) not null,
    sur_name varchar
(
    20
) not null,
    birth_day date not null
    );

alter table client
    owner to postgres;

INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (11, 'Пётр', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (19, 'Пётр', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (20, 'Пётр', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (21, 'Пётр', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (22, 'Лёша', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (3, 'Пётр', 'Петров', 'Петрович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (10, 'Иван', 'Иванов', 'Иванович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (9, 'Иван', 'Иванов', 'Иванович', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (24, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (25, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (27, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (29, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (31, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (33, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (35, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (37, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (39, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (41, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (43, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (74, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (76, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (78, 'Вася', 'Васильев', 'Васильевич', '2000-01-01');
INSERT INTO public.client (id, client_name, family_name, sur_name, birth_day)
VALUES (1, 'Пётр', 'Петров', 'Петровия', '2001-02-02');
