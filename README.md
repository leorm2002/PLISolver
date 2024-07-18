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
    ./mvnw quarkus:dev
    ```

## Usage

The solver can be used via the command line or integrated as a library in your Java application.
