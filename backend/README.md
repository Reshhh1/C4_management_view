# Manager view Backend Kotlin

This application serves as the backend for the manager view of C4Enhanced. It enables users to create and manage teams, and link those teams to C4 elements for streamlined project coordination.

## Technologies Used

### Application
* Kotlin *Programming language*
* Ktor *Framework*
* Exposed *ORM*
* Dagger2 *Dependency Injection framework*
* Gradle
* MySQL *Database*

### Testing
* JUnit *Framework for unit testing*
* Mockito *Framework for mocking*
* H2 *Testing database*

## Running the Application
Before running the application, follow these steps:
1. Navigate to the `src/main/resources` directory. 
2. Add a file named application.conf
3. Add the following content into that file:
```
    ktor {
  deployment {
    port = <insert PORT>
    port = ${?PORT}
  }
  application {
    modules = [com.example.ApplicationKt.module]
  }
  database {
    url = "jdbc:mysql://localhost:3306/management-view",
    driver = "com.mysql.cj.jdbc.Driver"
    user = <Insert username>,
    password = <Insert password>
  }

  jwt {
    secret = "secret"
    issuer = "http://localhost:<insert PORT>/"
    audience = "http://localhost:<insert PORT>/hello"
    realm = "Access to hello"
  }
}
```
4. Change the required fields with <>
5. Add the required database into your local mysql. It's currently "management-view" in the
provided file, but you could change this if needed.

To run the application, simply execute the main function inside the `Application.kt` file. The application will start on 
http://localhost:<`PORT`>.

## Testing the Application

1. Open the Intelij Idea and click on the database icon
2. click on the + icon go to datasources -> H2 database and click on it.
3. Any configurations that will be made in the H2 configuration, should be applied in the testApplication.conf file
4. To test the connection click on "test connection"


If this is successful you could test the application right click the `src/test` folder and click on `More run/debug -> Modify Run configuration..`. 

Add
the following Environment variable: `environment=testing`, right click the `src/test` again and run the test.

## Implemented API
Some endpoints are required to have a cookie with a JWT token for authorization. 
<table>
    <tr>
        <th>Resource</th>
        <th>GET</th>
        <th>POST</th>
        <th>PUT</th>
        <th>DELETE</th>
    </tr>
    <tr>
        <td>/users/{id}</td>
        <td>Gets the users firstName
Response:

{"firstName": string, prefixes: string, lastName: string}
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
    </tr>
        <tr>
        <td>/users/firstname</td>
        <td>Gets the users firstName
Cookie required
Response:

{"firstName": string}
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
    </tr>
    <tr>
        <td>/users</td>
        <td>-
        </td>
                <td>Posts a new user

Requestbody:

{"firstName": string, "prefixes": string, "lastName": string, "email": string, "password": string}[]
        </td>
        <td>Updates a specific user

Requestbody:

{"firstName": string, "prefixes": string, "lastName": string, "email": string, "role: Role}
        </td>
        <td>--</td>
    <tr>
    <tr>
        <td>/users/password</td>
        <td>--</td>
        <td>--</td>
        <td>Updates the password of a specific user
Cookie required
Requestbody:
*The user is authenticated*
{"oldPassword": string, "newPassword": string}
        </td>
        <td>--</td>
    </tr>
    <tr>
        <td>/users?name={name}</td>
        <td>Gets users by their name

Responsebody:

{"firstName": string, "prefixes": string, "lastName": string, "email": string}[]
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
    <tr>
        <td>/teams</td>
        <td>
Gets all teams

Responsebody:

{"id": Int, "name": string}[]
        </td>
        <td>
Posts a new team

Requestbody:

{ "name": string, "members": int[], "createdBy": int}
        </td>
        <td>--</td>
        <td>--</td>
    </tr>
    <tr>
        <td>/teams/{id}</td>
        <td>
Gets a specific team

Responsebody:

{"name": string, "members": {"id": int, "firstName": string, "prefixes": string, "lastName": string}[], "createdAt":
Date}
        </td>
        <td>--</td>
        <td>
        --
        </td>
        <td>--</td>
    </tr>
    <tr>
        <td>/teams/{id}/members</td>
        <td>
Gets all team members of a specific team

Responsebody:

{"id": int, "firstName": string, "prefixes": string, "lastName": string}[]
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
    </tr>
        <tr>
        <td>/sessions</td>
        <td>--</td>
        <td>Login for the user

Requestbody:

{"email: String, password: String}</td>
        <td>--</td>
        <td>--</td>
    </tr>
</table>