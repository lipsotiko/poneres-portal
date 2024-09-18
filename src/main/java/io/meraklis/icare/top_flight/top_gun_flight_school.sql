/*
	Assumtions:
		* All instructors are certified to fly all aircraft
        * Only students and instructors may be aboard a flight
        * Aircraft and instructor availability will need to be derived based on their initial availabilty, and current flight schedule
        * All instructors get paid $100/hr
        * Every 1/2 hour of flight costs 50 get paid $100/hr
*/
DROP DATABASE IF EXISTS top_gun_flight_school;
CREATE DATABASE IF NOT EXISTS top_gun_flight_school;
USE top_gun_flight_school;

CREATE TABLE users (
	id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	callsign VARCHAR(255),
	PRIMARY KEY (id)
);
INSERT INTO users (id, first_name, last_name, callsign) VALUES(1, 'Evangelos', 'Poneres', 'Goose');
INSERT INTO users (id, first_name, last_name, callsign) VALUES(2, 'Shawn', 'Nikkila', 'Maverick');
INSERT INTO users (id, first_name, last_name, callsign) VALUES(3, 'Jim', 'Firby', 'Iceman');
SELECT * FROM users;

CREATE TABLE roles (
	id INT NOT NULL,
    name VARCHAR(255),
	PRIMARY KEY (id)
);
INSERT INTO roles (id, name) VALUES(1, 'INSTRUCTOR');
INSERT INTO roles (id, name) VALUES(2, 'STUDENT');
SELECT * FROM roles;

CREATE TABLE user_role (
	user_id INT,
	role_id INT,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY (user_id) REFERENCES users (id),
	FOREIGN KEY (role_id) REFERENCES roles (id)
);
INSERT INTO user_role (user_id, role_id) VALUES(1, 2);
INSERT INTO user_role (user_id, role_id) VALUES(2, 2);
INSERT INTO user_role (user_id, role_id) VALUES(3, 1);
SELECT * FROM user_role;

