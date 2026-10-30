# Project Name

## Vision

Briefly describe the purpose of your project in 1–2 sentences.

Example:

> This project is a backend API for managing a farm, specifically with sows.  
> The system allows users to track, move, find etc. animals of their choice or places, like pens and sections, even farms.

---

## Links

Deployed application (optional):  
<LINK>

Source code repository:  
<LINK>

Portfolio website:  
<LINK>

---

# Architecture

## System Overview

Describe the overall structure of the system.

Example:

This project is built as a layered backend architecture using:

- Controller layer (REST endpoints)
- Service layer (business logic)
- Repository layer (database access)

Technologies used:

- Java
- Javalin
- JPA / Hibernate
- PostgreSQL
- Maven
- Lombok and Jackson
- JUnit 6, Hamcrest, Testcontainers and Rest Assured for testing
- Open-Meteo (external weather API)

---

## Architecture Diagram

(Optional but recommended)

You can include a simple diagram showing the main components.

Example:

Or include an image if you created a diagram.

---

## Key Design Decisions

Describe important technical decisions you made.

Examples:

- Why you chose your project structure
- How authentication works
- How errors are handled
- Why you chose specific libraries or frameworks

Example:

**Authentication**

Authentication is implemented using JWT tokens.  
Users log in and receive a token which must be included in subsequent API requests.

---

# Data Model

## ERD

Insert your Entity Relationship Diagram here.
![ERD](docs/erd.svg)

Example entities:

- User
- Activity
- Workout
- Exercise

You may include:

- an image
- a link to a diagram
- or a textual explanation

---

## Important Entities

Briefly describe the main entities in your system.

### User

Represents a registered user in the system.

Fields:

- id
- username
- password
- role

### Activity

Represents a recorded workout activity.

Fields:

- id
- userId
- date
- description

---

# API Documentation

Describe the most important endpoints.

## Example Endpoints

### Create User

Request body:

Response:

---

### Get All Activities

Response:

---

# User Stories

1. ✅**Som** landmand **vil** jeg oprette en gård med et navn, **så** jeg kan holde styr på mine dyr. 
2. ✅**Som** landmand **vil** jeg tilføje sektioner til gården, **så** stalden er opdelt som i virkeligheden
3. ✅**Som** medarbejder **vil** jeg registrere en so med øremærke, type og boks, **så** den er registreret i systemet
4. ✅**Som** medarbejder **vil** jeg flytte en so fra stald til stald, eller ligeledes sti til sti, **så** jeg ved hvor hun står
5. 🟡**Som** landmand **vil** jeg se en liste over alle søer på gården, **så** jeg har overblik over besætningen
6. 🟡**Som** medarbejder **vil** jeg filtrere søer på sektion, type eller tilstand, **så** jeg hurtigt finder dem jeg skal arbejde med
7. ✅**Som** medarbejder **vil** jeg se hvilke bokse og stier der er tomme, **så** jeg ved hvor jeg kan placere en so
8. 🟡**Som** medarbejder **vil** jeg rette på en sos oplysninger, inklusivt slagt, død og aflivning, **så** data er korrekte og op til dags dato
9. ✅**Som** landmand **vil** jeg se temperatur, luftfugtighed og regn for gårdens placering, **så** jeg kan vurdere forholdene for dyrene
10. ❌**Som** ejer **vil** jeg have at kun medarbejdere med login kan se og ændre data, så uvedkommende ikke kan ændre i besætningen
11. ❌**Som** medarbejder **vil** jeg se de vigtigste opgaver på dags dato, såsom uddiligering af medicin, vacciner eller altresyn, **så** besætning forbliver rask og i tempo med ugens opgaver
12. ❌**Som** medarbejder **vil** jeg udregne antal A-søer nødvendige til dagens faringer, **så** grisene ikke skal slås om næringen
13. ❌**Som** driftsleder **vil** jeg se hvem der har rykket hvilke dyr, hvor og hvornår, **så** vi kan have et godt samarbejde
14. ❌**Som** medarbejder **vil** jeg se hvilke søer der skal scannes, i forhold til deres tilstand og dage, **så** vi kan finde tomme søer i tide til næste brunst

---

# Development Notes

(Optional section)

Here you can briefly describe important challenges or lessons learned during development.

Examples:

- Handling authentication with JWT
- Designing the database schema
- Structuring the service layer
- Testing the API