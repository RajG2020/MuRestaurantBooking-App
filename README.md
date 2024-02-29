# RestaurantBooking-App

This is a Java application for managing customer restaurant bookings.

## Overview

The Restaurant Booking App allows customers to book tables at the restaurant and enables the restaurant owner to view all bookings for a particular day. This application is built using MuServer, a lightweight HTTP server library for Java.

## Features

- Book a table by providing customer details, date, time, and table size.
- View all bookings for a particular day.

## Setup

1. **Clone the repository:**

    ```bash
    git clone <repository-url>
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Run the application:**

    ```bash
    java -jar target/MuRestaurantBookingApp-0.0.1-SNAPSHOT.jar
    ```

4. **Access the application:**

     access the application using Web Browser/Postman for  checking applciation health by using API call: http://localhost:8080/health


Perform below opearations using Postman:

POST Opearion: http://localhost:8080/book
Sample Json request paylod:
{
  "name": "Raj G",
  "tableSize": "1",
  "date": "2028-02-28",
  "time": "14:00"

}

GET Opeartion: http://localhost:8080/bookings?date=2028-02-28 
Note: date is query paramter (Date format:YYYY-mm-dd)
Sample Json response paylod:
[
    {
        "name": "Raj G1",
        "tableSize": "1",
        "date": "2028-02-28",
        "time": "14:00"
    }
]
"# MuRestaurantBooking-App" 
"# MuRestaurantBooking-App" 
