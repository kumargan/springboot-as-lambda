drop table temp;


CREATE TABLE temp 
(id SERIAL PRIMARY KEY, 
first_name VARCHAR(50), 
last_name VARCHAR(50), 
email VARCHAR(50), 
mobile_no VARCHAR(50)
);

DROP FUNCTION insert_record;

CREATE FUNCTION insert_record() RETURNS VOID LANGUAGE PLPGSQL AS $$
DECLARE first_name TEXT= STRING_AGG('ganesh','') ;
DECLARE last_name TEXT= STRING_AGG('kumar','');
DECLARE email TEXT= LOWER(CONCAT(first_name, '.', last_name, '@gmail.com'));
DECLARE mobile_no BIGINT=CAST(1000000000 + FLOOR(RANDOM() * 9000000000) AS BIGINT);
BEGIN
INSERT INTO temp (first_name, last_name, email, mobile_no) VALUES (first_name, last_name, email, mobile_no);
END;
$$;

SELECT insert_record() FROM GENERATE_SERIES(1, 10000000);

select count(*) from temp where first_name='ganesh';

delete from temp where id>10000000;

select count(*) from temp where id=36864150;

