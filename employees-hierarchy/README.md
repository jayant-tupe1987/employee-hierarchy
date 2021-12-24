# Ever-changing Hierarchy

The project would help for company to build their employee hierarchy, and the ability for an employee to know who is/are their boss/bosses.

## Getting Started

Technology stack includes [Spring Boot](https://spring.io/projects/spring-boot) + Java + Maven

### Prerequisites
- [Java](https://www.oracle.com/technetworkk/java/javase/downloads/jdk8-downloads-2133151.html) >= 8
- [Mysql](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/) 8.0

## Available APIs
```
POST /users/authenticate (authenticate user and get JWT token)
POST /users/register (Register user. This api is not exposed to all.)
POST /hierarchy (Create hierarchy)
GET  /hierarchy (Get hierarchy)
GET  /hierarchy/{name}/superiors (Get superiors)
```

### API Usage

From now, we should able to access web api using postman with below details:

```
Register user POST /users/register This api is not exposed to all.
URL: `http://localhost:8081/employee-hierarchy/users/register` Replace dummy user and dummy password with your details
Body: {"username" : "dummy user","password":"dummy password"}
Expected response:{"status": "Success","code": 200,"message": "Success","data": "{dummy user} registered successfully."}

Authenticate user and get JWT token POST /users/authenticate Replace dummy user and dummy password with your details. Replace dummy token (use generated toke) with `$token-generated`
URL: `http://localhost:8081/employee-hierarchy/users/authenticate`
Body: {"username" : "dummy user","password":"dummy password"}
Expected response: {"jwtToken": "dummy token"}

POST /hierarchy (Create hierarchy)
URL: `http://localhost:8081/employee-hierarchy/hierarchy`
Body: {
	"Pete": "Nick",
	"Barbara": "Nick",
	"Nick": "Sophie",
	"Sophie": "Jonas"
}
Authorization: Bearer {`$token-generated`}
Expected response:{
    "status": "Success",
    "code": 200,
    "message": "Employee Hierarchy Created Successfully.",
    "data": "{"Jonas": {"Sophie": {"Nick": {"Pete": {}  , "Barbara": {} } } } }"
}

Get hierarchy GET  /hierarchy 
URL: `http://localhost:8081/employee-hierarchy/hierarchy`
Authorization: Bearer {`$token-generated`}
Expected response:{
    "status": "Success",
    "code": 200,
    "message": "Available Hierarchy Fetched Successfully.",
    "data": "{"Jonas": {"Sophie": {"Nick": {"Pete": {}  , "Barbara": {} } } } }"
}

GET  /hierarchy/{name}/superiors (Get superiors)
URL: `http://localhost:8081/employee-hierarchy/hierarchy/Pete/superiors`
Authorization: Bearer {`$token-generated`}
Expected response: {
    "status": "Success",
    "code": 200,
    "message": "Available Hierarchy Fetched Successfully.",
    "data": "{"Jonas": {"Sophie": {"Nick": {"Pete": {} } } } }"
}
```
##Deploy using Docker
- Copy zip folder and unzip it
- Check docker-compose.yml, Dockerfile,employees-hierarchy-0.0.2-SNAPSHOT jar inside target and db-init-script file/folder present inside employees-hierarchy folder
- Open command prompt from employees-hierarchy folder and run command `docker compose build` 
- After successful Build, run `docker compose up`
- Wait for application to start. You can check console logs for the same(Wait until you see - com.assignment.Application               : Started Application ).
- Available APIs and API Usage section to call APIs using postman

## JUnits
Test cases cover most of core logic, which is locate in `service layer` , `repository layer` and `controller layer`

## Coding style
Follow default Java code style on Eclipse IDE


## A deeper look

### Applied design pattern
- Inverse of control
- Repository pattern
- SOLID
- DTO - Data transfer object

### Build hierarchy algorithm
- Step 1: Check if input is valid and save in database.
- Step 2: Fetch data and store name, parent, child and id(employee id created) in TreeNode(DTO created to store hierarchy)
- Step 3: Build comprehensive tree from root/superior TreeNode

**For example:** 
- the company has following employees relationship
JSON input
{
	"Pete": "Nick",
	"Barbara": "Nick",
	"Nick": "Sophie",
	"Sophie": "Jonas"
}
- For each relationship `subordinate -> superior`, we prepare TreeNode, Store name, parent, child and id in TreeNode. Prepare JSON object from root to child showing employee and superior or indirect superior. 

- The expected result for above input is (Data key in JSON shows hierarchy created) 
{
    "status": "Success",
    "code": 200,
    "message": "Success",
    "data": "{"Jonas": {"Sophie": {"Nick": {"Pete": {}  , "Barbara": {} } } } }"
}
