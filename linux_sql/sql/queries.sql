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
DELETE FROM 
  cd.bookings;


-- Question 6:    Delete a member FROM the cd.members table
DELETE FROM 
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
FROM 
  cd.facilities 
WHERE 
  facid IN (1, 5);


-- Question 10:   Working with dates    
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate > '2012-09-01';


-- Question 11:   Combining results FROM multiple queries    
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
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
SELECT 
  distinct mems.firstname || ' ' || mems.surname as member, 
  (
    SELECT 
      recs.firstname || ' ' || recs.surname as recommender 
    FROM 
      cd.members recs 
    WHERE 
      recs.memid = mems.recommendedby
  ) 
FROM 
  cd.members mems 
order by 
  member;



-- Question 17:    Count the number of recommendations each member makes.     
SELECT 
recommendedby, 
count(*) 
FROM 
  cd.members 
WHERE 
  recommendedby is not null 
group by 
  recommendedby 
order by 
  recommendedby;



-- Question 18:    List the total slots booked per facility      
SELECT 
  facid, 
  sum(slots) 
FROM 
  cd.bookings 
group by 
  facid 
order by 
  facid;



-- Question 19:    List the total slots booked per facility in a given month       
SELECT 
  facid, 
  sum(slots) 
FROM 
  cd.bookings 
WHERE 
  starttime :: date >= '2012-09-1' 
  and starttime :: date < '2012-10-1' 
group by 
  facid 
order by 
  sum(slots); 



-- Question 20:    List the total slots booked per facility per month        
SELECT 
  facid, 
  extract(
    month 
    FROM 
      starttime
  ), 
  sum(slots) 
FROM 
  cd.bookings 
WHERE 
  extract(
    year 
    FROM 
      starttime
  ) = 2012 
group by 
  facid, 
  extract(
    month 
    FROM 
      starttime
  ) 
order by 
  facid, 
  extract(
    month 
    FROM 
      starttime
  );



-- Question 21:    Find the count of members who have made at least one booking        
SELECT 
  count(distinct memid) 
FROM 
  cd.bookings;



-- Question 22:    List each member's first booking after September 1st 2012        
SELECT 
  members.surname, 
  members.firstname, 
  members.memid, 
  MIN(bookings.starttime) 
FROM 
  cd.bookings bookings 
  INNER JOIN cd.members members ON members.memid = bookings.memid 
WHERE 
  bookings.starttime :: date >= '2012-09-01' 
GROUP BY 
  members.memid 
ORDER BY 
  members.memid;



-- Question 23:   Produce a list of member names, with each row containing the total member count        
SELECT 
  COUNT(members.memid) OVER (), 
  members.firstname, 
  members.surname 
FROM 
  cd.members members 
ORDER BY 
  members.joindate;



-- Question 24:    Produce a numbered list of members  
SELECT 
  count(*) over(
    ORDER BY 
      joindate
  ) AS row_number, 
  firstname, 
  surname 
FROM 
  cd.members;



-- Question 25:   Output the facility id that has the highest number of slots booked, again  
SELECT 
  facid, 
  total 
FROM 
  (
    SELECT 
      facid, 
      sum(slots) total, 
      rank() over (
        order by 
          sum(slots) desc
      ) rank 
    FROM 
      cd.bookings 
    group by 
      facid
  ) as ranked 
WHERE 
  rank = 1



-- Question 26:   Format the names of members 
SELECT 
  surname || ', ' || firstname as name 
FROM 
  cd.members;



-- Question 27:   Find telephone numbers with parentheses  
/* My first reflexe

SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(%' AND telephone LIKE '%)%';

*/

SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';



-- Question 28:  Count the number of members whose surname starts with each letter of the alphabet  
SELECT 
  substr(surname, 1, 1), 
  count(*) 
from 
  cd.members 
group by 
  substr(surname, 1, 1) 
order by 
  substr(surname, 1, 1);
