
create database scheduler;

use scheduler;

create table Patient(
    idPatient int primary key auto_increment,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    gender enum('Male', 'Female') not null,
    birthDate date not null,
    phoneNumber varchar(20) not null,
    email varchar(60),
    city varchar(80),
    zipCode varchar (12),
    streetName varchar(80),
    houseNumber varchar(8)
);

create table Doctor(
    idDoctor int primary key auto_increment,
    name varchar(50) not null,
    phoneNumber varchar(20) not null,
    gender enum('Male', 'Female') not null,
    birthDate date not null,
    licenseNumber varchar(60)
);

create table Appointment(
    idAppointment int primary key auto_increment,
    date datetime not null,
    description varchar(250),
    status enum ('Unconfirmed', 'Confirmed', 'Canceled', 'Completed') not null,
    price double not null,
    id_patient int not null,
    id_doctor int not null
);

alter table Appointment add constraint FK_PATIENT_APPOINTMENT
FOREIGN KEY(id_patient) REFERENCES Patient(idPatient);

alter table Appointment add constraint FK_DOCTOR_APPOINTMENT
FOREIGN KEY(id_doctor) REFERENCES Doctor(idDoctor);

Create view APPOINTMENT_DATA as SELECT Appointment.idAppointment, Appointment.date, Appointment.description, Appointment.status,
Appointment.price, Patient.idPatient, Patient.firstName, Patient.lastName, Patient.gender as "P_Gender", Patient.birthDate as "P_BIRTHDATE",
Patient.phoneNumber as "P_PHONE", Patient.email, Patient.city, Patient.zipCode, Patient.streetName, Patient.houseNumber,
Doctor.idDoctor, Doctor.name, Doctor.phoneNumber, Doctor.gender, Doctor.birthDate, Doctor.licenseNumber
FROM Appointment INNER JOIN Patient on Appointment.id_patient = Patient.idPatient
INNER JOIN Doctor on Appointment.id_doctor = Doctor.idDoctor;

/* Sample Data */

insert into Patient
(firstName, lastName, gender, birthDate, phoneNumber, email, city, zipCode, streetName, houseNumber)
    values
('James', 'Jones', 'Male', '1992-08-02', '636-48018', null, null, null,null,null),
('Robert', 'Smith', 'Male', '1981-04-12', '836-90319', null, null, null,null,null),
('Taylor', 'Jefferson', 'Male', '2000-05-02', '734-43028', null, null, null,null,null),
('Mary', 'Williams', 'Female', '1992-03-20', '124-98413', null, null, null,null,null),
('Michael', 'Hernandez', 'Male', '1995-01-02', '754-13153', null, null, null,null,null);

insert into Doctor
(name, gender, birthDate, licenseNumber)
    values
('Joseph Smith', 'Male', '1984-02-11', '55543325'),
('David Garcia', 'Male', '1979-12-01', '33126523'),
('Maria Martinez', 'Female', '1990-06-02', '18531234');

insert into Appointment
(date, description, status, price, id_patient, id_doctor)
    values
('2020-01-13 13:00:00', 'Test', 'Unconfirmed', 120.0, 1, 2),
('2020-01-13 14:00:00', 'Test2', 'Completed', 80.0, 4, 1),
('2020-01-13 16:30:00', 'Test3', 'Canceled', 100.0, 2, 1),
('2020-01-14 08:030:00', 'Test4', 'Completed', 110.0, 3, 3),
('2020-01-14 12:00:00', 'Test5', 'Confirmed', 200.0, 5, 2),
('2020-01-15 14:00:00', 'Test6', 'Confirmed', 220.0, 4, 3),
('2020-01-15 16:00:00', 'Test7', 'Unconfirmed', 300.0, 2, 3);