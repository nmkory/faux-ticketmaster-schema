DROP TABLE IF EXISTS cinemaseating_has_cinematheater CASCADE;
DROP TABLE IF EXISTS cinematheater_has_cinema CASCADE;
DROP TABLE IF EXISTS cinema_has_city CASCADE;
DROP TABLE IF EXISTS city CASCADE;

SELECT table_name
FROM information_schema.tables
WHERE table_schema='public';

CREATE TABLE city (
    cityid int NOT NULL,
    name varchar(255) NOT NULL,
    zipcode int NOT NULL,
    state varchar(255) NOT NULL,
    PRIMARY KEY (cityid)
);
--
CREATE TABLE cinema_has_city (
    cinemaid int NOT NULL,
    cityid int NOT NULL,
    name varchar(255) NOT NULL,
    totalcinematheaters int NOT NULL,
    PRIMARY KEY(cinemaid),
    FOREIGN KEY(cityid) REFERENCES city(cityid)
);

CREATE TABLE cinematheater_has_cinema (
    cinematheaterid int NOT NULL,
    cinemaid int NOT NULL,
    totalseatnum int NOT NULL,
    theatername varchar(255) NOT NULL,
    PRIMARY KEY(cinematheaterid),
    FOREIGN KEY(cinemaid) REFERENCES cinema_has_city(cinemaid)
);

CREATE TABLE cinemaseating_has_cinematheater (
    cinemaseatid int NOT NULL,
    seatnumber int NOT NULL,
    type varchar(255) NOT NULL,
    cinematheaterid int NOT NULL,
    PRIMARY KEY(cinemaseatid),
    FOREIGN KEY(cinematheaterid) REFERENCES cinematheater_has_cinema(cinematheaterid)
);

-- CREATE TABLE Payment (
--     PaymentID varchar(255),
--     BookingID varchar(255),
--     TransactionID varchar(255),
--     Amount int,
--     PaymentDate timestamp,
--     PaymentMethod varchar(255),
--     PRIMARY KEY(PaymentID),
--     FOREIGN KEY(BookingID) REFERENCES Booking(BookingID)
-- );

-- CREATE TABLE Booking (
--     BookingID varchar(255),
-- 	ShowSeatID varchar(255),
-- 	Email varchar(255),
--     Status varchar(255),
--     NumSeats int,
--     BoookingDate DateTime,
-- 	PRIMARY KEY(BookingID),
-- 	FOREIGN KEY(ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
-- );
-- CREATE TABLE UserAccount (
--     Email varchar(255),
-- 	BookingID varchar(255),
--     FirstName varchar(255),
--     PhoneNum int,
--     LastName varchar(255),
--     Password varchar(255),
-- 	PRIMARY KEY(Email)
-- 	FOREIGN KEY(BookingID) REFERENCES Booking(BookingID)
-- );
-- CREATE TABLE ShowSeating (
--     ShowSeatID varchar(255),
--     Price int,
-- 	PRIMARY KEY(ShowSeatID)
-- );
-- CREATE TABLE Show (
--     ShowID varchar(255),
-- 	BookingID varchar(255),
-- 	ShowSeatID varchar(255),
--     StartTime varchar(255),
--     EndTime int,
--     ShowDate Date,
-- 	PRIMARY KEY(ShowID),
-- 	FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),
-- 	FOREIGN KEY (ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
-- );
-- CREATE TABLE Movie (
--     MovieID varchar(255),
-- 	ShowID varchar(255),
--     Description varchar(255),
--     Title int,
--     Genre varchar(255),
--     Country varchar(255)
-- 	ReleaseDate DateTime(255),
--     Duration varchar(255),
-- 	MovieLanguage varchar(255),
-- 	PRIMARY KEY (MovieID),
-- 	FOREIGN KEY (ShowID) REFERENCES Show(ShowID)
-- );


-- CREATE TABLE CinemaSeat (
--     CinemaSID varchar(255),
-- 	CINEMATID varchar(255),
-- 	ShowSeatID varchar(255),
--     CSType varchar(255),
--     SeatNum int,
-- 	PRIMARY KEY(CinemaSID),
-- 	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CINEMATID),
-- 	FOREIGN KEY(ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
-- );

-- CREATE TABLE PlayedIn (
-- 	ShowID varchar(255),
-- 	CINEMATID varchar(255),
-- 	PRIMARY KEY(ShowID,CINEMATID),
-- 	FOREIGN KEY(ShowID) REFERENCES Show(ShowID),
-- 	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CINEMATID)
-- );
