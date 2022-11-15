# API Auth Requests #

### Sign Request

#### Request

```http request
POST /auth/sign
{
    "username": "user1", 
    "password": "user1"
}
```

#### Response

```json
{
    "type": "Bearer",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI1YWQ0MGIwMS0yNDg5LTRiZjktYWFlYy1kYWJjMDE0NGRjNWYiLCJzdWIiOiJ1c2VyMSIsImlhdCI6MTY2ODQ0MDA3OCwiZXhwIjoxNjY4NDQwMzc4LCJ1c2VybmFtZSI6InVzZXIxIiwicm9sZXMiOiJBZG1pbiJ9.NGwJWLhE0Tper-T9UskMoIv-hv-jDyStcgQUMV4_RW7u6D_fcxZslsuKMksH6rWfACodUKFXdfuso6aGl1TyHw",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY2ODQ0MDA3OCwiZXhwIjoxNjcxMDMyMDc4fQ.CsRiS29MvvCwy1yhUO2A19GMwxypdSLwHcm338ALChfeeucn-5tXsiGxN9qy3p5Y0GVcZCsp-eny742Sip5Jtw"
}
```

### Get New Access Token Request

#### Request

```http request
POST /auth/access
{
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzU1OCwiZXhwIjoxNjcxMDE1NTU4fQ.lF89sDR9-46EJ2HQxOIkCe7bz_F5eJNIci8SaaOM75Q6DP9CWAy04iyJAIb-buo8V9iegEd2w2-pB-tjYbfoow"
}
```
#### Response

```json
{
    "type": "Bearer",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzY3NSwiZXhwIjoxNjY4NDIzOTc1LCJ1c2VybmFtZSI6InVzZXI1Iiwicm9sZXMiOiJJbnRlcm4ifQ.UnOJJ9pVKbOlG6kPVofnPaw1ts5GKw3R9fhgvjMPFH-UvRc8wWJKXD5fU2bJNoDYN7iUwYT_cGISUQ4Y-V_NmA",
    "refreshToken": null
}
```

### Get All New Tokens Request

#### Request

```http request
POST /auth/refresh
{
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzU1OCwiZXhwIjoxNjcxMDE1NTU4fQ.lF89sDR9-46EJ2HQxOIkCe7bz_F5eJNIci8SaaOM75Q6DP9CWAy04iyJAIb-buo8V9iegEd2w2-pB-tjYbfoow"
}
Authorization: Bearer Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzY3NSwiZXhwIjoxNjY4NDIzOTc1LCJ1c2VybmFtZSI6InVzZXI1Iiwicm9sZXMiOiJJbnRlcm4ifQ.UnOJJ9pVKbOlG6kPVofnPaw1ts5GKw3R9fhgvjMPFH-UvRc8wWJKXD5fU2bJNoDYN7iUwYT_cGISUQ4Y-V_NmA
Swagger: Authorize: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzY3NSwiZXhwIjoxNjY4NDIzOTc1LCJ1c2VybmFtZSI6InVzZXI1Iiwicm9sZXMiOiJJbnRlcm4ifQ.UnOJJ9pVKbOlG6kPVofnPaw1ts5GKw3R9fhgvjMPFH-UvRc8wWJKXD5fU2bJNoDYN7iUwYT_cGISUQ4Y-V_NmA
```

#### Response

```json
{
    "type": "Bearer",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzcwMiwiZXhwIjoxNjY4NDI0MDAyLCJ1c2VybmFtZSI6InVzZXI1Iiwicm9sZXMiOiJJbnRlcm4ifQ.bwizJ0kkucKQuvSRkIrDKHdBLxjWou5EH0MfgOAl1hDfE0yfmgiXAxmwl6RonlZZfs4OD7SlWKtcjlpoqObNzA",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTY2ODQyMzcwMiwiZXhwIjoxNjcxMDE1NzAyfQ.WQk9-GavyYdY5TqGlp1efazozlTZJ1ptafGCZiQoi9OvbGXiELijfSWOk8Xa11r25sOwQw-iwLAafXQKftWDdA"
}
```