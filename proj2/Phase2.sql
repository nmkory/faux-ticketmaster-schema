CREATE TABLE Payment (
    PaymentID varchar(255),
	BookingID varchar(255),
    TransactionID varchar(255),
    Amount int,
    PaymentDate DateTime,
    PaymentMethod varchar(255),
	PRIMARY KEY(Payment),
	FOREIGN KEY(BookingID), REFERENCES Booking(BookingID)
);
CREATE TABLE Booking (
    BookingID varchar(255),
	ShowSeatID varchar(255),
	Email varchar(255),
    Status varchar(255),
    NumSeats int,
    BoookingDate DateTime,
	PRIMARY KEY(BookingID),
	FOREIGN KEY(ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
);
CREATE TABLE UserAccount (
    Email varchar(255),
	BookingID varchar(255),
    FirstName varchar(255),
    PhoneNum int,
    LastName varchar(255),
    Password varchar(255),
	PRIMARY KEY(Email)
	FOREIGN KEY(BookingID) REFERENCES Booking(BookingID)
);
CREATE TABLE ShowSeating (
    ShowSeatID varchar(255),
    Price int,
	PRIMARY KEY(ShowSeatID)
);
CREATE TABLE Show (
    ShowID varchar(255),
	BookingID varchar(255),
	ShowSeatID varchar(255),
    StartTime varchar(255),
    EndTime int,
    ShowDate Date,
	PRIMARY KEY(ShowID),
	FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),
	FOREIGN KEY (ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
);
CREATE TABLE Movie (
    MovieID varchar(255),
	ShowID varchar(255),
    Description varchar(255),
    Title int,
    Genre varchar(255),
    Country varchar(255)
	ReleaseDate DateTime(255),
    Duration varchar(255),
	MovieLanguage varchar(255),
	PRIMARY KEY (MovieID),
	FOREIGN KEY (ShowID) REFERENCES Show(ShowID)
);
CREATE TABLE Cinema (
    CinemaID varchar(255),
	CINEMATID varchar(255),
    Name varchar(255),
    TotalCinema int,
	PRIMARY KEY(CinemaID),
	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CinemaTID)
);
CREATE TABLE CinemaT (
    CinemaTID varchar(255),
    TotalSeatNum varchar(255),
    CTName varchar(255),
	PRIMARY KEY(CINEMATID)
);
CREATE TABLE CinemaSeat (
    CinemaSID varchar(255),
	CINEMATID varchar(255),
	ShowSeatID varchar(255),
    CSType varchar(255),
    SeatNum int,
	PRIMARY KEY(CinemaSID),
	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CINEMATID),
	FOREIGN KEY(ShowSeatID) REFERENCES ShowSeating(ShowSeatID)
);
CREATE TABLE City (
    CityID varchar(255),
	CinemaID varchar(255),
    Name varchar(255),
    ZipCode int,
    CState varchar(255),
	PRIMARY KEY(CityID),
	FOREIGN KEY(CinemaID) REFERENCES Cinema(CinemaID)
);
CREATE TABLE PlayedIn (
	ShowID varchar(255),
	CINEMATID varchar(255),
	PRIMARY KEY(ShowID,CINEMATID),
	FOREIGN KEY(ShowID) REFERENCES Show(ShowID),
	FOREIGN KEY(CINEMATID) REFERENCES CinemaT(CINEMATID)
);



