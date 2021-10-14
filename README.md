# Cowin Slot Tracker Android Application

* This App was made for helping the People getting the Vaccine Slots easily as soon as the Slots becomes available.

* The Android App lets the User add Pincodes to track and also the Age Group (i.e. 18+, 45+, or Both).
* The User can also search the Pincodes inside the App for easy viewing of the available slots. The
* The App is built with Jetpack Compose,Hilt and a Clean App Architecture(MVVM)
* For notifications, A Django backend is used with Celery-Beat Scheduler which schedules the tasks for searching the Pincode every 5 Minutes. If the Slot is Available it uses Firebase Cloud Messaging Service to send the message to the particular user which has subscribed to that Pincode.

[Backend Repository Link](https://github.com/samyakj2307/cowin-slot-tracker-backend)

# Demo Screenshots

<br/>

## Home Screen

<img src="./demoImages/2.jpg" width="324" height="720" alt="Home Screen">

<br/>

## Search Pincode

<img src="./demoImages/3.jpg" width="324" height="720" alt="Search Pincodes">

<br/>

## Center Information

<img src="./demoImages/4.jpg" width="324" height="720" alt="Center Information">

<br/>

<img src="./demoImages/5.jpg" width="324" height="720" alt="Center Information">

<br/>

## Track Pincode Screen

<img src="./demoImages/6.jpg" width="324" height="720" alt="Track Pincode">
<br/>

## Add Pincode for Tracking

<img src="./demoImages/7.jpg" width="324" height="720" alt="Add Pincode for Tracking">
<br/>
<img src="./demoImages/8.jpg" width="324" height="720" alt="Add Pincode for Tracking">
<br/>
<img src="./demoImages/9.jpg" width="324" height="720" alt="Add Pincode for Tracking">
<br/>


## Get Notifications

<img src="./demoImages/10.jpeg" width="324" height="720" alt="Get Notifications">
<br/>









