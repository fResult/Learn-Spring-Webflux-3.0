# HTTP - Routes

## Endpoints

| HTTP Method | Root Path | Sub Path (if any) | Media-Type        |
|:-----------:|:---------:|:-----------------:|-------------------|
|     GET     | `/nested` |         -         | application/json  |
|     GET     | `/nested` |         -         | text/event-stream |
|     GET     | `/nested` |     `/:name`      | application/json  |

### JSON Media Type Response

#### GET `/nested`

```console
➜ curl -H "accept: application/json" http://localhost:8080/nested
{"message":"Hello, World!"}⏎
```

#### GET `/nested/:name`

```bash
curl -H "accept: application/json" http://localhost:8080/nested/:name
```

E.g. for `name` as `John`:

```console
➜ curl -H "accept: application/json" http://localhost:8080/nested/John
{"message":"Hello, John!"}⏎
```

### Event Stream Media Type Response

```console
➜ curl -H "accept: text/event-stream" http://localhost:8080/nested
data:#1
data:#2
data:#3
data:#4
data:#5
data:#6
data:#7
data:#8
data:#9
data:#10
data:#11
.
.
.
```

### Custom Route Predicate Endpoints

#### GET `/test`

This route accepts query parameters `name` and `uid`.

- The `uid` must be in `1`, `2`, or `3` only, otherwise it will return a 404 error.
- The `name` is used in the response message

```shell
curl localhost:8080/test?name={{name}}&uid={{id}}
```

```console
➜ curl localhost:8080/test?name=John&uid=3
{"message":"Hello, John!"}⏎

➜ curl localhost:8080/test?name=John&uid=4
{
  "timestamp":"2025-07-20T17:47:40.822+00:00",
  "path":"/test",
  "status":404,
  "error":"Not Found",
  "requestId":"13881290-8",
  "message":"..."
}⏎
```

#### GET `/greetings/:name`

It's a case-insensitive route that responds to both `/greetings/:name` and `/GREETINGS/:name`.\
The `:name` wll be converted to lowercase in the response.

```shell
curl localhost:8080/greetings/:name
```

```console
➜ curl localhost:8080/greetings/John
{"message":"Hello, john!"}⏎


➜ curl localhost:8080/GREETINGS/John
{"message":"Hello, john!"}⏎
```
