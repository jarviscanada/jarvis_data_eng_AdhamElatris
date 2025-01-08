-- Question 1:  Insert some data into a table 

insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values (9, 'Spa', 20, 30, 100000, 800);


-- Question 2:   Insert calculated data into a table 

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT MAX(facid) + 1, 'Spa', 20, 30, 100000, 800
FROM cd.facilities;


-- Question 3:     Update some existing data 

UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;


-- Question 4:     Update a row based on the contents of another row 
UPDATE cd.facilities
SET 
guestcost = (SELECT guestcost FROM cd.facilities WHERE facid = 0) * 1.1,
membercost = (SELECT membercost FROM cd.facilities WHERE facid = 0) * 1.1
WHERE facid = 1;


-- Question 5:     Update a row based on the contents of another row 
DELETE from cd.bookings;


-- Question 6:     Delete a member from the cd.members table
DELETE from cd.members
WHERE memid = 37;


-- Question 7:     Control which rows are retrieved - part 2 
SELECT facid, name, membercost, monthlymaintenance 
FROM cd.facilities 
WHERE membercost > 0 AND (membercost < monthlymaintenance/50.0);  


-- Question 8:     Basic string searches  
SELECT * FROM cd.facilities
WHERE name like '%Tennis%';     


-- Question 9:     Matching against multiple possible values   
SELECT * from cd.facilities
WHERE facid IN (1, 5);


-- Question 10:    Working with dates    
SELECT memid, surname, firstname, joindate from cd.members
WHERE joindate > '2012-09-01';


-- Question 11:    Combining results from multiple queries    
SELECT surname FROM cd.members
UNION
select name from cd.facilities;