CREATE TABLE instructor_availability (
	id INT NOT NULL,
    user_id INT NOT NULL,
	start DATE,
    end DATE,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE certifiction (
	id INT NOT NULL,
    name VARCHAR(255),
    expiration_date DATE,
	PRIMARY KEY (id)
);
INSERT INTO certifiction (id, name) VALUES(1, 'MEDICAL');
INSERT INTO certifiction (id, name) VALUES(2, 'SOLO');
SELECT * FROM certifiction;

CREATE TABLE user_certifiction (
	user_id INT,
	certification_id INT,
    expiration_date DATE,
	PRIMARY KEY (user_id, certification_id),
	FOREIGN KEY (user_id) REFERENCES users (id),
	FOREIGN KEY (certification_id) REFERENCES certifiction (id)
);

CREATE TABLE aircraft (
	id INT NOT NULL AUTO_INCREMENT,
	make VARCHAR(255),
	model VARCHAR(255),
    required_pilots INT,
    passanger_capacity INT,
	PRIMARY KEY (id)
);
INSERT INTO aircraft(make, model, required_pilots, passanger_capacity) VALUES ('Boeing', '314 Clipper', 2, 68);
INSERT INTO aircraft(make, model, required_pilots, passanger_capacity) VALUES ('Convair', 'CV-240', 1, 40);
SELECT * FROM aircraft;

CREATE TABLE aircraft_availability (
	id INT NOT NULL AUTO_INCREMENT,
    aircraft_id INT NOT NULL,
	start DATE,
    end DATE,
	PRIMARY KEY (id),
	FOREIGN KEY (aircraft_id) REFERENCES aircraft (id)
);

INSERT INTO aircraft_availability(aircraft_id, start, end) VALUES(1, '2022-01-01', '2022-12-31');
INSERT INTO aircraft_availability(aircraft_id, start, end) VALUES(2, '2022-06-01', '2022-12-31');
SELECT * FROM aircraft_availability;

CREATE TABLE time_blocks(
	id INT NOT NULL,
    duration_in_minutes INT,
    start TIME,
	PRIMARY KEY (id)
);
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(1, 30, '00:00');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(2, 30, '00:30');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(3, 30, '01:30');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(4, 30, '02:00');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(5, 30, '02:30');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(6, 30, '03:00');
INSERT INTO time_blocks(id, duration_in_minutes, start) VALUES(7, 30, '03:30');
SELECT * FROM time_blocks;
-- etc...

CREATE TABLE flight_schedule (
	id INT NOT NULL AUTO_INCREMENT,
    version INT,
	aircraft_id INT NOT NULL,
	flight_date date,
	start DATETIME,
    end DATETIME,
    booked_by INT,
	PRIMARY KEY (id),
	FOREIGN KEY (aircraft_id) REFERENCES aircraft (id),
	FOREIGN KEY (booked_by) REFERENCES users (id)
);

-- Another way to handle scheduling conflicts; not a fan...
CREATE TABLE flight_time_blocks(
    flight_date date,
    aircraft_id INT,
    instructor_id INT,
    time_block_id INT,
	PRIMARY KEY (flight_date, aircraft_id, instructor_id, time_block_id),
	FOREIGN KEY (aircraft_id) REFERENCES aircraft (id),
	FOREIGN KEY (instructor_id) REFERENCES users (id),
	FOREIGN KEY (time_block_id) REFERENCES time_blocks (id)
);

CREATE TABLE flight_schedule_roster (
	id INT NOT NULL AUTO_INCREMENT,
    user_id INT NULL,
    flight_schedule_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (flight_schedule_id) REFERENCES flight_schedule (id),
	FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE flight_invoice (
	id INT NOT NULL AUTO_INCREMENT,
    flight_schedule_id INT NOT NULL,
    payment_id INT NOT NULL,
    payment_status VARCHAR(255),
	PRIMARY KEY (id),
	FOREIGN KEY (flight_schedule_id) REFERENCES flight_schedule (id)
);

CREATE TABLE flight_invoice_item (
	id INT NOT NULL AUTO_INCREMENT,
    flight_invoice_id INT NOT NULL,
    description VARCHAR(255),
    cost DECIMAL(15,2),
	PRIMARY KEY (id),
	FOREIGN KEY (flight_invoice_id) REFERENCES flight_invoice (id)
);

-- Trigger to prevent scheduleing conflicts
DELIMITER $$
CREATE TRIGGER flight_schedule_insert_trigger 
BEFORE INSERT ON flight_schedule
FOR EACH ROW 
BEGIN

	IF (
		SELECT NOT EXISTS (
			SELECT 1
			FROM aircraft_availability a
			WHERE TIMESTAMP(NEW.start) BETWEEN TIMESTAMP(a.start) AND DATE_ADD(TIMESTAMP(a.end), INTERVAL 1 DAY)
			AND TIMESTAMP(NEW.end) BETWEEN TIMESTAMP(a.start) AND DATE_ADD(TIMESTAMP(a.end), INTERVAL 1 DAY)
			LIMIT 1)
    ) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No aircraft availability';
	END IF;

	SET @TMP_START = DATE_ADD(NEW.start, INTERVAL 1 SECOND);
	SET @TMP_END = DATE_ADD(NEW.end, INTERVAL -1 SECOND);
	IF (
		SELECT EXISTS (
			SELECT 1
			FROM flight_schedule b
			WHERE
				TIMESTAMP(@TMP_START) BETWEEN b.start AND b.END
				OR TIMESTAMP(@TMP_END) BETWEEN b.start AND b.END
				OR b.start BETWEEN TIMESTAMP(@TMP_START) AND TIMESTAMP(@TMP_END)
				OR b.end BETWEEN TIMESTAMP(@TMP_START) AND TIMESTAMP(@TMP_END)
			LIMIT 1)
    ) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aircraft is already booked';
	END IF;
    
    -- TO DO: Check for instructor availability;
END$$

-- VALID
INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2022-03-01T12:00', '2022-03-01T14:30', 1);

-- INVALID 
INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2021-01-01T12:00', '2021-03-01T14:30', 1);

INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2022-03-01T11:00', '2022-03-01T14:30', 1);
INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2022-03-01T12:00', '2022-03-01T15:30', 1);
INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2022-03-01T11:00', '2022-03-01T15:30', 1);

-- VALID
INSERT INTO flight_schedule(aircraft_id, start, end, booked_by) VALUES (1, '2022-03-01T10:00', '2022-03-01T12:00', 1);

SELECT * FROM flight_schedule;

--DELETE FROM flight_schedule WHERE aircraft_id = 1;
