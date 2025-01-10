# Table Setup (DDL)

## -- Question 1:  Insert some data into a table 
    insert into cd.facilities (
    facid, name, membercost, guestcost, 
    initialoutlay, monthlymaintenance
    ) 
    values 
    (9, 'Spa', 20, 30, 100000, 800);
## Documentation:
This simple SQL query inserts a new record into the cd.facilities table using the INSERT INTO statement. 
The column names are specified explicitly to ensure the values are assigned correctly. 
The corresponding values are provided in the same order as the column names, respecting 
the data structure of the table.


## -- Question 2:   Insert calculated data into a table 
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
### Documentation:
This SQL query inserts data into the cd.facilities table using the INSERT INTO statement. 
Compared to the previous question, we did not assign a fixed value to the facid column. 
This time, we use a SELECT statement to get the last facid in our table and add 1 to it, which ensures 
automatic generation of the facid. This approach can be seen as similar to using an AUTO_INCREMENT 
for the facid column.


## -- Question 3:    Update some existing data 
    UPDATE 
    cd.facilities 
    SET 
    initialoutlay = 10000 
    WHERE 
    facid = 1;
### Documentation:
This SQL query modifies existing data in the cd.facilities table using the UPDATE statement. 
It updates the initialoutlay column to 10000 (specified in the SET statement) for the row where 
the facid is equal to 1 (specified in the WHERE statement).

## -- Question 4:    Update a row based on the contents of another row 
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
### Documentation:
This SQL query updates the guestcost and membercost columns in the cd.facilities table for the row where 
facid is 1, based on the contents of the first row.

- The guestcost is set to 10% more of the value of the first tennis court (facid 0) guestcost.
- The membercost is set to 10% more to the value of the first tennis court (facid 0) membercost.

The UPDATE statement uses subqueries to get the values from one row and apply them to another.

## -- Question 5:    Update a row based on the contents of another row 
    DELETE from 
    cd.bookings;
### Documentation:
This SQL query deletes everything in the cd.bookings table using the DELETE statement.

## -- Question 6:    Delete a member from the cd.members table
    DELETE from 
    cd.members 
    WHERE 
    memid = 37;
### Documentation:
This SQL query deletes a row from the cd.members table where the memid is equal to 37.

## -- Question 7:    Control which rows are retrieved - part 2 
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
### Documentation:
This SQL query retrieves the facid, name, membercost, and monthlymaintenance columns from 
the cd.facilities table. The query filters the rows based on two conditions:

- The membercost must be greater than 0.
- The membercost must be less than 1/50th of the monthlymaintenance.

Only rows that meet both conditions will be returned.


## -- Question 8:    Basic string searches  
    SELECT 
    * 
    FROM 
    cd.facilities 
    WHERE 
    name like '%Tennis%';
### Documentation:
This SQL query retrieves all records from the cd.facilities table where the name column contains the word 
"Tennis." The LIKE operator is used with the % symbol to match any values that have "Tennis" anywhere 
in the name column.

## -- Question 9:    Matching against multiple possible values   
    SELECT 
    * 
    from 
    cd.facilities 
    WHERE 
    facid IN (1, 5);
### Documentation:
This SQL query retrieves all records from the cd.facilities table where the facid column 
matches either 1 or 5. The IN operator is used to specify multiple possible values for facid.

## -- Question 10:   Working with dates    
    SELECT 
    memid, 
    surname, 
    firstname, 
    joindate 
    from 
    cd.members 
    WHERE 
    joindate > '2012-09-01';
### Documentation:
This SQL query retrieves the memid, surname, firstname and joindate columns from the cd.members table. 
It filters the records to include only those where the joindate is later than September 1, 2012.

## -- Question 11:   Combining results from multiple queries    
    SELECT 
    surname 
    FROM 
    cd.members 
    UNION 
    select 
    name 
    from 
    cd.facilities;
### Documentation:
This SQL query retrieves the surname from the cd.members table and the name from the cd.facilities table. 
The UNION operator combines the results of the two SELECT statements into a single result set, 
removing any duplicate values.


## -- Question 12:   Retrieve the start times of members' bookings   
    SELECT 
    starttime 
    FROM 
    cd.bookings 
    INNER JOIN cd.members ON cd.bookings.memid = cd.members.memid 
    WHERE 
    cd.members.firstname = 'David' 
    AND cd.members.surname = 'Farrell';
### Documentation:
This SQL query retrieves the starttime column from the cd.bookings table for bookings made by a member named "David Farrell." An INNER JOIN is used to combine data from the cd.members table (for the member's first name and surname) and the cd.bookings table (to retrieve the corresponding start time).


## -- Question 13:    Work out the start times of bookings for tennis courts    
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
### Documentation:
This SQL query retrieves the starttime from the cd.bookings table and the name from the cd.facilities table for Tennis Courts, where the booking was made on September 21, 2012. An INNER JOIN is used to combine data from both tables. The results are sorted by starttime using the ORDER BY statement.



## -- Question 14:    Produce a list of all members, along with their recommender     
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
### Documentation:
This SQL query generates a list of all members along with the names of their recommenders. The query uses a LEFT JOIN to include all members from the cd.members table, even if they do not have a recommender. The ON condition matches each member's recommendedby value with the memid of the recommender in the same table. The result is sorted alphabetically by the members' surname and firstname.


## -- Question 15:    Produce a list of all members who have recommended another member    
    SELECT 
    DISTINCT members.firstname, 
    members.surname 
    FROM 
    cd.members members 
    INNER JOIN cd.members r ON members.memid = r.recommendedby 
    ORDER BY 
    members.surname, 
    members.firstname;
### Documentation:
This SQL query retrieves a distinct list of members who have recommended at least one other member. An INNER JOIN is used to find matches where a member's memid appears as a recommendedby value for another member in the same table. The DISTINCT keyword ensures that each member is listed only once. The results are ordered alphabetically by the members' surname and firstname.


## -- Question 16:    Produce a list of all members, along with their recommender, using no joins.     
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
### Documentation:
This SQL query produces a list of all members along with their recommenders without using joins. It uses a subquery within the SELECT statement to retrieve the recommender's name by matching the member's recommendedby value to the memid in the same table. The DISTINCT keyword ensures unique results, and the concatenation (||) combines the first name and surname for display. The list is sorted alphabetically by the member's full name using the ORDER BY clause.


