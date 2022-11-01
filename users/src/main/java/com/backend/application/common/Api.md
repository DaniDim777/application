# API User Requests #

### Create User

#### Request

```http request
POST /users
{
    "username": "Alex2577", 
    "password": "anypassword", 
    "idRole": "Intern"
}
```

#### Response

```json
{
    "id": 1,
    "uuid": "113bda45-fdd2-4e8d-9ecf-0a56453aa512",
    "username": "Alex2577",
    "idRole": "Intern",
    "created": "2022-11-01T14:35:06.8756589",
    "modified": "2022-11-01T14:35:06.8756589"
}
```

### Update User

#### Request

```http request
PUT /users/113bda45-fdd2-4e8d-9ecf-0a56453aa512
{
    "username": "Alex777", 
    "password": "anotherPassword", 
    "idRole": "Mentor"
}
```
#### Response

```json
{
    "id": 1,
    "uuid": "113bda45-fdd2-4e8d-9ecf-0a56453aa512",
    "username": "Alex777",
    "idRole": "Mentor",
    "created": "2022-11-01T14:35:06.875659",
    "modified": "2022-11-01T14:44:53.5653173"
}
```

### Get User

#### Request

```http request
GET /users/113bda45-fdd2-4e8d-9ecf-0a56453aa512
```

#### Response

```json
{
    "id": 1,
    "uuid": "113bda45-fdd2-4e8d-9ecf-0a56453aa512",
    "username": "Alex777",
    "idRole": "Mentor",
    "created": "2022-11-01T14:35:06.875659",
    "modified": "2022-11-01T14:44:53.5653173"
}
```

### Get All Users

#### Request

```http request
GET /users
```

#### Response

```json
[
{
    "id": 1,
    "uuid": "113bda45-fdd2-4e8d-9ecf-0a56453aa512",
    "username": "Alex2577",
    "idRole": "Intern",
    "created": "2022-11-01T14:35:06.875659",
    "modified": "2022-11-01T14:44:53.5653173"
},
{
    "id": 2,
    "uuid": "113bda45-fdd2-4e8d-9ecf-0a56453aa512",
    "username": "Alex777",
    "idRole": "Mentor",
    "created": "2022-11-01T14:35:06.875659",
    "modified": "2022-11-01T14:44:53.5653173"
}
]
```

### Delete User

#### Request

```http request
DELETE /users/113bda45-fdd2-4e8d-9ecf-0a56453aa512
```

#### Response

```text
Status: 204 No Content
```

