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
    or after building
    ```sh
    cd service/target
    java -jar Service-1.0.0-SNAPSHOT-runner.jar
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

<h1 id="service-api-service">Service</h1>

## POST solveLinearInteger

`POST /solveLinearInteger`

> Body parameter

```json
{
  "funzioneObbiettivo": {
    "tipo": "MIN",
    "c": [
      0.1
    ]
  },
  "vincoli": [
    {
      "vincolo": [
        0.1
      ],
      "verso": "LE"
    }
  ],
  "parameters": {
    "maxIterazioni": 0,
    "passaggiIntermedi": true
  }
}
```


> Example responses


```json
{
  "tableau": [
    [
      "string"
    ]
  ],
  "soluzione": [
    "string"
  ],
  "time": 0,
  "passiRisoluzione": [
    {
      "message": "string",
      "tableau": [
        [
          "string"
        ]
      ],
      "passaggiIntermedi": [
        {}
      ]
    }
  ],
  "error": "string"
}
```

## POST solveLinearSimplex


`POST /solveLinearSimplex`

> Body parameter

```json
{
  "funzioneObbiettivo": {
    "tipo": "MIN",
    "c": [
      0.1
    ]
  },
  "vincoli": [
    {
      "vincolo": [
        0.1
      ],
      "verso": "LE"
    }
  ],
  "parameters": {
    "maxIterazioni": 0,
    "passaggiIntermedi": true
  }
}
```
> example response
```json
{
  "tableau": [
    [
      "string"
    ]
  ],
  "soluzione": [
    "string"
  ],
  "time": 0,
  "passiRisoluzione": [
    {
      "message": "string",
      "tableau": [
        [
          "string"
        ]
      ],
      "passaggiIntermedi": [
        {}
      ]
    }
  ],
  "error": "string"
}
```


# Schemas

<h2 id="tocS_FunzioneObbiettivo">FunzioneObbiettivo</h2>
<!-- backwards compatibility -->
<a id="schemafunzioneobbiettivo"></a>
<a id="schema_FunzioneObbiettivo"></a>
<a id="tocSfunzioneobbiettivo"></a>
<a id="tocsfunzioneobbiettivo"></a>

```json
{
  "tipo": "MIN",
  "c": [
    0.1
  ]
}

```

### Properties

| Name | Type                | Required | Restrictions | Description |
| ---- | ------------------- | -------- | ------------ | ----------- |
| tipo | [Tipo](#schematipo) | true     | MIN/MAX         | Verso della funzione obbiettivo        |
| c    | [double]            | true    | none         | Funzione obbiettivo espressa come vettore dei pesi delle variabili        |

<h2 id="tocS_Message">Message</h2>
<!-- backwards compatibility -->
<a id="schemamessage"></a>
<a id="schema_Message"></a>
<a id="tocSmessage"></a>
<a id="tocsmessage"></a>

```json
{
  "message": "string",
  "tableau": [
    [
      "string"
    ]
  ],
  "passaggiIntermedi": [
    {
      "message": "string",
      "tableau": [
        [
          "string"
        ]
      ],
      "passaggiIntermedi": []
    }
  ]
}

```

### Properties

| Name              | Type                        | Required | Restrictions | Description |
| ----------------- | --------------------------- | -------- | ------------ | ----------- |
| message           | string                      | false    | none         | Messaggio in forma testuale che riporta una informazione        |
| tableau           | [array]                     | false    | none         | RIporta un tableau, matrice di interi, come prima colonna il valore di Z        |
| passaggiIntermedi | [[Message](#schemamessage)] | false    | none         | Contiene una lista di passaggi intermedi        |

<h2 id="tocS_Parameters">Parameters</h2>
<!-- backwards compatibility -->
<a id="schemaparameters"></a>
<a id="schema_Parameters"></a>
<a id="tocSparameters"></a>
<a id="tocsparameters"></a>

```json
{
  "maxIterazioni": 0,
  "passaggiIntermedi": true
}

