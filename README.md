# Community Service Tracker
FBLA Project

TO RUN THE APPLICATION USING GRADLE: 
=============================================================================================================================================================================
Prerequisties
1. Have Java 1.8 or higher downloaded
2. Download MongoDB to store the data, go to here for instructions: https://docs.mongodb.com/manual/administration/install-community/
3. Download Gradle to run the program

Instructions
1. On the local machine, extract the Bay_MontaVistaHighSchool.zip files to tmp folder
2. Extract the files from the zip file and have all the contents appear in the tmp folder, ie file path to the content should say C:\tmp
3. In the command prompt type: mongod
4. Then open another command prompt while keeping the previous one running, and type: mongo
5. You will now enter mongo database, to create the database type: use communityServiceDB
6. Now database set up is done, press keys : [CTRL] + C
7. To import the JSON file into the database, type ONE of the following into the command prompt based on you operating system:
	a. WINDOWS: mongorestore -d communityServiceDB C:/tmp/data/communityServiceDB
	b. UNIX: mongorestore -d communityServiceDB tmp/data/communityServiceDB
	c. IOS: mongorestore -d communityServiceDB tmp/data/communityServiceDB
8. In the same command prompt change directory path to tmp folder
9. Then type: gradle build
10. Then type: gradle run
11. This opens the application.
12. Enter admin for username and 1234 for password and hit LOGIN to open the Main page of the application.


Alternative: TO RUN THE APPLICATION USING COMSRV.JAR: 
=============================================================================================================================================================================
Prerequisties
1. Have Java 1.8 or higher downloaded
2. Download MongoDB to store the data, go to here for instructions: https://docs.mongodb.com/manual/administration/install-community/
3. 

Instructions
1. On the local machine, extract the Bay_MontaVistaHighSchool.zip files to tmp folder
2. Extract the files from the zip file and have all the contents appear in the tmp folder, ie file path to the content should say C:\tmp
3. In the command prompt type: mongod
4. Then open another command prompt while keeping the previous one running, and type: mongo
5. You will now enter mongo database, to create the database type: use communityServiceDB
6. Now database set up is done, press keys : [CTRL] + C
7. To import the JSON file into the database, type ONE of the following into the command prompt based on you operating system:
	a. WINDOWS: mongorestore -d communityServiceDB C:/tmp/data/communityServiceDB
	b. UNIX: mongorestore -d communityServiceDB tmp/data/communityServiceDB
	c. IOS: mongorestore -d communityServiceDB tmp/data/communityServiceDB
8. In the same command prompt change directory path to tmp/bin
9. Then type: comsrv
10. This opens the application.
11. Enter admin for username and 1234 for password and hit LOGIN to open the Main page of the application.
