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
It updates the initialoutlay column to 10000 (specified in the SET statement) for the row WHERE 
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
This SQL query updates the guestcost and membercost columns in the cd.facilities table for the row WHERE 
facid is 1, based on the contents of the first row.

- The guestcost is set to 10% more of the value of the first tennis court (facid 0) guestcost.
- The membercost is set to 10% more to the value of the first tennis court (facid 0) membercost.

The UPDATE statement uses subqueries to get the values FROM one row and apply them to another.



## -- Question 5:    Update a row based on the contents of another row 
    DELETE FROM 
    cd.bookings;
### Documentation:
This SQL query deletes everything in the cd.bookings table using the DELETE statement.



## -- Question 6:    Delete a member FROM the cd.members table
    DELETE FROM 
    cd.members 
    WHERE 
    memid = 37;
### Documentation:
This SQL query deletes a row FROM the cd.members table WHERE the memid is equal to 37.



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
This SQL query retrieves the facid, name, membercost, and monthlymaintenance columns FROM 
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
This SQL query retrieves all records FROM the cd.facilities table WHERE the name column contains the word 
"Tennis." The LIKE operator is used with the % symbol to match any values that have "Tennis" anyWHERE 
in the name column.



## -- Question 9:    Matching against multiple possible values   
    SELECT 
    * 
    FROM 
    cd.facilities 
    WHERE 
    facid IN (1, 5);
### Documentation:
This SQL query retrieves all records FROM the cd.facilities table WHERE the facid column 
matches either 1 or 5. The IN operator is used to specify multiple possible values for facid.



## -- Question 10:   Working with dates    
    SELECT 
    memid, 
    surname, 
    firstname, 
    joindate 
    FROM 
    cd.members 
    WHERE 
    joindate > '2012-09-01';
### Documentation:
This SQL query retrieves the memid, surname, firstname and joindate columns FROM the cd.members table. 
It filters the records to include only those WHERE the joindate is later than September 1, 2012.



## -- Question 11:   Combining results FROM multiple queries    
    SELECT 
    surname 
    FROM 
    cd.members 
    UNION 
    SELECT 
    name 
    FROM 
    cd.facilities;
