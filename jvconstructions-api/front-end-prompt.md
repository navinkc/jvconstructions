# Front End Application - UI for JV Constructions

## Overview
This project has the prompts for building the front-end UI application for the JV Constructions. It was built on react
framework.

## Tech-stack
- Framework - React
- Language - Typescript
- Host server - AWS EC2

### Reference site : https://hireandbuild.com

## Front-end project infra setup
- Look at the tech-stack section and create the initial project in the directory.
- The project is going run as a separate server on AWS EC2.
- The deployment for the frontend has to be managed inside it's root directory.
- This should be compatible to the backend project in the dir `/Users/navinkc/Desktop/Arcadia/Navin/jv/jvconstructions-api/`

## Features
- Take the reference site as a model `important`
- This is a front end application where the user will interact on it.
- It should be more interactive.
- It should have a home page with navbar (ref the reference site).
- From the reference site, include only the below navigation options,
  - Home
  - About Us
  - Services
  - Projects
  - Contact
- We can have the footer just like the reference

### Endpoint documentation - API_DOCUMENTATION.md 

## Initial Prompts
- Create a React project which should be compatible to the backend application in the dir `/Users/navinkc/Desktop/Arcadia/Navin/jv/jvconstructions-api`.
- Go through the backend project thoroughly before making any changes in front-end.
- Look at the tech-stack section as a reference while creating the project.
- The code should be more simple and modular.
- Modularize the code into different components.
- Maintain the css code in a separate css file.
- Mainly the methods should not exceed a max of 10 lines of code.
- Try to split up the methods and make it reusable.
- Handle all the CORS related errors properly and simplify it. This should be compatible to the backend as given earlier.
- For now just create a homepage with the navbar and the foot bar.
- We have the below major endpoints,
  - projects - for the projects and related data handling
  - services - for the services and related data handling
  - enquiries - for the enquiries and related data handling
  - users - for managing the user data
- For more information related to the API endpoints, take a look at the Endpoint documentation.