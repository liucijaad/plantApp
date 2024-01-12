# Functional Specification  
## 1. **Introduction**  
1. **Overview**  

PotPal is an Android application. The primary objective of this application is to streamline, simplify and enhance the process of caring for indoor plants. With this app, users can effortlessly add their plants to the tracker, and then the app will allow them to create their plant profile.  
  
By creating a plant profile, users can take a picture of the plant with the app, and PotPal will identify the plant’s category (desert, cacti, tropical, etc.). Based on the category and environmental conditions the application will suggest personal care schedules. Users then are able to log care activities and the application will warn them if the plants are not getting the correct amount of water. Notifications will be used to remind the users about plant watering days. Furthermore, if users encounter any visual symptoms or health issues displayed by their plants, the app provides resources to help them diagnose and address potential problems.  
  
2. **Glossary**  
  
Click-distance - number of clicks (taps) a user needs to perform to complete a task.  
  
Sharpening - enhancing the clarity, definition, or visual distinction of certain elements to make them more prominent or noticeable.  
  
## 2. **Description**  
2.1. **Product / System Functions**  
  
PotPal offers a minimalistic and simple solution for the care of indoor plants. The application’s functionality can be broken down into several key features:  
  
1. Plant Tracker  
1.1. Users can effortlessly create profiles for their plants.  
1.2. Each plant profile is unique for each plant.  
2. Plant Profile Creation  
1.1. Users can take pictures of their plants using the app.  
1.2. The application recognises and categorises the plant.  
1.3. The application asks the user about the plant’s environment.  
3. Personalised Care Schedules  
1.1. Based on the identified category and environmental factors, PotPal suggests a personalised care schedule.  
1.2. The care schedule includes watering frequency, method and amount of water.  
4. Watering Reminders  
1.1. Users can mark the days they water their plants on the app as well as the method and the amount of water used.  
1.2. The application stores this information to track the watering frequency.  
1.3. Notifications are sent to remind the users about watering days.  
5. Health Monitoring  
1.1. If the application detects that the plants are not receiving the correct amount of water, it issues warnings.  
1.2. Users are also able to log any visual symptoms or health issues displayed by the plants.  

2.2. **Characteristics and Objectives**  
  
This application is aimed at novice gardeners, who might need extra information and help while taking care of their plants. From a novice gardener’s perspective, the application must provide accurate information about the care of plants. It must also present this information in simple, easy-to-understand terms.  
  
The users of the application will be indoor plant enthusiasts as PotPal only gives personalised care schedules for indoor plants. The application must be able to correctly identify and categorise plants and consider environmental factors when creating a personalised care schedule.  
  
Because PotPal is an Android application, its users will be Android smartphone owners. Only a basic understanding of smartphone gestures (such as swiping and tapping) is needed to use the application. The application must have an intuitive user interface with clearly labelled buttons and menus. A short tutorial describing ways of interacting with the application is also needed.  
  
PotPal offers a simple user interface that is aimed at minimalistic users. Because of this, the user interface must properly sharpen and group information without adding unnecessary visual clutter.  
  
This application is mostly suited for casual users. It must not encourage competitiveness or require daily usage of the application. The time spent using the application must also be minimised. Notifications must only be sent on days when plant care is needed.  
  
The main objective of the application is to simplify and streamline the care of indoor plants. From the user’s perspective, this is achieved through a well-designed user interface. The click distance for most commonly used features must be less than 5. The notifications reminding of plant care must be sent at an appropriate time ideally, the user should be able to choose when they would like to receive these reminders. The user should also be able to choose how many reminders they would like to receive and the notifications about plant care activity should stop once the user logs the activity in the application.  
  
The application should also aim to provide enough basic information about the plant and its care. The plant profile should be customisable, minimalist yet informative. Users should be able to see the care schedule at a glance as well as a summary of recent plant care.  
  
2.3. **Operational Scenarios**

**Scenario 1: New User**  
  
