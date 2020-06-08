SELECT m.title, s.sdate, s.sttime, t.tname, c.sno
FROM Users u, Bookings b, Shows s, Movies m, Plays p, Theaters t, CinemaSeats c
WHERE u.email = 'sharitamedellin@gmail.com'
AND u.email = b.email
AND b.sid = s.sid
AND s.mvid = m.mvid
AND b.sid = p.sid
AND p.tid = t.tid
AND p.tid = c.tid;

--SELECT * FROM CinemaSeats;
