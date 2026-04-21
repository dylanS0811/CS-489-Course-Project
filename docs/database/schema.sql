CREATE TABLE roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE app_users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_app_users_role FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE addresses (
    address_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(128) NOT NULL,
    state VARCHAR(32) NOT NULL,
    zip_code VARCHAR(16) NOT NULL
);

CREATE TABLE patients (
    patient_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_number VARCHAR(16) NOT NULL UNIQUE,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    address_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_patients_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

CREATE TABLE dentists (
    dentist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_code VARCHAR(16) NOT NULL UNIQUE,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    specialization VARCHAR(128) NOT NULL
);

CREATE TABLE surgeries (
    surgery_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    surgery_number VARCHAR(16) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    address_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_surgeries_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

CREATE TABLE appointments (
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(32) NOT NULL,
    confirmation_sent BOOLEAN NOT NULL,
    reason VARCHAR(512),
    patient_id BIGINT NOT NULL,
    dentist_id BIGINT NOT NULL,
    surgery_id BIGINT NOT NULL,
    booked_by_user_id BIGINT NOT NULL,
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    CONSTRAINT fk_appointments_dentist FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id),
    CONSTRAINT fk_appointments_surgery FOREIGN KEY (surgery_id) REFERENCES surgeries(surgery_id),
    CONSTRAINT fk_appointments_booked_by FOREIGN KEY (booked_by_user_id) REFERENCES app_users(user_id),
    CONSTRAINT uk_dentist_slot UNIQUE (dentist_id, appointment_date, appointment_time)
);

CREATE TABLE dental_bills (
    bill_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT UNIQUE,
    issue_date DATE NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT fk_bills_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    CONSTRAINT fk_bills_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);