A user downloads the PotPal application. The first time they launch the application, they are guided by a tutorial to create a plant profile. Following the steps outlined on the screen, the user taps the “+” button. PotPal asks for permission to access the camera. The user takes a photograph of the plant using the application. PotPal identifies the plant as a succulent. The user is prompted to name the plant. After giving the plant a name, they are asked about the plant's environmental conditions - the amount of sunlight, humidity and temperature of the room. After collecting the information, PotPal asks the user for the most recent date when the plant was watered. Based on the information provided, the application suggests the next watering date as well as the amount of water and method of watering the plant needs. The user is prompted to select the timeframe they wish to receive notifications about the suggested plant watering time.  
  
  **Scenario 2: An Existing User**  
  
A user received a notification from PotPal suggesting to water the orchid, whose profile was created in the past. The user taps on the notification and is brought to the plant profile. The application suggests to the user to check the plant’s soil. The user reports to the application that the soil is dry. PotPal suggests to put the plant in the container with lukewarm water for 15 seconds. The user follows the application’s suggestions and marks that the plant was watered that day. The application shows the next plant watering date and the user closes the application.  
  
  2.4. **Constraints**  
  
PotPal is an Android application and therefore it is constrained to devices running Android versions 13 and newer. The device that runs the application must also have a working camera. It must also have a working Internet connection. The user interface must also be properly displayed in most common smartphone resolutions. The application also must not use more than 4GB of RAM. The development of the application is constrained to the Android Studio integrated development environment.  
  
## 3. **Functional Requirements**  
1. **Plant identification**

*Description*  
  
The Plant Identification feature is a core functionality of PotPal, enabling users to identify their indoor plants by capturing a photo using their Android smartphones. The application will utilise the PlantNet API to analyse the image and categorise the plant into its respective group. We aim to make the identification process as smooth and intuitive as possible, providing users with accurate and quick results.  
  
*Criticality*  
  
This requirement is critical to the overall system as it will serve as the basis for personalised care recommendations. The accuracy and speed of plant recognition directly impacts the effectiveness of subsequent features.  
  
*Technical Issues*  
  
Implementation requires seamless integration with the PlantNet API, necessitating a strong communication mechanism between the mobile app and the external API. Efficient image processing algorithms must be employed to ensure quick and accurate plant identification.  
  
*Dependencies with other requirements*  
  
Plant identification is tightly linked with the Personal Care Schedules feature, as identified plants will be used to generate customised care plans. In other words, without correctly recognised plants, the schedule might create incorrect plans, causing damage to the plant. Additionally, the identified plant data needs to be stored in a database for future reference, establishing a dependency on the Database Management component.  
  
2. **Personal Care Schedules**

*Description*  
  
The Personal Care Schedules feature enables users to create and follow personalised care plans for their indoor plants. Upon successful plant identification, the application prompts users for environmental conditions (air humidity, pot size, amount of light) and generates optimal watering schedules based on the plant species and input data. Users will have the flexibility to freely modify their suggested plans according to their preferences.  
  
*Criticality*  
  
This requirement is crucial to the success of our project as it directly influences the well-being of the plants. The accuracy of care schedules depends on the user’s input and their ability to take care of their plants according to their recommendations.  
  
*Technical Issues*  
  
Implementation of this feature might involve algorithmic calculations based on user-provided information, requiring careful consideration of various environmental factors. The system must be able to adapt and update schedules dynamically based on user modifications and errors.  
  
*Dependencies with other requirements*  
  
This requirement heavily relies on the successful completion of the Plant Identification feature to gather relevant plant information. Additionally, the generated care schedules will be stored in the database for future reference and real-time monitoring, establishing a dependency on the Database Management.  
  
3. **Plant health Companion**

*Description*  
  
The Plant Health Companion feature monitors users' care routines in real-time, providing feedback on potential issues such as over or under-watering. The system should automatically adjust watering schedules if needed. Notifications will be utilised to remind users of watering days, and the application will offer information on optimal conditions for their plants. In case of visual symptoms or health issues, the app will provide resources for diagnosis and solutions.  
  
*Criticality*  
  
This requirement is critical for maintaining the health of the plants and ensuring users receive timely feedback.Real-time monitoring and proactive feedback contribute significantly to the effectiveness of plant care.  
  
*Technical Issues*  
  
Implementing an efficient monitoring algorithm that can adapt to changing conditions and accurately detect plant health issues is essential. Integration with the notification system should be able to deliver timely alerts.  
  
*Dependencies with other requirements*  
  