### Documentation:
This SQL query retrieves the surname FROM the cd.members table and the name FROM the cd.facilities table. 
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
This SQL query retrieves the starttime column FROM the cd.bookings table for bookings made by a member named "David Farrell." An INNER JOIN is used to combine data FROM the cd.members table (for the member's first name and surname) and the cd.bookings table (to retrieve the corresponding start time).



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
This SQL query retrieves the starttime FROM the cd.bookings table and the name FROM the cd.facilities table for Tennis Courts, WHERE the booking was made on September 21, 2012. An INNER JOIN is used to combine data FROM both tables. The results are sorted by starttime using the ORDER BY statement.



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
This SQL query generates a list of all members along with the names of their recommenders. The query uses a LEFT JOIN to include all members FROM the cd.members table, even if they do not have a recommender. The ON condition matches each member's recommendedby value with the memid of the recommender in the same table. The result is sorted alphabetically by the members' surname and firstname.



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
This SQL query retrieves a distinct list of members who have recommended at least one other member. An INNER JOIN is used to find matches WHERE a member's memid appears as a recommendedby value for another member in the same table. The DISTINCT keyword ensures that each member is listed only once. The results are ordered alphabetically by the members' surname and firstname.



## -- Question 16:    Produce a list of all members, along with their recommender, using no joins.     
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
### Documentation:
This SQL query produces a list of all members along with their recommenders without using joins. It uses a subquery within the SELECT statement to retrieve the recommender's name by matching the member's recommendedby value to the memid in the same table. The DISTINCT keyword ensures unique results, and the concatenation (||) combines the first name and surname for display. The list is sorted alphabetically by the member's full name using the ORDER BY clause.



## -- Question 17:    Count the number of recommendations each member makes.     
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
### Documentation:
This SQL query counts the number of recommendations each member makes. It retrieves the recommendedby column FROM the cd.members table, excluding NULL values. The GROUP BY statement groups members who have the same recommender, and the COUNT(*) function counts the number of recommendations for each member. The results are sorted by recommendedby using the ORDER BY clause.



## -- Question 18:    List the total slots booked per facility      
    SELECT 
      facid, 
      sum(slots) 
    FROM 
      cd.bookings 
    group by 
      facid 
    order by 
      facid;
### Documentation:
This SQL query lists the total number of slots booked for each facility. It retrieves the facid and sums up the slots FROM the cd.bookings table. The GROUP BY statement groups the results by facid, calculating the total slots for each facility. The results are sorted by facid using the ORDER BY clause



## -- Question 19:    List the total slots booked per facility in a given month       
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
### Documentation:
This SQL query lists the total number of slots booked for each facility within a specific month (September 2012). It retrieves the facid and sums the slots FROM the cd.bookings table, filtering the results to include only bookings with a starttime in September 2012. The GROUP BY statement groups the results by facid, and the results are sorted by the total number of slots using the ORDER BY clause.



## -- Question 20:    List the total slots booked per facility per month        
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
### Documentation:
This SQL query lists the total number of slots booked for each facility per month in the year 2012. It retrieves the facid, extracts the month FROM the starttime, and sums the slots FROM the cd.bookings table. The WHERE clause filters bookings to only include those FROM the year 2012. The results are grouped by facid and the extracted month using the GROUP BY statement. The results are then sorted by facid and month using the ORDER BY clause.



## -- Question 21:    Find the count of members who have made at least one booking        
    SELECT 
      count(distinct memid) 
    FROM 
      cd.bookings;
### Documentation:
This SQL query retrieves the count of distinct members (memid) who have made at least one booking. It SELECTs the distinct memid values FROM the cd.bookings table and counts them using the COUNT(DISTINCT) function. This ensures that each member is counted only once, even if they have made multiple bookings.



## -- Question 22:    List each member's first booking after September 1st 2012        
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
### Documentation:
This SQL query retrieves the surname, firstname, and memid FROM the cd.members table of members who have made at least one booking, along with the earliest booking date FROM the cd.bookings table. It joins the cd.bookings and cd.members tables using an INNER JOIN based on the memid column. The WHERE clause filters the bookings to only include those FROM September 1, 2012 and after. The results are grouped by memid to get the earliest booking date (MIN(starttime)), and the output is sorted by memid using the ORDER BY clause.



## -- Question 23:   Produce a list of member names, with each row containing the total member count        
    SELECT 
      COUNT(members.memid) OVER (), 
      members.firstname, 
      members.surname 
    FROM 
      cd.members members 
    ORDER BY 
      members.joindate;
### Documentation:
This SQL query retrieves a list of member names (firstname and surname) along with the total member count for each row. The COUNT(members.memid) OVER () is used to calculate the total number of members without grouping the results, so each row will contain the same total count. The results are sorted by the members' joindate using the ORDER BY clause.



## -- Question 24:    Produce a numbered list of members  
    SELECT 
      count(*) over(
        ORDER BY 
          joindate
      ) AS row_number, 
      firstname, 
      surname 
    FROM 
      cd.members;
### Documentation:
This SQL query produces a numbered list of members, WHERE each member is assigned a row number based on their join date. The COUNT(*) OVER (ORDER BY joindate) function is used to generate a sequential row number for each member, ordered by their join date. The result includes the firstname, surname, and the generated row_number for each member.



## -- Question 25:   Output the facility id that has the highest number of slots booked, again  
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
### Documentation:
This SQL query retrieves the facility ID (facid) that has the highest number of slots booked. It first calculates the total number of slots booked for each facility using the SUM(slots) function and groups the data by facid. The RANK() OVER (ORDER BY SUM(slots) DESC) function assigns a rank to each facility based on the total number of slots booked, with the highest total receiving rank 1. The outer query then filters the results to only include the facility with rank 1, which corresponds to the facility with the highest number of slots booked.