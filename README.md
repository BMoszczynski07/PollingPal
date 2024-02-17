# PollingPal Documentation

## Overview
PollingPal is a full-stack platform (NextJS + Spring Boot + MariaDB) designed for creating and commenting on surveys anonymously or using user accounts.

## Branches
- license -> includes the MIT license and documentation
- mobile -> includes mobile application
- web -> includes web application
- API -> includes API built in Spring Boot for CRUD operations on polls

## Features

- **Survey Creation:**
  - Users can create surveys with various question types, such as multiple choice, short answer, and more.
  - Surveys can be customized with titles, descriptions, and optional images.

- **Anonymous Participation:**
  - Surveys can be taken anonymously without the need for user authentication.
  - Results are aggregated anonymously to maintain privacy.

- **User Accounts:**
  - Users have the option to create accounts for personalized experiences.
  - Account holders can track their survey participation history.

- **Commenting System:**
  - Participants can leave comments on surveys to provide feedback or discuss specific questions.
  - Moderation tools are available to manage and filter comments.

## Technologies Used

- **Frontend:**
  - Built with [Next.js](https://nextjs.org/) for a fast and efficient React-based frontend.

- **Backend:**
  - Developed using [Spring Boot](https://spring.io/projects/spring-boot) to handle server-side logic and database interactions.

- **Database:**
  - Utilizes [MariaDB](https://mariadb.org/) for storing survey data and user information.

- **Mobile app:**
  - Developed in Java using Android Studio IDE

## Getting started
  1. Clone all the branches from the repository:
   ```bash
   git clone -b <branch-name> https://github.com/BMoszczynski07/PollingPal.git 
   ```
  2. Install all dependencies for web application:
  ```
  npm i
  ```
  3. Run mobile application, API by clicking the run button in your IDE and web application by typing in console:
  ```
  npm run dev
  ```
  4. Enjoy!
