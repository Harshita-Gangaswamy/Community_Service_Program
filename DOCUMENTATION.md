#Community Service Tracker

Introduction:
This program is designed to track a student's community service hours for various 
Community Service Award (CSA) programs. The users will be administrative staff who have the ability to 
add/edit/view student information, add/edit/view various program information, add/edit/view student hours, 
and create weekly or monthly reports for student hours or program hours.

Before Running:
Install MongoDB for data storage and make sure Mongod is running
Java 1.8 or above and JVM is running,

While running:

When you run the program, the first screen that will show up is a login page. 
Type in "admin" for username and "1234" for password. 


On the search student page, there will be a menubar with different pages for different tasks. 
Under the "Student" tab you will see a drop down listing "Student Hours", "Search and Edit Student", 
"Add Student", and "Delete Student". 

	Add Student Page:
	- This page allows the user to add a new student. 
	- The following information must be entered: a unique 7 digit long Student ID, first name, last name, 
		and grade. A middle name is not required, but can be inputed if necessary.  

	Search and Edit Student page:
	- This page allows the user to view and edit student information.
	- Enter the student's ID in order to search the rest of the student information. Once you hit search, 
		you will be taken to another page displaying all the student's information. 
	- The edit button will allow you to edit the information shown and the same requirement as was stated 
		in the add student page part of this document applies here as well.

	Student Hours page:
	- This page allows the user to add and edit hours a student has for each program
	- Enter the student's ID in order to search the rest of the student information. Once you click search 
		a dropdown with all the programs the student is affiliated with at the moment will be listed. 
	- The add program button will display all the programs available and the user can select multiple 
		programs the student is apart of by pressing the [SHIFT] key while clicking on the programs names. 
	- The selected programs will be highlighted. 
	- When you choose one program from the dropdown, a table will appear showing the date and hours the 
	student has done so far and the total hours for that specific program. 
	- If you would like to add hours then fill out the appeared text fields labeled hours and date. You can 
		use the calendar icon to choose the specific date. 
	- Select a row on the table to edit and then click on the edit button. The selected information will 
		appear in textfields.
	- To delete information on the table, select it and click the delete button.

	Delete Student page:
	- This page allows the user to delete all of the student's information.
	- Enter the student's ID in order to search the rest of the student information. Once you hit search, 
		you will be taken to another page displaying all the student's information. Click on the delete button 
		and a confirmation window will appear asking you to confirm if you want to delete that student's information.


The next tab on the menubar called "CSA Programs" will have the following dropdown items: 
"Search and Edit Programs" and "Add Program". 

	Add Program page:
	- This page allows the user to create a CSA program.
	- Enter the unique program ID of any length and program name. Description of the program is optional.
	*Note: the program ID can not be changed later

	Search and Edit Programs page:
	- This page allows the user to view and edit the program's information.
	- Input the program's unique ID. Then once you click search all of the information will be displayed. 
	- The edit button will allow you to change the program name and its description.


The next tab called "Reports" will have these following dropdown items: 
"Weekly CSA Report", "Monthly CSA Report", "Weekly Student Report", and "Monthly Student Report". 

	CSA Report pages:
	- This page allows the user to see the CSA program and its total hours report for either a certain week or month.
	- The report will display each CSA program and the total hours done by all students in each program. 
	- Display monthly report by each month and year (the years only go from 2015 - 2020 ). 
		- report can be printed to txt file and will be stored under reports/Monthly CSA Reports
	- Display weekly report by each week (week numbers with 1 - 52) and year (2015 - 2020).
		- report can be printed to txt file and will be stored under reports/Weekly CSA Reports

	Student Report pages:
	- This page allows the user to see each student's information and their total hours report for either a certain week or month.
	- The report will display each student's information and their total hours done in all their programs. 
	- Display monthly report by each month and year ( 2015 - 2020 ). 
		- report can be printed to txt file and will be stored under reports/Monthly Student Reports
	- Display weekly report by each week ( 1 - 52 ) and year ( 2015 - 2020 ).
		- report can be printed to txt file and will be stored under reports/Weekly Student Reports


The last tab called "Logout" will have the dropdown item of the same name "Logout".

	Logout page:
	- Takes the user back to the login page.