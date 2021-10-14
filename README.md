# Cowin Slot Tracker Android Application

* This App was made for helping the People getting the Vaccine Slots easily as soon as the Slots becomes available.

* The Android App lets the User add Pincodes to track and also the Age Group (i.e. 18+, 45+, or Both).
* The User can also search the Pincodes inside the App for easy viewing of the available slots. The
* The App is built with Jetpack Compose,Hilt and a Clean App Architecture(MVVM)
* For notifications, A Django backend is used with Celery-Beat Scheduler which schedules the tasks for searching the Pincode every 5 Minutes. If the Slot is Available it uses Firebase Cloud Messaging Service to send the message to the particular user which has subscribed to that Pincode.

# Demo Screenshots

<br/>

## Home Screen

![Home Screen](./demoImages/2.png?raw=true "Home Screen")

<br/>

## Search Pincode

![Search Pincode](./demoImages/3.png?raw=true "Search Pincodes")

<br/>

## Center Information

![Center Information](./demoImages/4.png?raw=true "Center Information")

<br/>

![Center Information](./demoImages/5.png?raw=true "Center Information")

<br/>

## Track Pincode Screen

![Track Pincode](./demoImages/6.png?raw=true "Track Pincode")
<br/>

### Add Pincode for Tracking

![Add Pincode for Tracking](./demoImages/7.png?raw=true "Add Pincode for Tracking")
<br/>
![Add Pincode for Tracking](./demoImages/8.png?raw=true "Add Pincode for Tracking")
<br/>
![Add Pincode for Tracking](./demoImages/9.png?raw=true "Add Pincode for Tracking")

### Get Notifications

![Get Notifications](./demoImages/10.png?raw=true "Get Notifications")
<br/>









