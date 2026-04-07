# 🕹️ Ratebit - Game Rating Community

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Firestore-FFCA28?logo=firebase&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-Electric_Eel+-3DDC84?logo=android-studio&logoColor=white)

**Ratebit** is a social platform for game enthusiasts where users can discover new titles, manage their favorite games, and share opinions through a robust and intuitive rating system.

---

## ✨ Features

### 👤 For Users
*   **Authentication:** Secure login and signup system integrated with Firestore.
*   **Game Feed:** Real-time game list with search by name.
*   **Smart Filters:** Filter by category and minimum rating using a 5-star system.
*   **Game Page:** Complete details including developer, release date, and description.
*   **Review System:**
    *   Intuitive star rating (1 to 5).
    *   Highlighted "Your Review" section at the top of the list.
    *   Editing existing reviews.
    *   Automatic calculation of the game's global average rating.
*   **Favorites:** Save your preferred games with one click (synchronized across all screens).
*   **Customizable Profile:** Change username and profile picture via URL.

### 🛡️ For Administrators
*   **Content Management:** Exclusive access to add new games to the platform.

---

## 🚀 Technologies Used

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **Database:** [Firebase Firestore](https://firebase.google.com/docs/firestore) (Real-time updates)
*   **Architecture:** Repository Pattern for data abstraction.
*   **UI/UX:** 
    *   Material Design 3
    *   ConstraintLayout for complex layouts.
    *   BottomSheetDialogs for filters and menus.
    *   ShapeableImageView for circular avatars.
*   **Image Loading:** [Glide](https://github.com/bumptech/glide) (with caching and error handling).

---

## 🛠️ How to Run the Project

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/ratebit.git
    ```
2.  **Open in Android Studio:**
    Import the project and wait for Gradle synchronization.
3.  **Firebase Configuration:**
    *   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add an Android app with the package `com.example.ratebit`.
    *   Download the `google-services.json` file and place it in the `app/` folder.
    *   Enable **Firestore Database** and create the collections: `users`, `games`, `reviews`.
4.  **Run:**
    Select an emulator or physical device and click **Run**.

---

## 📁 Main Folder Structure

```text
com.example.ratebit
├── model/       # Data classes (Game, User, Rating)
├── repository/  # Firebase communication logic
├── ui/          # Activities and UI logic
│   └── adapter/ # Adapters for RecyclerView (Games and Reviews)
└── res/         # Resources (Layouts, Drawables, Themes)
```

---

## 🤝 Contributing

Contributions are welcome! Feel free to open an **Issue** or submit a **Pull Request**.

1.  Fork the project.
2.  Create a Feature Branch (`git checkout -b feature/NewFeature`).
3.  Commit your changes (`git commit -m 'Add: New Feature'`).
4.  Push to the Branch (`git push origin feature/NewFeature`).
5.  Open a Pull Request.

---

## 📄 License

This project is under the MIT License.

---
⭐ **Developed with ❤️ by the Ratebit community.**
