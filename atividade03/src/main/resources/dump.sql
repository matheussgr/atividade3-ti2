--
-- PostgreSQL database dump
--

-- Dumped from database version 13.6 (Ubuntu 13.6-0ubuntu0.21.10.1)
-- Dumped by pg_dump version 13.6 (Ubuntu 13.6-0ubuntu0.21.10.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: id-servico; Type: SEQUENCE; Schema: public; Owner: matheussgr
--

CREATE SEQUENCE public."id-servico"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 1000000
    CACHE 1;


ALTER TABLE public."id-servico" OWNER TO matheussgr;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: servico; Type: TABLE; Schema: public; Owner: matheussgr
--

CREATE TABLE public.servico (
    id integer DEFAULT nextval('public."id-servico"'::regclass) NOT NULL,
    nome text,    
    descricao text,
    preco double precision,
);


ALTER TABLE public.servico OWNER TO matheussgr;

--
-- Name: servico servico_pkey; Type: CONSTRAINT; Schema: public; Owner: matheussgr
--

ALTER TABLE ONLY public.servico
    ADD CONSTRAINT servico_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--    