```

### Properties

| Name              | Type           | Description | Default  |
| ----------------- | -------------- | ------------ | ----------- |
| maxIterazioni     | integer(int32) | false|   numero massimo di iterazioni per risoluzione con tagli di gomory        |
| passaggiIntermedi | boolean        | false|    noneindica se ritornare i passaggi intermedi (es simplesso primale e duale)     |

<h2 id="tocS_PublicProblem">PublicProblem</h2>
<!-- backwards compatibility -->
<a id="schemapublicproblem"></a>
<a id="schema_PublicProblem"></a>
<a id="tocSpublicproblem"></a>
<a id="tocspublicproblem"></a>

```json
{
  "funzioneObbiettivo": {
    "tipo": "MIN",
    "c": [
      0.1
    ]
  },
  "vincoli": [
    {
      "vincolo": [
        0.1
      ],
      "verso": "LE"
    }
  ],
  "parameters": {
    "maxIterazioni": 0,
    "passaggiIntermedi": true
  }
}

```

### Properties

| Name               | Type                                            | Required | Restrictions | Description |
| ------------------ | ----------------------------------------------- | -------- | ------------ | ----------- |
| funzioneObbiettivo | [FunzioneObbiettivo](#schemafunzioneobbiettivo) | true    | none         | none        |
| vincoli            | [[Vincolo](#schemavincolo)]                     | true    | none         | none        |
| parameters         | [Parameters](#schemaparameters)                 | true    | none         | none        |

<h2 id="tocS_Response">Response</h2>
<!-- backwards compatibility -->
<a id="schemaresponse"></a>
<a id="schema_Response"></a>
<a id="tocSresponse"></a>
<a id="tocsresponse"></a>

```json
{
  "tableau": [
    [
      "string"
    ]
  ],
  "soluzione": [
    "string"
  ],
  "time": 0,
  "passiRisoluzione": [
    {
      "message": "string",
      "tableau": [
        [
          "string"
        ]
      ],
      "passaggiIntermedi": [
        {}
      ]
    }
  ],
  "error": "string"
}

```

### Properties

| Name             | Type                        | Required | Restrictions | Description |
| ---------------- | --------------------------- | -------- | ------------ | ----------- |
| tableau          | [array]                     | false    | none         | none        |
| soluzione        | [string]                    | false    | none         | none        |
| time             | integer(int64)              | false    | none         | none        |
| passiRisoluzione | [[Message](#schemamessage)] | false    | none         | none        |
| error            | string                      | false    | none         | none        |

<h2 id="tocS_Tipo">Tipo</h2>
<!-- backwards compatibility -->
<a id="schematipo"></a>
<a id="schema_Tipo"></a>
<a id="tocStipo"></a>
<a id="tocstipo"></a>

```json
"MIN"

```

### Properties

| Name        | Type   | Required | Restrictions | Description |
| ----------- | ------ | -------- | ------------ | ----------- |
| *anonymous* | string | false    | none         | none        |

#### Enumerated Values

| Property    | Value |
| ----------- | ----- |
| *anonymous* | MIN   |
| *anonymous* | MAX   |

<h2 id="tocS_Verso">Verso</h2>
<!-- backwards compatibility -->
<a id="schemaverso"></a>
<a id="schema_Verso"></a>
<a id="tocSverso"></a>
<a id="tocsverso"></a>

```json
"LE"

```

### Properties

| Name        | Type   | Required | Restrictions | Description |
| ----------- | ------ | -------- | ------------ | ----------- |
| *anonymous* | string | false    | none         | none        |

#### Enumerated Values

| Property    | Value |
| ----------- | ----- |
| *anonymous* | LE    |
| *anonymous* | GE    |
| *anonymous* | E     |

<h2 id="tocS_Vincolo">Vincolo</h2>
<!-- backwards compatibility -->
<a id="schemavincolo"></a>
<a id="schema_Vincolo"></a>
<a id="tocSvincolo"></a>
<a id="tocsvincolo"></a>

```json
{
  "vincolo": [
    0.1
  ],
  "verso": "LE"
}

```

### Properties

| Name    | Type                  | Required | Restrictions | Description |
| ------- | --------------------- | -------- | ------------ | ----------- |
| vincolo | [number]              | false    | none         | none        |
| verso   | [Verso](#schemaverso) | false    | none         | none        |


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