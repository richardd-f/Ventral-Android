# Ventral Android

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Jetpack Compose](https://img.shields.io/badge/Compose-Material3-green)
![Hilt](https://img.shields.io/badge/Dagger--Hilt-2.51.1-orange)
![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-red)
![Cloudinary](https://img.shields.io/badge/Cloudinary-3.0.2-blueviolet)

Ventral Android is the official mobile client for the Ventral ecosystem, built with a modern, reactive stack. It provides a seamless user experience for managing events and data, interfacing directly with the **Ventral API**.

Designed using a feature-based modular architecture, this application focuses on performance, state management, and a clean UI/UX using Jetpack Compose.

---

## Key Features
* **Modern UI:** Declarative UI built with Jetpack Compose and Material Design 3.
* **Dependency Injection:** Scalable architecture powered by Dagger-Hilt.
* **Networking:** Type-safe REST API consumption using Retrofit & OkHttp.
* **Image Management:** Cloudinary integration for image uploads and Coil for optimized loading.
* **Local Storage:** Data persistence via Jetpack DataStore.
* **Reactive Flow:** Real-time data handling using Kotlin Coroutines and Flow.

---

## Tech Stack
* **UI:** Jetpack Compose (Material 3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **DI:** Dagger-Hilt
* **Network:** Retrofit 2 & OkHttp 4
* **Async:** Kotlin Coroutines
* **Storage:** Jetpack DataStore (Preferences)
* **Image Hosting:** Cloudinary SDK

---

## Installation

1. **Clone the repository**
    ```bash
    git clone https://github.com/richardd-f/Ventral-Android.git
    ```

2. **Open in Android Studio**
    * Launch Android Studio (Ladybug or newer).
    * Select **Open** and choose the `Ventral-Android` folder.

3. **Sync Project**
    * Wait for the Gradle sync to finish. The IDE will automatically download all dependencies listed in `libs.versions.toml` or `build.gradle`.

4. **Environment Setup**
    * Ensure your [Ventral API](https://github.com/richardd-f/Ventral-API) is running.
    * If testing on a physical device, update API `BASE_URL` to your computer's local IP address.
    * You can update API Base url in `di/AppModule`

5. **Run the App**
    * Select your target device (Emulator or Physical).
    * Click the **Run** icon (Green Play button).

---
