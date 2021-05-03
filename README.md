# CSU22012: Data Structures and Algorithms Group Project

## Summary/highlights:
* Implementation of a bus management system based on Vancouver bus system data
* Topics covered: graphs, searching and sorting, tries
* Group project, 4 people per group (pick your own groups)
* Register groups by April 16th and send me a link to project repository
* Submission to blackboard only – no webcat/junit tests needed
* 40% of your overall CSU22012 mark
* Deliverables: code, pre-recorded 5-minute demo, design document
  
## Assignment Code Specification – 4 parts:

![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/mainscreen.png)

You are given the following input files (obtained by using TransLink open API
https://developer.translink.ca/ enabling access to data about Vancouver public transport system)

* stops.txt – list of all bus stops in the system, cca 8,000 entries
* transfers.txt – list of possible transfers and transfer times between stops, cca 5,000 entries
* stop_times.txt – daily schedule containing the trip times of all routes on all stops, cca 1,7
million entries


## Part - 1 Shortest Path Algorithm ( Djikstra's Algorithm )
Shortest Paths between 2 bus stops (as input by the user), returning the list of stops en route as well as the associated “cost”.

Stops are listed in stops.txt and connections (edges) between them come from stop_times.txt and
transfers.txt files. All lines in transfers.txt are edges (directed), while in stop_times.txt an edge
should be added only between 2 consecutive stops with the same trip_id.

Cost associated with edges should be as follows: 1 if it comes from stop_times.txt, 2 if it comes from
transfers.txt with transfer type 0 (which is immediate transfer possible), and for transfer type 2 the
cost is the minimum transfer time divided by 100.

### Demo
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part1demo.gif)



## Part - 2 Ternary Search Tree ( TST ) 
Searching for a bus stop by full name or by the first few characters in the name, using a Ternary Search Tree (TST), 
returning the full stop information for each stop matching the search criteria (which can be zero, one or more stops)

In order for this to provide meaningful search functionality please move keywords flagstop, wb, nb,
sb, eb from start of the names to the end of the names of the stops when reading the file into a TST
(eg “WB HASTINGS ST FS HOLDOM AVE” becomes “HASTINGS ST FS HOLDOM AVE WB”)

### Demo
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part2%20demo.gif)



## Part - 3 Sorting Algorithm and Maps
Searching for all trips with a given arrival time, returning full details of all trips matching the
criteria (zero, one or more), sorted by trip id


Arrival time should be provided by the user as hh:mm:ss. When reading in stop_times.txt file you
will need to remove all invalid times, e.g., there are times in the file that start 27/28 hours, so are
clearly invalid. Maximum time allowed is 23:59:59.

### Demo
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part3%20demo.gif)



## Part - 4 UI and Error Handling
Provide front interface enabling selection between the above features or an option to exit
the programme, and enabling required user input. It does not matter if this is command-line
or graphical, as long as functionality/error checking is provided.

You are required to provide error checking and show appropriate messages in the case of erroneous
inputs – eg bus stop doesn’t exist, wrong format for time for bus stop (eg letters instead of
numbers), no route possible etc. 
### Part 1 Error Handling
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part1error.png)

### Part 2 Error Handling
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part2error.png)

### Part 3 Error Handling
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part3error.png)

### Part 4 Option to exit
![](https://github.com/JohnWesleyK/CSU22012-DSA-Group-Project/blob/main/Demo%20Pictures%20and%20Videos/part4close.png)




## Team
|      Name      | Course | Student ID |      Email      | Github Username |
|----------------|--------|------------|-----------------|-----------------|
|   Li Honglin   |   CSB  |  19315272  |   lih4@tcd.ie   |     [DesLeeHL](https://github.com/DesLeeHL)    |
|  John Kommala  |   ICS  |  19303445  | kommalaj@tcd.ie |   [JohnWesleyK](https://github.com/JohnWesleyK)   |
| Kumagai Hiroki |   CSB  |  19312623  | kumagaih@tcd.ie |    [kumachan96](https://github.com/kumachan96)   |
|  Masanari Doi  |   CSB  |  19313167  |   doim@tcd.ie   |   [dodonga2211](https://github.com/dodonga2211)   |
 

 
