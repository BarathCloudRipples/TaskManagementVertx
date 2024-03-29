CREATE DATABASE IF NOT EXISTS "UserDatabase"
  WITH OWNER = postgres

    
CREATE TABLE IF NOT EXISTS user_details
(
	user_id character varying(50) NOT NULL,
	first_name character varying(50) NOT NULL,
	last_name character varying(50),
	designation character varying(50) NOT NULL,
	username character varying(50) NOT NULL,
	email character varying(50) NOT NULL,
	password character varying(50) NOT NULL,
	role_id integer  NOT NULL,
	PRIMARY KEY (user_id),
	FOREIGN KEY (role_id) REFERENCES role_details(role_id)
);


CREATE TABLE IF NOT EXISTS role_details
(
	role_id integer NOT NULL,
	rolename character varying(50) NOT NULL,
	PRIMARY KEY (role_id)
);


INSERT INTO public.role_details
	(role_id, role_name) 
VALUES 
	(1, 'admin'),
	(2, 'user');
	
	
CREATE TABLE IF NOT EXISTS public.task_details
(
    task_id serial,
    title character varying(50),
    description character varying(50),
    status character varying(50)",
    timeline character varying(50),
    assign_to character varying(50) NOT NULL,
    PRIMARY KEY (assign_to)
);


CREATE SEQUENCE IF NOT EXISTS task_details_task_id_seq
    INCREMENT BY 1
    START WITH 1
    MINVALUE 0;
    

