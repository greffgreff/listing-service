<p>
  <img src="https://github.com/rently-io/listing-service/actions/workflows/ci.yml/badge.svg" />
  <img src="https://github.com/rently-io/listing-service/actions/workflows/cd.yml/badge.svg" />
</p>

# Listing Service

This Spring Boot project is one among other RESTful APIs used in the larger Rently project. More specifically, this endpoint is intended to serve requests regarding adverts that users create on the Rently.io website (in theory) and fecthing specific listings. Fetching listings in bulk is perfomed using this [search service](https://github.com/rently-io/search-service). 

Listings are stored on a MongoDB database. Possible requests include `GET`, `POST`, `PUT`, `DELETE`. Upon certain requests, both data valiation and ownership verification is performed using JWTs.

After each subsequent additions and changes to the codebase of the service, tests are ran and, if passed, the service is automatically deployed on to a Heroku instance [here](https://listing-service-rently.herokuapp.com/) and dockerized [here](https://hub.docker.com/repository/docker/dockeroo80/rently-listing-service).

> ⚠️ Please note that the service is currently deployed on a free Heroku instance and needs a few seconds to warm up on first request!

Please use the following command to run the docker image:
```bash
docker run -p 8081:8081 -e CREDENTIALS=bb63b9ccbf9ab6:GhcjiQrwHJ6tGjXz dockeroo80/rently-listing-service
```

### C2 model
![C2 model](https://i.imgur.com/34Nvkd4.jpg)

## Objects

### Response Object

| **Field**              | **Description**                                                           |
| ---------------------- | ------------------------------------------------------------------------- |
| `timestamp`, timestamp | A timestamp of when the request was served. Format: _yyyy-MM-dd HH:mm:ss_ |
| `status` int           | The http response status code                                             |
| `content`, any         | The response data, _optional_                                             |
| `message`, string      | The response message, _optional_                                          |

### Listing Object

| **Field**              | **Description**                                            |
| ---------------------- | ---------------------------------------------------------- |
| `id` uuid string       | The listing's id                                           |
| `name` string          | The listing's title                                        |
| `price` float          | The listing's daily charge                                 |
| `startDate` timestamp  | The listing's rental start date                            |
| `endDate` timestamp    | The listing's rental end date                              |
| `leaser` uuid string   | The listing's leaser's id (user id)                        |
| `address` address      | The listing's address, per listing basis                   |
| `phone` string         | The listing's contact phone, per listing basis, not leaser |
| `image` string         | The listing's image url                                    |
| `desc` string          | The listing's description                                  |
| `createdAt`, timestamp | Timestamp of when the listing was created                  |
| `updatedAt`, timestamp | Timestamp of when the last changes to the listing was made |

### Address Object

| **Field**        | **Description**                               |
| ---------------- | --------------------------------------------- |
| `country` string | The address country                           |
| `city` string    | The address city                              |
| `zip` string     | The address zipcode                           |
| `street` string  | The address street number and name _optional_ |

### JWT Object

| **Field**         | **Description**              |
| ----------------- | ---------------------------- |
| `sub` uuid string | The user's id                |
| `iat` timestamp   | Issue time of the token      |
| `exp` timestamp   | Expiration time of the token |
| `jti` uuid string | The token's id               |

<br />

## Request Mappings

### `GET /api/v1/{id}`

Returns a json [response](#response-object) object containing one [listing](#listing-object) object.

#### URL parameters:

|    **Parameter** | **Description**  | **Required** |
| ---------------: | ---------------- | :----------: |
| `id` uuid string | Valid listing id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-04-06T15:16:53.679+00:00",
  "status": 200,
  "data": {
    "id": "cb3a431e-483e-4fb6-b61d-00fed3b0a1c5",
    "name": "some title thing",
    "desc": "I'm not going to use my trailer for a few days...",
    "price": "123",
    "image": null,
    "startDate": "1649282400",
    "endDate": "1649282400",
    "createdAt": "1649061809",
    "updatedAt": "1649061809",
    "address": {
      "street": "5 rue des roses",
      "city": "Remelfing",
      "zip": "57200",
      "country": "France"
    },
    "leaser": "a56c392d-f314-44a6-a8ae-a988b9784465",
    "phone": "+33749421717"
  }
}
```

#### Possible error codes:

| **Status** | **Message**                | **Description**                                                   |
| :--------: | -------------------------- | ----------------------------------------------------------------- |
|   `404`    | _"Could not find listing"_ | No listing found on the database with specified request parameter |

<br />

### `POST /api/v1/`

Inserts an unregistered listing in the database. Performs validation on fields and throws an error accordingly.

#### URL parameters:

> _none_

#### Request body parameters:

A [listing](#listing-object) object.

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 201,
  "message": "Successfully added listing to database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                                                                           | **Description**                                                                   |
| :--------: | --------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
|   `401`    | _"Request is either no longer valid or has been tampered with"_                                                       | Request bearer has either expired or the subject and the data holder do not match |
|   `406`    | _"A non-optional field has missing value. Value of field '`field`' was expected but got null"_                        | Non-optional field was missing                                                    |
|   `406`    | _"Validation failure occurred. Value of field '`field`' could not be recognized as type "`type`" (value: '`value`')"_ | Non-optional field was of the wrong type                                          |

<br />

### `PUT /api/v1/{id}`

Updates a listing using the request body data in json format. Performs validation on fields and throws an error accordingly alongside verifying ownership beforehand using the `subject` of the request's JWT and the URL path variable `id`.

#### URL parameters:

|    **Parameter** | **Description**    | **Required** |
| ---------------: | ------------------ | :----------: |
| `id` uuid string | A valid listing id |     true     |

#### Request body parameters:

A [listing](#listing-object) object.

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "message": "Successfully updated listing from database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                                                                           | **Description**                                                                   |
| :--------: | --------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
|   `404`    | _"Could not find listing"_                                                                                            | No listing found on the database with specified request parameter                 |
|   `406`    | _"A non-optional field has missing value. Value of field '`field`' was expected but got null"_                        | Non-optional field was missing                                                    |
|   `406`    | _"Validation failure occurred. Value of field '`field`' could not be recognized as type "`type`" (value: '`value`')"_ | Non-optional field was of the wrong type                                          |
|   `401`    | _"Request is either no longer valid or has been tampered with"_                                                       | Request bearer has either expired or the subject and the data holder do not match |

<br />

### `DELETE /api/v1/{id}`

Deletes a listing from the database. Performs ownership verification beforehand using the `subject` of the request's JWT and the URL path variable `id`.

#### URL parameters:

|    **Parameter** | **Description**    | **Required** |
| ---------------: | ------------------ | :----------: |
| `id` uuid string | A valid listing id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "message": "Successfully deleted listing from database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                     | **Description**                                                                   |
| :--------: | --------------------------------------------------------------- | --------------------------------------------------------------------------------- |
|   `404`    | _"Could not find listing"_                                      | No listing found on the database with specified request parameter                 |
|   `401`    | _"Request is either no longer valid or has been tampered with"_ | Request bearer has either expired or the subject and the data holder do not match |
