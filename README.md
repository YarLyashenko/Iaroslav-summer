# Summer tests

Please create a new branch before starting to work on tasks. You can
send us the solution in any form you like - as a pull request, link to your repository,
archive, anything. We'd appreciate if you could keep it private in case
you decide to put it into your repository.

## Run `summer` service

To run the test service you need to have `docker` and `docker-compose` 
installed locally and run from repository root:

```bash
docker-compose build
docker-compose up -d
```

From then on, `summer` service will respond to requests on `http://localhost:4000` on your local machine.

## Service API description

`summer` service consists of four endpoints

- `/auth`
- `/sum`
- `/sumLazy`
- `/sumLimited`

Endpoint `/auth` responds to `POST` requests. It is responsible for user authentication.
It expects body with `JSON` like this:

```bash
{"username": "USER", "password": "PASS"}
```

You can find your credentials in [credentials file](./credentials.yml).

If valid credentials are sent, endpoint will respond with `JSON` body containing token:

```bash
http POST http://localhost:4000/auth username=VALID_USERNAME password=VALID_PASSWORD

HTTP/1.1 200 OK
Content-Length: 36
Content-Type: application/json; charset=utf-8
Date: Thu, 14 Feb 2019 14:45:36 GMT

{
    "token": "VALID_TOKEN"
}
``` 

If invalid username or password are sent `summer` will respond with `401 Unauthorized`:

```bash
http POST http://localhost:4000/auth username=definitelly password=invalid

HTTP/1.1 401 Unauthorized
Content-Length: 24
Content-Type: application/json; charset=utf-8
Date: Thu, 14 Feb 2019 14:47:22 GMT

{
    "error": "Unauthorized"
}
```

If body is missing or cannot be understood `/auth` endpoint
will return `400 Bad Request` response, for example:

```bash
http POST http://localhost:4000/auth

HTTP/1.1 400 Bad Request
Content-Length: 32
Content-Type: application/json; charset=utf-8
Date: Thu, 14 Feb 2019 14:44:35 GMT

{
    "error": "Invalid Body Content"
}
```


Rest of the endpoints respond only to `GET` requests. All requests to these endpoint must be
authorized by sending HTTP Authorization header with valid token, that you got from `/auth`
endpoint: 

```bash
Authorization: Bearer VALID_TOKEN
```


`GET` endpoints take two query parameters: `a` and `b`. 
Service will sum parameters and return `JSON` response.

For example, calling `/sum` endpoint with `a=1` and `b=2` can be described with:

```bash
http GET http://localhost:4000/sum?a=1&b=2 'Authorization: Bearer VALID_TOKEN'

HTTP/1.1 200 OK
Content-Length: 12
Content-Type: application/json; charset=utf-8
Date: Thu, 14 Feb 2019 14:38:14 GMT

{
    "result": 3
}
```

If one of query parameters is invalid (e.g. not a number) or absent service will respond with proper
HTTP response code and `JSON` error response, for example:

```bash
http GET http://localhost:4000/sum?a=1 'Authorization: Bearer VALID_TOKEN'

HTTP/1.1 400 Bad Request
Content-Length: 25
Content-Type: application/json; charset=utf-8
Date: Thu, 14 Feb 2019 14:39:04 GMT

{
    "error": "Missing Param"
}
```

If invalid `token` is provided, `GET` endpoints will respond with `401 Unauthorized`

## Tasks

Your task is to implement test client, tests and skeleton of testing framework for API tests of `summer` service.
You can complete any number of tasks in any order. Please note that
part 3 is completely optional - if you decide to skip it for now, we'll come back to it 
later during the on-site interview. 

### Example of solution

Example tests `DSL` can be found [here](./tests.dsl). Please treat it as an example - your solution may look different 
in the end.

### General requirements

Your solution must run in `docker`. We expect that the only things
we need installed to run the solution are `docker` and `docker-compose`.

### Part 1

Implement tests of `/sum` endpoint, testing basic functionality of summing two numbers.

### Part 2

Write tests for `/sumLazy` endpoint. It will return response to your request in up to 2 seconds. If serving
request takes longer then that, the test should fail. 
 
### Part 3 - optional

Write tests for `/sumLimited` endpoint. It has a rate limiting mechanism implemented, so that no more then 1 request
per second is served. In case of more requests in one second, response will look like that:

```bash
http GET http://localhost:4000/sumLimited?a=1&b=2

HTTP/1.1 429 Too Many Requests
Content-Length: 29
Content-Type: text/plain; charset=utf-8
Date: Tue, 12 Feb 2019 16:01:13 GMT

{
    "error": "Too Many Requests"
}
```

Your task is to verify if rate limiting is properly implemented - if not, tests should fail.

 