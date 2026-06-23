-- ** Database generated with pgModeler (PostgreSQL Database Modeler).
-- ** pgModeler version: 2.0.0-alpha1
-- ** PostgreSQL version: 18.0
-- ** Project Site: pgmodeler.io
-- ** Model Author: ---

-- ** Database creation must be performed outside a multi lined SQL file. 
-- ** These commands were put in this file only as a convenience.

-- object: aadhaar_esign | type: DATABASE --
-- DROP DATABASE IF EXISTS aadhaar_esign;
CREATE DATABASE aadhaar_esign;
-- ddl-end --


-- object: lookup | type: SCHEMA --
-- DROP SCHEMA IF EXISTS lookup CASCADE;
CREATE SCHEMA lookup;
-- ddl-end --
ALTER SCHEMA lookup OWNER TO postgres;
-- ddl-end --

SET search_path TO pg_catalog,public,lookup;
-- ddl-end --

-- object: public.application | type: TABLE --
-- DROP TABLE IF EXISTS public.application CASCADE;
CREATE TABLE public.application (
	application_id bigserial NOT NULL,
	application_name varchar(250) NOT NULL,
	application_code varchar(20) NOT NULL,
	webhook_url text,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT application_pk PRIMARY KEY (application_id),
	CONSTRAINT uk_application_application_code UNIQUE (application_code)
);
-- ddl-end --
ALTER TABLE public.application OWNER TO postgres;
-- ddl-end --

-- object: public.signature_provider | type: TABLE --
-- DROP TABLE IF EXISTS public.signature_provider CASCADE;
CREATE TABLE public.signature_provider (
	signature_provider_id smallint NOT NULL,
	provider_code varchar(20) NOT NULL,
	provider_name varchar(250) NOT NULL,
	priority smallint NOT NULL DEFAULT 0,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	CONSTRAINT signature_provider_pk PRIMARY KEY (signature_provider_id),
	CONSTRAINT uk_signature_provider_provider_code UNIQUE (provider_code)
);
-- ddl-end --
ALTER TABLE public.signature_provider OWNER TO postgres;
-- ddl-end --

