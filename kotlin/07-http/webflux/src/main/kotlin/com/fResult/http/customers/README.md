# HTTP - Customer Endpoints

## Endpoints

**Rest Controller style:**

- [GET `/rc/customers` - Retrieve all customers](#GET-rccustomers---retrieve-all-customers)

**Functional Style:**

- [GET `/fe/customers` - Retrieve all customers](#GET-fecustomers---retrieve-all-customers)

### Rest Controller Endpoints

#### GET `/rc/customers` - Retrieve all customers

**Body Request:** N/A\
**Body Response:** List of [`Customer`][CustomerEntity]\
**Response Status:** `200 OK` (if successful)

**cUrl Example:**

```bash
curl localhost:8080/rc/customers
```

#### GET `/rc/customers/{id}` - Retrieve a customer by ID

**Body Request:** N/A\
**Body Response:** [`Customer`][CustomerEntity]

**Response Status:**

- `200 OK` (if successful)
- `404 Not Found` (if customer not found)

**cUrl Example:**

```bash
curl localhost:8080/rc/customers/:id
```

#### POST `/rc/customers` - Create a new customer

**Body Request:** [`Customer`][CustomerEntity]\
**Body Response:** [`Customer`][CustomerEntity] (created customer)\
**Response Status:** `201 Created` (if successful)

**cUrl Example:**

```bash
curl -X POST localhost:8080/rc/customers \
     -H "Content-Type: application/json" \
     -d '{"name": "John Doe"}'
```

### Functional Endpoints

#### GET `/fe/customers` - Retrieve all customers

**Body Request:** N/A\
**Body Response:** List of [`Customer`][CustomerEntity]\
**Response Status:** `200 OK` (if successful)

**cUrl Example:**

```bash
curl localhost:8080/fe/customers
```

#### GET `/fe/customers/{id}` - Retrieve a customer by ID

**Body Request:** N/A\
**Body Response:** [`Customer`][CustomerEntity]

**Response Status:**

- `200 OK` (if successful)
- `404 Not Found` (if customer not found)

**cUrl Example:**

```bash
curl localhost:8080/fe/customers/:id
```

#### POST `/fe/customers` - Create a new customer

**Body Request:** [`Customer`][CustomerEntity]\
**Body Response:** [`Customer`][CustomerEntity] (created customer)\
**Response Status:** `201 Created` (if successful)

**cUrl Example:**

```bash
curl -X POST localhost:8080/fe/customers \
     -H "Content-Type: application/json" \
     -d '{"name": "Jane Doe"}'
```

[CustomerEntity]: ./Customer.kt
