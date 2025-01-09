-- Question 1:  Insert some data into a table 
insert into cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
values 
  (9, 'Spa', 20, 30, 100000, 800);


-- Question 2:   Insert calculated data into a table 
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
SELECT 
  MAX(facid) + 1, 
  'Spa', 
  20, 
  30, 
  100000, 
  800 
FROM 
  cd.facilities;


-- Question 3:    Update some existing data 
UPDATE 
  cd.facilities 
SET 
  initialoutlay = 10000 
WHERE 
  facid = 1;


-- Question 4:    Update a row based on the contents of another row 
UPDATE 
  cd.facilities 
SET 
  guestcost = (
    SELECT 
      guestcost 
    FROM 
      cd.facilities 
    WHERE 
      facid = 0
  ) * 1.1, 
  membercost = (
    SELECT 
      membercost 
    FROM 
      cd.facilities 
    WHERE 
      facid = 0
  ) * 1.1 
WHERE 
  facid = 1;


-- Question 5:    Update a row based on the contents of another row 
DELETE from 
  cd.bookings;


-- Question 6:    Delete a member from the cd.members table
DELETE from 
  cd.members 
WHERE 
  memid = 37;


-- Question 7:    Control which rows are retrieved - part 2 
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  AND (
    membercost < monthlymaintenance / 50.0
  );


-- Question 8:    Basic string searches  
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name like '%Tennis%';


-- Question 9:    Matching against multiple possible values   
SELECT 
  * 
from 
  cd.facilities 
WHERE 
  facid IN (1, 5);


-- Question 10:   Working with dates    
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
from 
  cd.members 
WHERE 
  joindate > '2012-09-01';


-- Question 11:   Combining results from multiple queries    
SELECT 
  surname 
FROM 
  cd.members 
UNION 
select 
  name 
from 
  cd.facilities;



-- Question 12:   Retrieve the start times of members' bookings   
SELECT 
  starttime 
FROM 
  cd.bookings 
  INNER JOIN cd.members ON cd.bookings.memid = cd.members.memid 
WHERE 
  cd.members.firstname = 'David' 
  AND cd.members.surname = 'Farrell';



-- Question 13:    Work out the start times of bookings for tennis courts    
SELECT 
  b.starttime, 
  f.name 
FROM 
  cd.bookings b 
  INNER JOIN cd.facilities f ON b.facid = f.facid 
WHERE 
  f.name LIKE '%Tennis Court%' 
  AND b.starttime :: date = '2012-09-21' 
ORDER BY 
  b.starttime;



-- Question 14:    Produce a list of all members, along with their recommender     
SELECT 
  members.firstname, 
  members.surname, 
  r.firstname AS recfname, 
  r.surname AS recsname 
FROM 
  cd.members members 
  LEFT JOIN cd.members r ON members.recommendedby = r.memid 
ORDER BY 
  members.surname, 
  members.firstname;



-- Question 15:    Produce a list of all members who have recommended another member    
SELECT 
  DISTINCT members.firstname, 
  members.surname 
FROM 
  cd.members members 
  INNER JOIN cd.members r ON members.memid = r.recommendedby 
ORDER BY 
  members.surname, 
  members.firstname;



-- Question 16:    Produce a list of all members, along with their recommender, using no joins.     
select 
  distinct mems.firstname || ' ' || mems.surname as member, 
  (
    select 
      recs.firstname || ' ' || recs.surname as recommender 
    from 
      cd.members recs 
    where 
      recs.memid = mems.recommendedby
  ) 
from 
  cd.members mems 
order by 
  member;