This requirement is closely tied to the accurate identification of plants and the generation of personalised care schedules. The success of this feature relies on the information obtained from these processes.  
  
4. **Notifications and Reminders**

*Description*  
  
The application must feature a notification system to alert users about watering days, system updates, and potential plant health issues. Notifications should be timely, informative, and customizable to accommodate user preferences.  
  
*Criticality*  
  
This requirement is critical for keeping users informed and engaged with the application. It is also important when it comes to maintaining consistent care routines. Those notifications contribute to the success of the Care Scheduling and Plant Health Monitoring features.  
  
*Technical Issues*  
  
Implementation involves integrating with the Android notification system. Keeping the alerts consistent and yet non-intrusive might also prove to be a challenge in itself.  
  
*Dependencies with other requirements*  
  
This requirement plays a crucial role when it comes to actually helping the users with their plantcare routines. Notifications play a key role in alerting users about plant health issues detected by the monitoring system, and they contribute to the overall user experience.  
  
## 4. **System Architecture**  
  
The system architecture can be delineated into three main modules: *User Interface (UI)*  
  
This module encompasses the frontend components responsible for user interaction. It includes screens for plant identification, care schedules, health monitoring, user authentication, and notification preferences. The UI interacts with backend components to fetch and display relevant data.  
  
*Backend Services*  
  
This module, hosted on the server, consists of backend services responsible for handling user authentication, plant identification using the PlantNet API, managing user data in the SQLite database, generating personalised care schedules, and monitoring plant watering habits.  
  
*Third-Party Integration*  
  
The PlantNet API serves as a crucial third-party component integrated into the backend for plant identification. Additionally, the application utilises Android's built-in notification system for delivering reminders and alerts.  
  
## 5. **High-Level Design**  
  
![class diagram](https://cdn.discordapp.com/attachments/766607481979535380/1180168195190030376/classes_i_guess.png)
  
*UserInterface* class represents the frontend of the application, responsible for displaying various screens related to plant identification, scheduling, health monitoring, authentication, and notification preferences.  
  
*User* class represents the application's users, with methods for authentication. *Schedule* class encapsulates the plant care schedule details, including watering days.  
  
*Plant* class represents individual plants, including their identification details, group, and environmental conditions.  
  
*Garden* class manages a list of plants, providing methods to add, remove, and retrieve plants. *Environment* class encapsulates environmental factors affecting plant care.  
  
![](https://cdn.discordapp.com/attachments/766607481979535380/1180168219219202139/highleveldiagramidk.png)
  
**Step 1: Register** Users begin their PotPal journey by registering for an account using a unique username and password.  
  
**Step 2: Log in** After registration, users can access their PotPal profile by logging in using their unique credentials. Successful authentication grants access to their personalised profile.  
  
**Step 3: Add Plants** Once logged in, users can add their indoor plants to their app. Once the plant is identified users can add photos of their plants, add their names and environmental conditions.  
  
**Step 4: Create/Modify Schedule** After adding plants, users can automatically create personalised watering schedules based on their plants’ species and preferred conditions.  
  
**Step 5: Get Watering Notification** Users are reminded whenever their plants need to be watered or if they are falling behind on their schedule.  
  
**Step 6: Mark Watering Days/Times** Users actively engage in the care of their plants by marking watering times. Based on those, the app will provide real-time feedback on whether they are under- or over-watering their plants.  
  
**Step 7: Identify Problems** In case users notice visual symptoms or health problems of any kind, they will be provided with resources suggesting what might be the probable cause of the issue.  
  
## 6. **Preliminary Schedule**  
  
**Task 1: API** Ensure the calls to API are working and the data is formatted appropriately for further use.
**Tasl 2: Plant Profile** Create system to build a plant profile. Based on API call results and user input.
**Task 3: Care Schedule** Implement Personalised Care Schedule algorithm with watering day predictions.
**Task 4: UI** Implement working User Interface.
**Task 5: Tutorial** Implement tutorial that explains how to use UI and functionalities of the application.
**Task 6: Notifications** Implement notification reminder system for watering days.
**Task 7: Health** Implement health companion with most common plant disease symptoms.

![](https://cdn.discordapp.com/attachments/1048367126160867419/1180175823081312286/gantt.png)

