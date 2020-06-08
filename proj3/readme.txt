1. Transfer the whole folder 'phase3setup' to /tmp/$(logname)/phase3setup on 
   your assigned lab machine, where $(logname) is your user name.

2. Optional - Kill existing PostgreSQL instance and change pgport by running these commands:
       killall postgres
       export PGPORT=49434
	   
3. Initialize the database instance:
       bash /tmp/$(logname)/phase3setup/postgresql/startdb.sh

4. Create tables and load data:
       bash /tmp/$(logname)/phase3setup/postgresql/createdb.sh

5. Optional: Enter SQL command windows and test with some SQL commands:
       psql -h localhost $(logname)_db
       psql -h /tmp/$LOGNAME/mydb/sockets $(logname)_db < test.sql
	   
6. Stop the database instance:
       bash /tmp/$(logname)/phase3setup/postgresql/stopdb.sh