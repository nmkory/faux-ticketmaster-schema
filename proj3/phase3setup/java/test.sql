-- INSERT INTO Users(email, lname, fname, phone, pwd) VALUES ('nmkorry@gmail.com', 'Kory', 'Nicholas', '0053413061', '25ddfc08640f7315476018d35d5cac5207e775c4c0d0a18491ef5686fd5ac091');

--SELECT * FROM Bookings LIMIT 3;
-- SELECT * FROM ShowSeats LIMIT 3;
-- SELECT * FROM Theaters LIMIT 3;
-- SELECT * FROM CinemaSeats LIMIT 3;
-- SELECT * FROM Show Limit 3;

-- get show id info
-- SELECT 'Show ID for bid 1 in Booking table'::text;
-- SELECT b.sid
-- FROM Bookings b
-- WHERE b.bid = 1;
--
-- --get tid from sid
-- SELECT 'Theater ID from Plays for SID 83'::text;
-- SELECT tid FROM Plays WHERE sid = (SELECT b.sid
--                                    FROM Bookings b
--                                    WHERE b.bid = 1);
--
--
-- SELECT * FROM Bookings LIMIT 3;
-- SELECT * FROM Shows WHERE sid = 1;
-- SELECT * FROM Plays W 3;
-- SELECT m.title, s.sdate, s.sttime, t.tname, t.tid
-- FROM  Shows s, Movies M, Theaters t, Plays p
-- WHERE s.sid = 2
-- AND s.mvid = m.mvid
-- AND s.sid = p.sid
-- AND p.tid = t.tid;
--
-- SELECT cs.sno, cs.csid
-- FROM  Shows s, Movies M, Theaters t, Plays p, CinemaSeats cs
-- WHERE s.sid = 2
-- AND s.mvid = m.mvid
-- AND s.sid = p.sid
-- AND p.tid = t.tid
-- AND p.tid = cs.tid;
--
-- SELECT * FROM Payments LIMIT 3;
-- SELECT MAX(bid)
-- FROM Bookings;


-- DROP SEQUENCE IF EXISTS bid_sequence;
-- DROP SEQUENCE IF EXISTS sid_sequence;
-- DROP SEQUENCE IF EXISTS pid_sequence;
-- DROP SEQUENCE IF EXISTS ssid_sequence;
-- DROP SEQUENCE IF EXISTS mvid_sequence;
-- CREATE SEQUENCE bid_sequence;
-- CREATE SEQUENCE sid_sequence;
-- CREATE SEQUENCE pid_sequence;
-- CREATE SEQUENCE ssid_sequence;
-- CREATE SEQUENCE mvid_sequence;
-- SELECT setval('bid_sequence', (SELECT MAX(bid) FROM Bookings));
-- SELECT setval('sid_sequence', (SELECT MAX(sid) FROM Shows));
-- SELECT setval('pid_sequence', (SELECT MAX(pid) FROM Payments));
-- SELECT setval('ssid_sequence', (SELECT MAX(ssid) FROM ShowSeats));
-- SELECT setval('mvid_sequence', (SELECT MAX(mvid) FROM Movies));
-- INSERT INTO Bookings(bid, status, bdatetime, seats, sid, email)
-- VALUES ((SELECT nextval('bid_sequence')), 'Pending', '2020-10-10', 1, 2, 'nmkory@gmail.com');
-- DELETE FROM ShowSeats WHERE bid IN (501, 502, 503);
-- DELETE FROM Bookings WHERE email = 'nmkory@gmail.com';

SELECT *
FROM Shows
WHERE sdate = '01-01-2019'
AND tid =
-- SELECT bid FROM Payments WHERE pid = 1;
-- SELECT * FROM Payments WHERE pid = 5;
-- SELECT * FROM Bookings WHERE status = 'Cancelled';
--UPDATE ShowSeats SET bid = 1 WHERE csid = 2834 AND ssid = 483 AND sid = 83;
-- SELECT * FROM CinemaSeats WHERE csid IN (SELECT csid FROM ShowSeats WHERE sid = 83);

-- SELECT * FROM Payments LIMIT 3;
-- SELECT * FROM CinemaSeats LIMIT 3;