-- object: lookup.provider_cd | type: TABLE --
-- DROP TABLE IF EXISTS lookup.provider_cd CASCADE;
CREATE TABLE lookup.provider_cd (
	provider_cd smallint NOT NULL,
	code varchar(250) NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT provider_cd_pk PRIMARY KEY (provider_cd)
);
-- ddl-end --
ALTER TABLE lookup.provider_cd OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.provider_cd (provider_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'1', E'AADHAAR_ESIGN', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.provider_cd (provider_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'2', E'DSC', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.provider_cd (provider_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'3', E'ESTAMP', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: lookup.locale_cd | type: TABLE --
-- DROP TABLE IF EXISTS lookup.locale_cd CASCADE;
CREATE TABLE lookup.locale_cd (
	locale_cd smallint NOT NULL,
	code varchar(5) NOT NULL,
	description varchar(250),
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT locale_cd_pk PRIMARY KEY (locale_cd)
);
-- ddl-end --
COMMENT ON COLUMN lookup.locale_cd.description IS E'Full form of language';
-- ddl-end --
ALTER TABLE lookup.locale_cd OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.locale_cd (locale_cd, code, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'1', E'en_IN', E'English(India)', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: lookup.provider_locale | type: TABLE --
-- DROP TABLE IF EXISTS lookup.provider_locale CASCADE;
CREATE TABLE lookup.provider_locale (
	provider_cd smallint NOT NULL,
	locale_cd smallint NOT NULL,
	description varchar(100) NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text

);
-- ddl-end --
ALTER TABLE lookup.provider_locale OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.provider_locale (provider_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'1', E'1', E'Aadhaar eSign', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.provider_locale (provider_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'2', E'1', E'Digital Signature Certificate', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.provider_locale (provider_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'3', E'1', E'eStamp', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: provider_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.provider_locale DROP CONSTRAINT IF EXISTS provider_cd_fk CASCADE;
ALTER TABLE lookup.provider_locale ADD CONSTRAINT provider_cd_fk FOREIGN KEY (provider_cd)
REFERENCES lookup.provider_cd (provider_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: provider_locale_provider_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.provider_locale_provider_cd_idx CASCADE;
CREATE INDEX provider_locale_provider_cd_idx ON lookup.provider_locale
USING btree
(
	provider_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.provider_capability | type: TABLE --
-- DROP TABLE IF EXISTS public.provider_capability CASCADE;
CREATE TABLE public.provider_capability (
	provider_capability_id serial NOT NULL,
	signature_provider_id smallint NOT NULL,
	provider_cd smallint NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	CONSTRAINT provider_capability_pk PRIMARY KEY (provider_capability_id)
);
-- ddl-end --
COMMENT ON COLUMN public.provider_capability.start_time IS E'same as created_at';
-- ddl-end --
COMMENT ON COLUMN public.provider_capability.end_time IS E'same as updated_at';
-- ddl-end --
ALTER TABLE public.provider_capability OWNER TO postgres;
-- ddl-end --

-- object: provider_cd_fk | type: CONSTRAINT --
-- ALTER TABLE public.provider_capability DROP CONSTRAINT IF EXISTS provider_cd_fk CASCADE;
ALTER TABLE public.provider_capability ADD CONSTRAINT provider_cd_fk FOREIGN KEY (provider_cd)
REFERENCES lookup.provider_cd (provider_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: provider_capability_provider_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.provider_capability_provider_cd_idx CASCADE;
CREATE INDEX provider_capability_provider_cd_idx ON public.provider_capability
USING btree
(
	provider_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: lookup.environment_cd | type: TABLE --
-- DROP TABLE IF EXISTS lookup.environment_cd CASCADE;
CREATE TABLE lookup.environment_cd (
	environment_cd smallint NOT NULL,
	code varchar(10) NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT environment_cd_pk PRIMARY KEY (environment_cd)
);
-- ddl-end --
ALTER TABLE lookup.environment_cd OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.environment_cd (environment_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'1', E'DEV', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.environment_cd (environment_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'2', E'UAT', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.environment_cd (environment_cd, code, start_time, end_time, created_at, updated_at, update_info) VALUES (E'3', E'PRO', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: lookup.environment_locale | type: TABLE --
-- DROP TABLE IF EXISTS lookup.environment_locale CASCADE;
CREATE TABLE lookup.environment_locale (
	environment_cd smallint NOT NULL,
	locale_cd smallint NOT NULL,
	description varchar(100) NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text

);
-- ddl-end --
ALTER TABLE lookup.environment_locale OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.environment_locale (environment_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'1', E'1', E'DEV', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.environment_locale (environment_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'2', E'1', E'UAT', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.environment_locale (environment_cd, locale_cd, description, start_time, end_time, created_at, updated_at, update_info) VALUES (E'3', E'1', E'PRO', DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: environment_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.environment_locale DROP CONSTRAINT IF EXISTS environment_cd_fk CASCADE;
ALTER TABLE lookup.environment_locale ADD CONSTRAINT environment_cd_fk FOREIGN KEY (environment_cd)
REFERENCES lookup.environment_cd (environment_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: environment_locale_environment_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.environment_locale_environment_cd_idx CASCADE;
CREATE INDEX environment_locale_environment_cd_idx ON lookup.environment_locale
USING btree
(
	environment_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: locale_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.environment_locale DROP CONSTRAINT IF EXISTS locale_cd_fk CASCADE;
ALTER TABLE lookup.environment_locale ADD CONSTRAINT locale_cd_fk FOREIGN KEY (locale_cd)
REFERENCES lookup.locale_cd (locale_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: locale_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.locale_cd_idx CASCADE;
CREATE INDEX locale_cd_idx ON lookup.environment_locale
USING btree
(
	locale_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.provider_configuration | type: TABLE --
-- DROP TABLE IF EXISTS public.provider_configuration CASCADE;
CREATE TABLE public.provider_configuration (
	provider_configuration_id bigserial NOT NULL,
	signature_provider_id smallint NOT NULL,
	environment_cd smallint NOT NULL,
	api_url text NOT NULL,
	health_url text,
	timeout_ms integer NOT NULL,
	retry_count smallint NOT NULL DEFAULT 0,
	api_key varchar(250),
	secret varchar(150),
	certificate_reference text,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT provider_configuration_pk PRIMARY KEY (provider_configuration_id)
);
-- ddl-end --
COMMENT ON COLUMN public.provider_configuration.secret IS E'certificate password';
-- ddl-end --
COMMENT ON COLUMN public.provider_configuration.certificate_reference IS E'Certificate location(/home/thirumal/certificate/thirumal.pfx)';
-- ddl-end --
ALTER TABLE public.provider_configuration OWNER TO postgres;
-- ddl-end --

-- object: signature_provider_fk | type: CONSTRAINT --
-- ALTER TABLE public.provider_configuration DROP CONSTRAINT IF EXISTS signature_provider_fk CASCADE;
ALTER TABLE public.provider_configuration ADD CONSTRAINT signature_provider_fk FOREIGN KEY (signature_provider_id)
REFERENCES public.signature_provider (signature_provider_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: provider_configuration_signature_provider_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.provider_configuration_signature_provider_idx CASCADE;
CREATE INDEX provider_configuration_signature_provider_idx ON public.provider_configuration
USING btree
(
	signature_provider_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: environment_cd_fk | type: CONSTRAINT --
-- ALTER TABLE public.provider_configuration DROP CONSTRAINT IF EXISTS environment_cd_fk CASCADE;
ALTER TABLE public.provider_configuration ADD CONSTRAINT environment_cd_fk FOREIGN KEY (environment_cd)
REFERENCES lookup.environment_cd (environment_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: environment_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.environment_cd_idx CASCADE;
CREATE INDEX environment_cd_idx ON public.provider_configuration
USING btree
(
	environment_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: locale_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.provider_locale DROP CONSTRAINT IF EXISTS locale_cd_fk CASCADE;
ALTER TABLE lookup.provider_locale ADD CONSTRAINT locale_cd_fk FOREIGN KEY (locale_cd)
REFERENCES lookup.locale_cd (locale_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: provider_locale_locale_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.provider_locale_locale_cd_idx CASCADE;
CREATE INDEX provider_locale_locale_cd_idx ON lookup.provider_locale
USING btree
(
	locale_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: signature_provider_fk | type: CONSTRAINT --
-- ALTER TABLE public.provider_capability DROP CONSTRAINT IF EXISTS signature_provider_fk CASCADE;
ALTER TABLE public.provider_capability ADD CONSTRAINT signature_provider_fk FOREIGN KEY (signature_provider_id)
REFERENCES public.signature_provider (signature_provider_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: provider_capability_signature_provider_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.provider_capability_signature_provider_idx CASCADE;
CREATE INDEX provider_capability_signature_provider_idx ON public.provider_capability
USING btree
(
	signature_provider_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.signature_request | type: TABLE --
-- DROP TABLE IF EXISTS public.signature_request CASCADE;
CREATE TABLE public.signature_request (
	signature_request_id bigserial NOT NULL,
	application_id bigint NOT NULL,
	status_cd smallint NOT NULL,
	signature_request_uuid uuid NOT NULL DEFAULT uuidv7(),
	reference_no varchar(250),
	original_document_hash varchar(64),
	original_document_path text,
	request_payload jsonb,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	update_info text,
	CONSTRAINT signature_request_pk PRIMARY KEY (signature_request_id),
	CONSTRAINT uk_signature_request_uuid UNIQUE (signature_request_uuid)
);
-- ddl-end --
COMMENT ON COLUMN public.signature_request.signature_request_uuid IS E'UUID for AADHAAR e-sign orchestration';
-- ddl-end --
ALTER TABLE public.signature_request OWNER TO postgres;
-- ddl-end --

-- object: public.signed_document | type: TABLE --
-- DROP TABLE IF EXISTS public.signed_document CASCADE;
CREATE TABLE public.signed_document (
	signed_document_id bigserial NOT NULL,
	signature_request_id bigint NOT NULL,
	storage_path text,
	signed_hash varchar(64),
	signed_time timestamptz NOT NULL,
	signer_name varchar(150),
	signer_yob varchar(4),
	signer_gender varchar(10),
	signer_pin varchar(10),
	signer_aadhaar_suffix varchar(4),
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	CONSTRAINT signed_document_pk PRIMARY KEY (signed_document_id)
);
-- ddl-end --
COMMENT ON COLUMN public.signed_document.signer_name IS E'Aadhaar signer name';
-- ddl-end --
COMMENT ON COLUMN public.signed_document.signer_yob IS E'Aadhaar signer YOB';
-- ddl-end --
ALTER TABLE public.signed_document OWNER TO postgres;
-- ddl-end --

-- object: signature_request_fk | type: CONSTRAINT --
-- ALTER TABLE public.signed_document DROP CONSTRAINT IF EXISTS signature_request_fk CASCADE;
ALTER TABLE public.signed_document ADD CONSTRAINT signature_request_fk FOREIGN KEY (signature_request_id)
REFERENCES public.signature_request (signature_request_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signed_document_signature_request_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signed_document_signature_request_idx CASCADE;
CREATE INDEX signed_document_signature_request_idx ON public.signed_document
USING btree
(
	signature_request_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: application_fk | type: CONSTRAINT --
-- ALTER TABLE public.signature_request DROP CONSTRAINT IF EXISTS application_fk CASCADE;
ALTER TABLE public.signature_request ADD CONSTRAINT application_fk FOREIGN KEY (application_id)
REFERENCES public.application (application_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signature_request_application_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signature_request_application_idx CASCADE;
CREATE INDEX signature_request_application_idx ON public.signature_request
USING btree
(
	application_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: lookup.status_cd | type: TABLE --
-- DROP TABLE IF EXISTS lookup.status_cd CASCADE;
CREATE TABLE lookup.status_cd (
	status_cd smallint NOT NULL,
	code varchar NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	CONSTRAINT status_cd_pk PRIMARY KEY (status_cd)
);
-- ddl-end --
ALTER TABLE lookup.status_cd OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.status_cd (status_cd, code, start_time, end_time, created_at, updated_at) VALUES (E'0', E'Unkown', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_cd (status_cd, code, start_time, end_time, created_at, updated_at) VALUES (E'1', E'PENDING', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_cd (status_cd, code, start_time, end_time, created_at, updated_at) VALUES (E'2', E'SUCCESS', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_cd (status_cd, code, start_time, end_time, created_at, updated_at) VALUES (E'3', E'FAILED', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: lookup.status_locale | type: TABLE --
-- DROP TABLE IF EXISTS lookup.status_locale CASCADE;
CREATE TABLE lookup.status_locale (
	status_cd smallint NOT NULL,
	locale_cd smallint NOT NULL,
	description varchar(75) NOT NULL,
	start_time timestamptz NOT NULL DEFAULT current_timestamp,
	end_time timestamptz NOT NULL DEFAULT 'infinity'::timestamp,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp

);
-- ddl-end --
ALTER TABLE lookup.status_locale OWNER TO postgres;
-- ddl-end --

INSERT INTO lookup.status_locale (status_cd, locale_cd, description, start_time, end_time, created_at, updated_at) VALUES (E'0', E'1', E'UNKNOWN', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_locale (status_cd, locale_cd, description, start_time, end_time, created_at, updated_at) VALUES (E'1', E'1', E'PENDING', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_locale (status_cd, locale_cd, description, start_time, end_time, created_at, updated_at) VALUES (E'2', E'1', E'SUCCESS', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --
INSERT INTO lookup.status_locale (status_cd, locale_cd, description, start_time, end_time, created_at, updated_at) VALUES (E'3', E'1', E'FAILED', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
-- ddl-end --

-- object: status_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.status_locale DROP CONSTRAINT IF EXISTS status_cd_fk CASCADE;
ALTER TABLE lookup.status_locale ADD CONSTRAINT status_cd_fk FOREIGN KEY (status_cd)
REFERENCES lookup.status_cd (status_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: status_locale_status_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.status_locale_status_cd_idx CASCADE;
CREATE INDEX status_locale_status_cd_idx ON lookup.status_locale
USING btree
(
	status_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: locale_cd_fk | type: CONSTRAINT --
-- ALTER TABLE lookup.status_locale DROP CONSTRAINT IF EXISTS locale_cd_fk CASCADE;
ALTER TABLE lookup.status_locale ADD CONSTRAINT locale_cd_fk FOREIGN KEY (locale_cd)
REFERENCES lookup.locale_cd (locale_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: status_locale_locale_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS lookup.status_locale_locale_cd_idx CASCADE;
CREATE INDEX status_locale_locale_cd_idx ON lookup.status_locale
USING btree
(
	locale_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: status_cd_fk | type: CONSTRAINT --
-- ALTER TABLE public.signature_request DROP CONSTRAINT IF EXISTS status_cd_fk CASCADE;
ALTER TABLE public.signature_request ADD CONSTRAINT status_cd_fk FOREIGN KEY (status_cd)
REFERENCES lookup.status_cd (status_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signature_request_status_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signature_request_status_cd_idx CASCADE;
CREATE INDEX signature_request_status_cd_idx ON public.signature_request
USING btree
(
	status_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: public.signature_attempt | type: TABLE --
-- DROP TABLE IF EXISTS public.signature_attempt CASCADE;
CREATE TABLE public.signature_attempt (
	signature_attempt_id bigserial NOT NULL,
	signature_request_id bigint NOT NULL,
	provider_configuration_id bigint NOT NULL,
	status_cd smallint NOT NULL,
	request_sent_at timestamptz NOT NULL DEFAULT current_timestamp,
	response_received_at timestamptz,
	provider_transaction_id varchar(75),
	request_payload jsonb,
	response_payload jsonb,
	http_status smallint,
	provider_status_code smallint,
	provider_status_message text,
	error_code varchar(15),
	error_message text,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp,
	CONSTRAINT signature_attempt_pk PRIMARY KEY (signature_attempt_id)
);
-- ddl-end --
COMMENT ON TABLE public.signature_attempt IS E'Number of attempt can be calulated using signature_request_id. Latency can be calculated from the difference between request_sent_at and response_received_at';
-- ddl-end --
ALTER TABLE public.signature_attempt OWNER TO postgres;
-- ddl-end --

-- object: signature_request_fk | type: CONSTRAINT --
-- ALTER TABLE public.signature_attempt DROP CONSTRAINT IF EXISTS signature_request_fk CASCADE;
ALTER TABLE public.signature_attempt ADD CONSTRAINT signature_request_fk FOREIGN KEY (signature_request_id)
REFERENCES public.signature_request (signature_request_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signature_attempt_signature_request_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signature_attempt_signature_request_idx CASCADE;
CREATE INDEX signature_attempt_signature_request_idx ON public.signature_attempt
USING btree
(
	signature_request_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: provider_configuration_fk | type: CONSTRAINT --
-- ALTER TABLE public.signature_attempt DROP CONSTRAINT IF EXISTS provider_configuration_fk CASCADE;
ALTER TABLE public.signature_attempt ADD CONSTRAINT provider_configuration_fk FOREIGN KEY (provider_configuration_id)
REFERENCES public.provider_configuration (provider_configuration_id) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signature_attempt_provider_configuration_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signature_attempt_provider_configuration_idx CASCADE;
CREATE INDEX signature_attempt_provider_configuration_idx ON public.signature_attempt
USING btree
(
	provider_configuration_id
)
WITH (FILLFACTOR = 90);
-- ddl-end --

-- object: status_cd_fk | type: CONSTRAINT --
-- ALTER TABLE public.signature_attempt DROP CONSTRAINT IF EXISTS status_cd_fk CASCADE;
ALTER TABLE public.signature_attempt ADD CONSTRAINT status_cd_fk FOREIGN KEY (status_cd)
REFERENCES lookup.status_cd (status_cd) MATCH FULL
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: signature_attempt_status_cd_idx | type: INDEX --
-- DROP INDEX IF EXISTS public.signature_attempt_status_cd_idx CASCADE;
CREATE INDEX signature_attempt_status_cd_idx ON public.signature_attempt
USING btree
(
	status_cd
)
WITH (FILLFACTOR = 90);
-- ddl-end --


