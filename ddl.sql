
-- Drop table

-- DROP TABLE public.projects

CREATE TABLE public.projects (
	id varchar(100) NOT NULL DEFAULT uuid_generate_v1(),
	"name" varchar(50) NOT NULL,
	created date NOT NULL DEFAULT now(),
	updated date NULL,
	description varchar(300) NULL,
	useyn CHAR(1) NOT NULL DEFAULT 'Y',
	CONSTRAINT projects_pkey PRIMARY KEY (id)
);


-- public.menu_selected definition

-- Drop table

-- DROP TABLE public.menu_selected;

CREATE TABLE public.menu_selected (
	id varchar(100) NOT NULL,
	name varchar(30) NOT NULL,
	"date" varchar(30) NOT NULL,
	menu varchar(50) NOT NULL,
	created date NULL,
	updated date NULL,
	projectid varchar(100) NOT NULL,
	division varchar(30) NULL,
	CONSTRAINT pk PRIMARY KEY (id)
);


-- public.menu_selected foreign keys

ALTER TABLE public.menu_selected ADD CONSTRAINT menu_selected_projects_fk FOREIGN KEY (projectid) REFERENCES projects(id);


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- Drop table

-- DROP TABLE public.users

CREATE TABLE public.users (
	id varchar(100) NOT NULL DEFAULT uuid_generate_v1(),
	"name" varchar(50) NOT NULL,
	created date NOT NULL DEFAULT now(),
	updated date NULL,
	description varchar(300) NULL,
	projectid varchar(100) NULL,
	useyn CHAR(1) NOT NULL DEFAULT 'Y',
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_projectid_fkey FOREIGN KEY (projectid) REFERENCES projects(id)
);

-- Drop table

-- DROP TABLE public.restaurants

CREATE TABLE public.restaurants (
	id varchar(100) NOT NULL DEFAULT uuid_generate_v1(),
	"name" varchar(50) NOT NULL,
	created date NOT NULL DEFAULT now(),
	updated date NULL,
	description varchar(300) NULL,
	projectid varchar(100) NULL,
	useyn CHAR(1) NOT NULL DEFAULT 'Y',
	CONSTRAINT restaurants_pkey PRIMARY KEY (id),
	CONSTRAINT restaurants_projectid_fkey FOREIGN KEY (projectid) REFERENCES projects(id)
);






