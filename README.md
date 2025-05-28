# 🌤️ Weather Forecast App

A simple and responsive Weather Forecast application built using Angular 14 for the frontend and Java Spring Boot for the backend. Users can enter a city name and retrieve the current weather data (temperature, humidity, conditions, etc.). The frontend is securely integrated with a backend proxy to fetch weather information without exposing the API key.

## 🌐 Live Demo

Access the deployed app here:  
http://weatheruiapp.s3-website.us-east-2.amazonaws.com

## 🛠️ Tech Stack

### Frontend:
- Angular 14
- HTML, CSS
- Hosted on AWS S3 (Static website hosting)

### Backend:
- Java Spring Boot
- REST API to proxy weather API calls
- Deployed on AWS EC2

### Weather API:
- OpenWeatherMap 

## 🔐 Features

- Users can search weather by city name.
- Displays temperature, humidity, and weather conditions.
- Backend proxy setup to protect the API key.
- Graceful error handling (e.g., invalid city names, network issues).
- Includes unit tests for Angular components and Spring Boot services.
