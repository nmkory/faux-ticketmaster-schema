DROP TABLE IF EXISTS played_in CASCADE;
DROP TABLE IF EXISTS cinemaseating_has_cinematheater CASCADE;
DROP TABLE IF EXISTS cinematheater_has_cinema CASCADE;
DROP TABLE IF EXISTS cinema_has_city CASCADE;
DROP TABLE IF EXISTS city CASCADE;
DROP TABLE IF EXISTS showseating_has_show CASCADE;
DROP TABLE IF EXISTS payment_has_booking CASCADE;
DROP TABLE IF EXISTS booking_has_show_by_user CASCADE;
DROP TABLE IF EXISTS show_played_movie CASCADE;
DROP TABLE IF EXISTS movie CASCADE;
DROP TABLE IF EXISTS useraccount CASCADE;

SELECT table_name
FROM information_schema.tables
WHERE table_schema='public';

CREATE TABLE city (
    cityid int NOT NULL,
    name varchar(255) NOT NULL,
    zipcode varchar(5) NOT NULL,
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

CREATE TABLE movie (
    movieid int NOT NULL,
    description varchar(255) NOT NULL,
    duration interval NOT NULL,
    language varchar(255) NOT NULL,
    title varchar(255) NOT NULL,
    genre varchar(255) NOT NULL,
    country varchar(255) NOT NULL,
    releasedate date NOT NULL,
    PRIMARY KEY (movieid)
);

CREATE TABLE show_played_movie (
    showid int NOT NULL,
    showdate date NOT NULL,
    starttime time NOT NULL,
    endtime time NOT NULL,
    movieid int NOT NULL,
    PRIMARY KEY (showid),
    FOREIGN KEY(movieid) REFERENCES movie(movieid)
);

CREATE TABLE showseating_has_show (
    showseatid int NOT NULL,
    price money NOT NULL,
    showid int NOT NULL,
    PRIMARY KEY (showseatid),
    FOREIGN KEY(showid) REFERENCES show_played_movie(showid)
);

CREATE TABLE useraccount (
    email varchar(255) NOT NULL,
    firstname varchar(255) NOT NULL,
    lastname varchar(255) NOT NULL,
    phone varchar(10) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE booking_has_show_by_user (
    bookingid int NOT NULL,
    numseats int NOT NULL,
    status varchar(255) NOT NULL,
    showdate date NOT NULL,
    starttime time NOT NULL,
    showid int NOT NULL,
    email varchar(255) NOT NULL,
    PRIMARY KEY (bookingid),
    FOREIGN KEY(showid) REFERENCES show_played_movie(showid),
    FOREIGN KEY(email) REFERENCES useraccount(email)
);

CREATE TABLE payment_has_booking (
    paymentid int NOT NULL,
    transactionid int, --can be NULL
    amount money NOT NULL,
    paymentmethod varchar(255) NOT NULL,
    paymentdatetime timestamp NOT NULL,
    bookingid int, --can be NULL
    PRIMARY KEY (paymentid),
    FOREIGN KEY(bookingid) REFERENCES booking_has_show_by_user(bookingid)
);

CREATE TABLE played_in (
    showid int NOT NULL,
    cinematheaterid int NOT NULL,
    FOREIGN KEY(showid) REFERENCES show_played_movie(showid),
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

-- CREATE TABLE PlayedIn (
-- 	ShowID varchar(255),
-- 	CINEMATID varchar(255),
-- 	PRIMARY KEY(ShowID,CINEMATID),
-- 	FOREIGN KEY(ShowID) REFERENCES Show(ShowID),
-- 	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CINEMATID)
-- );
