# StepBloom - Project 2 🌸

A modern health activity tracking application built with Android Jetpack Compose that encourages users to record daily activities and cultivate a healthier lifestyle. This project aligns with **UN Sustainable Development Goal 3: Good Health and Well-being**.

---

## 👤 Student Information

| Field | Details |
| :--- | :--- |
| **Name** | Li ShangYu |
| **Matric No** | A207419 |
| **Course** | Mobile Programming / Mobile Application Programming |

---

## 🎯 SDG Theme: SDG 3 (Good Health and Well-being)

> **Our Mission:** StepBloom serves as a personal wellness companion. By allowing users to log activities, visualize outcomes, and track progress, it empowers individuals to develop consistent health habits and improve their daily quality of life.

---

## 📝 Project Description

StepBloom (Project 2) significantly extends the foundational UI and user flows established in Project 1 by integrating advanced, real-world components:
* **Local & Cloud Persistence:** Robust data management using Room for offline storage and Firebase Firestore for cross-device synchronization.
* **Network APIs:** Asynchronous networking using Retrofit to ingest external REST API resources.
* **Hardware Sensors:** Direct interaction with device hardware via the Android Sensor API to capture real-time physical movement.

---

## ✨ Features

### 🖥️ App Screens
* **Home Screen:** The central dashboard giving an overview of the application and quick navigation.
* **Add Activity Screen:** An intuitive form for users to log new activities daily.
* **Activity Result Screen:** Displays dynamic summaries and motivational feedback on recorded activities.
* **Summary List Screen:** A comprehensive history viewing component showcasing all logged historical data.
* **Profile / Goal Screen:** Dedicated space for user profile configurations and setting fitness objectives.
* **API Data Screen:** A dynamic interface displaying externally fetched web service records.
* **Sensor Data Screen:** Live diagnostics component showing physical motion feedback directly from device hardware.

### ⚙️ Core Technical Capabilities
* **Room Local Database:** Full CRUD support ensuring data persists reliably even while offline.
* **Firebase Firestore:** Automated data streaming to cloud infrastructure instantly when a user creates metadata.
* **Retrofit API Integration:** Graceful asynchronous networking to retrieve data from a public REST endpoint.
* **Accelerometer Integration:** Low-latency tracking of physical device shakes and step motions.

---

## 🛠️ Technologies Used & Architecture

The application is built entirely using modern Android development practices:

* **Language:** Kotlin (100%)
* **UI Framework:** Jetpack Compose (Declarative UI)
* **Navigation:** Navigation Compose (Single-activity architecture)
* **State Management:** Architecture Components (ViewModel, LiveData/Flow)
* **Design System:** Material Design 3 (Material 3)
* **Local Storage:** Room Database
* **Cloud Infrastructure:** Firebase Firestore Cloud Database
* **Network Client:** Retrofit 2 & OkHttp
* **Hardware API:** Android `SensorManager` (Accelerometer)

---

## 🚀 Setup & Installation Instructions

Follow these steps to get the development environment running locally:

1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/YOUR_USERNAME/StepBloom.git](https://github.com/YOUR_USERNAME/StepBloom.git)
    ```
2.  **Open in Android Studio**
    * Launch Android Studio (Ladybug or newer recommended).
    * Select `Open An Existing Project` and choose the cloned `StepBloom` directory.
3.  **Sync Dependencies**
    * Allow Android Studio to download required libraries. Click **Sync Project with Gradle Files** if it doesn't trigger automatically.
4.  **Network Configuration**
    * Ensure your testing emulator or physical Android deployment device has an active **Internet Connection** to let Firebase and Retrofit communicate properly.
5.  **Run the Application**
    * Select your device/emulator target and click the green **Run** button (`Shift + F10`).
6.  **Test Operations**
    * Navigate to the *Add Activity Screen*, save a mock entry, and verify data populates locally in the *Summary List* and syncs safely to the *Firestore* console dashboard.
