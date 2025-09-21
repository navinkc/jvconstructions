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

### API endpoint documentation path - API_DOCUMENTATION.md 

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
- For more information related to the API endpoints, take a look at the API endpoint documentation.

## Prompts after the infra creation
- I can see that the app is running without any errors as per the initial prompts.
- When I click on the Services and Projects, there's a message `Failed to fetch services` and `Failed to fetch projects`. Please check and fix it by fetching the values from the API endpoints.
- For now let's work on the services endpoint and the pages related to services.
    - As per the given reference site, the services should be a dropdown.
    - It should list all the values available from the endpoint (Refer services endpoint section `## 3. Services API` from the API_DOCUMENTATION.md).
    - Get only the names from the endpoint response and display it in the dropdown.
    - The dropdown should be dynamic. For eg, if the get all services endpoint returns only one service, it should return only one item.
    - Same way, if the get all services endpoint returns multiple services, the dropdown should have multiple items.
    - When a user clicks on any service, in the backend we can hit the `### 3.2 Get Service by Name` endpoint and display the content in a separate page.
    - We should not allow the user to click on the Services in the navbar. A user can select only one service at a time and it should navigate to that particular service's page.
    - We can have a separate service page as a component and fetch the values from the endpoint.
    - Refer the site `https://hireandbuild.com/home-construction-company-in-chennai/` for the services page.
- Main things to consider - when there is no response from the backend or backend is having any internal server error. Please display an appropriate a message something like `Sorry for the inconvenience. Internal server error. Try after sometime!`

## Issues to be fixed
- I can see that there's an error in fetching the services data from the backend though the backend application is running on port 8082.
- I have attached the screen shot of the error. 
- Please check and go through the backend project and the API_DOCUMENTATION.md thoroughly before proceeding for any fix.
- Add more logs of the backend response for the better visibility.
- Try to resolve that error and make it to work fine.

## Services dropdown prompt
- Currently, the services dropdown looks like a traditional style.
- Make the dropdown more modern and stylish. The up and down arrows switching can be removed.
- It should look like a normal nav bar option but when we hover on it, the dropdown options should be listed.
- The elements of the dropdown should be aligned in center and the width of the dropdown can be expanded based on the max size of an element.
- When hovering on the dropdown elements, instead of changing the color of the text, make service element itself to highlight and do not change the color of the hovered element.

### Issues to be fixed in services dropdown prompt
- Attached image of the current services dropdown.
- When hovering on it, an underline appears which is good and this can be applied to all the navbar options.
- The Services dropdown option is not properly aligned with the other navbar options. Align all the navbar options in a single line.
- The other major issue is, when hovering on the dropdown elements, the elements are getting highlighted with a blue color which is blocking the visibility of the values.
- Do not highlight the elements when hovering on it but instead, zoom that particular element a little bit.
- Strictly do not change the color of the elements when hovering.
- Also the width of the dropdown is expanded based on the max size of an element which is fine but add a padding in both left and the right sides.

## Home page detailing prompts
- Current home page looks good now but let's add some more styles to it.
- Refer the reference site (https://hireandbuild.com/).
- Before proceeding with the changes, study the API_DOCUMENTATION.md file thoroughly.
- Check how the images are coming in the responses.
- Study the backend project (jvconstructions-api) once completely for the better understanding of the API handling.
- Just like the reference site, can you add a pictures of the project's images in the background?
- Instead of the background color let's add the random images from the projects.
- Mainly the data in the home page should be visible clearly and the background image should not block the data visibility.
- To obtain this we can adjust the transperancy of the image so that the data/contents of the home page can be displayed clearly.
- The background image can be changed randomly in a frequency. Which means, we can display the image of one project for 10 or 20 seconds then the another project's image can be displayed. With this the site looks more attractive.
- This background image displaying should work in a cycle.
- When there is only one project available, we can display that one particular image in the background. No need of this image cycling process in this case.
- If there is only one project and that one project has multiple images, then we can continue with the background image cycling process.
- When there is no projects or the project is available but there is no associated images, then we can display the colored background (current home page).
- Look at the reference site for any reference related queries.

## Fixes to be made in homepage detailing
- I have attached the screen shot of the current homepage.
- Currently the background image is getting displayed only in a particular area.
- Instead can you make it to be displayed in the navbar too? When the website is scrolled down, then the navbar can be turned to the white color.
- If it is on the homepage and unscrolled, the the background image can be displayed in the navbar as well.
- To make the image background section bigger, can you put the "Send Us a Message" form in the homepage as well? Just like the reference site?
- So that the first homepage section after navbar can be little bigger which makes the background image section bigger and will get more space for the visibility.
- This makes the website more attractive to the users.

## Current issues and fixes with the recent changes
- I have observed that the navbar and the homepage section's are displaying different images.
- When the homepage is scrolled down, the navbar is turning back to it's original form which is correct.
- But when the homepage is scrolled up again, the navbar remains in the same form and not displaying any background images. The same issue with while navigating back to the home page from a different page.
- Also on the navbar, when the background images are getting displayed, the text of the navbar options are not visible.
- Make the changes in a way that there is no change in displaying the background image for navbar and the homepage section. It should display one image in both navbar and homepage.
- The navbar should be turned to white only when the homepage is scrolled down. If the homepage is scrolled up, we should display the images as stated. The same should apply when it is navigated back to the homepage from anyother pages.
- When displaying the images in the navbar and homepage, the navbar options should be visible properly. We can display the text of the navbar options with white color or with any color so that the options can be properly viewed.
- It mainly affects the Services dropdown as that is having the dropdown feature.
- When it is scrolled down, we can follow the current navbar but while displaying the images, the options should be properly visible.
- Another important changes is on the homepage is the Send Us a Message form. The form can be transperant instead of the white background.
- Also no need of the caption "Send Us a Message" in the form as it is affecting the website design. Users will understand by looking at it.
- The fields of the form can have the white fill and the "Send Message" button can have it's color just to maintain the form's consistancy. Apart from this make the form transparent so that the background image can be viewed properly.
- I have attached the screen shot of the current homepage.
- Make sure the changes doesn't affect the current flow.

## Issues to be fixed in homepage
- Still the issues are not fixed. Navbar displays one image and the hero section displays the other.
- The navbar and the homepage hero section should be aligned on displaying the images in the background.
- When the image changes due to the frequency, both the navbar and hero section should display the same image. There's no differentiation.
- Also the services dropdown with the navbar background image is still not fixed. The text of the dropdown elements are not visible. We can probably maintain a different styling to the dropdown when the navbar with the background image.
- Specifically the text padding of the dropdown and the text is not proper when the navbar is with the background image display.
- I have attached the screenshot. Please look at it and also take the reference site (https://hireandbuild.com/) for better understanding.

## Footer fixing prompt
- Current footer is good but we have to make a small changes here are there.
- The footer has "Quick Links" section. This has Services options too. Since services is a dropdown option and there is a separate section in footer called "Services" , remove the Services from the Quick Links section.
- The current "Services" section in footer is showing the random options.
- Instead display the services that we are currently displaying in the services dropdown in the navbar.
- Make sure that the height of the footer should be dynamically changed according to the size of the services list items.
- Do not change any formatting or styling in the current footer.