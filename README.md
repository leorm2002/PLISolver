# Linear and Integer Linear Optimization Solver

This project is a solver for linear and integer linear optimization problems using primal simplex, dual simplex, and cutting plane (Gomory cuts) methods. The implementation is in Java using the Quarkus framework.

## Features

- **Linear Optimization**: Solve linear programming problems using the primal simplex and dual simplex methods.
- **Integer Linear Optimization**: Solve integer linear programming problems using the cutting plane method (Gomory cuts).
- **Quarkus Framework**: Leveraging the Quarkus framework for building a high-performance, lightweight application.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/leorm2002/PLISolver.git
    cd PLISolver
    ```

2. **Build the project:**

    Ensure you have Maven and Java (JDK 11 or later) installed.

    ```sh
    mvn clean install
    ```

3. **Run the application:**

    ```sh
    cd service
    ./mvnw quarkus:dev -DdebugHost=0.0.0.0
    ```

## Usage

The solver can be used either via a web gui or used via rest api


## Endpoints

### 1. Solve Linear Integer Problem

- **URL:** `/solveLinearInteger`
- **Method:** `POST`
- **Tags:** `Service`
- **Request Body:**
  - **Content Type:** `application/json`
  - **Schema:** `PublicProblem`
- **Responses:**
  - **200 OK:**
    - **Content Type:** `application/json`
    - **Schema:** `Response`

### 2. Solve Linear Simplex Problem

- **URL:** `/solveLinearSimplex`
- **Method:** `POST`
- **Tags:** `Service`
- **Request Body:**
  - **Content Type:** `application/json`
  - **Schema:** `PublicProblem`
- **Responses:**
  - **200 OK:**
    - **Content Type:** `application/json`
    - **Schema:** `Response`

## Components

### Schemas

#### 1. FunzioneObbiettivo

- **Type:** `object`
- **Properties:**
  - `tipo`:
    - **$ref:** `#/components/schemas/Tipo`
  - `c`:
    - **Type:** `array`
    - **Items:**
      - **Type:** `number`
      - **Format:** `double`

#### 2. Message

- **Type:** `object`
- **Properties:**
  - `message`:
    - **Type:** `string`
  - `tableau`:
    - **Type:** `array`
    - **Items:**
      - **Type:** `array`
      - **Items:**
        - **Type:** `string`
  - `passaggiIntermedi`:
    - **Type:** `array`
    - **Items:**
      - **$ref:** `#/components/schemas/Message`

#### 3. Parameters

- **Type:** `object`
- **Properties:**
  - `maxIterazioni`:
    - **Type:** `integer`
    - **Format:** `int32`
  - `passaggiIntermedi`:
    - **Type:** `boolean`

#### 4. PublicProblem

- **Type:** `object`
- **Properties:**
  - `funzioneObbiettivo`:
    - **$ref:** `#/components/schemas/FunzioneObbiettivo`
  - `vincoli`:
    - **Type:** `array`
    - **Items:**
      - **$ref:** `#/components/schemas/Vincolo`
  - `parameters`:
    - **$ref:** `#/components/schemas/Parameters`

#### 5. Response

- **Type:** `object`
- **Properties:**
  - `tableau`:
    - **Type:** `array`
    - **Items:**
      - **Type:** `array`
      - **Items:**
        - **Type:** `string`
  - `passaggiIntermedi`:
    - **Type:** `array`
    - **Items:**
      - **$ref:** `#/components/schemas/Message`

## Usage

To use these endpoints, send a POST request to the respective URL with the appropriate request body in JSON format. The request body should conform to the specified schema. The response will also be in JSON format, conforming to the response schema.

### Example Request

```json
{
  "funzioneObbiettivo": {
    "tipo": "MAX",
    "c": [1.0, 2.0]
  },
  "vincoli": [
    {
      "vincolo": [1.0, 1.0, 2.0],
      "verso": "LE",
    }
  ],
  "parameters": {
    "maxIterazioni": 100,
    "passaggiIntermedi": true
  }
}
```