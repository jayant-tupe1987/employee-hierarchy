#Install Oracle:
docker pull store/oracle/database-enterprise:12.2.0.1
docker run -d -p 1522:1522 --name oracle store/oracle/database-enterprise:12.2.0.1
docker exec -it oracle bash -c "source /home/oracle/.bashrc; sqlplus /nolog"
(It takes time to come to healty status like 35-45 minutes after that docker exec command need to run)
#Connect to Database and execute scripts
Use below passwords
use Oradoc_db1 for sys
use personia for personia

Execute below commands and use password mentioned above as per user:
connect sys as sysdba; 
alter session set "_ORACLE_SCRIPT"=true;
create user personia identified by personia;
GRANT ALL PRIVILEGES TO personia;
connect personia;

Execute schema.sql script

#Deploy Application
docker build -t employee-hierarchy:latest .
docker run  --network=host --name employee-hierarchy -d -p 8081:8081 employee-hierarchy:latest