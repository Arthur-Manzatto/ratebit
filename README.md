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
*   **Advanced Review System:**
    *   Intuitive star selection (1 to 5 stars).
    *   **"Your Review"** section: Your personal evaluation always appears at the top.
    *   **Edit Mode:** Change your rating and comment at any time.
    *   **Review Filtering:** Filter comments by rating to see what others think.
    *   **Automatic Rating:** Real-time calculation of the game's global average.
*   **Favorites:** Save games with one click, synchronized across List, Profile, and Game pages.
*   **Customizable Profile:** Personalize your identity with a custom username and profile picture URL.

### 🛡️ For Administrators
*   **Content Control:** Exclusive access to add new games to the platform via a restricted interface.

---

## 🚀 Technologies Used

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **Database:** [Firebase Firestore](https://firebase.google.com/docs/firestore) (Real-time synchronization)
*   **Architecture:** Repository Pattern for clean data management.
*   **UI/UX:** 
    *   **Material Design 3** for a modern look.
    *   **ConstraintLayout** for responsive designs.
    *   **BottomSheetDialogs** for clean filters and settings.
    *   **ShapeableImageView** for polished circular avatars.
*   **Image Loading:** [Glide](https://github.com/bumptech/glide) with smart caching and error placeholders.

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
    *   Download `google-services.json` and place it in the `app/` folder.
    *   Enable **Firestore Database** and create collections: `users`, `games`, `reviews`.
4.  **Run:**
    Select an emulator or device and click **Run**.

---

## 📁 Project Structure

```text
com.example.ratebit
├── model/       # Data classes (Game, User, Rating)
├── repository/  # Firebase & Data logic
├── ui/          # Activities & View logic
│   └── adapter/ # RecyclerView Adapters (Games & Reviews)
└── res/         # Layouts, Drawables, Themes & Strings
```

---

## 👥 Contributors

Developed with ❤️ by:

| [<img src="https://github.com/Arthur-Manzatto.png" width="60"/><br /><sub><b>Arthur Manzatto</b></sub>](https://github.com/Arthur-Manzatto) | [<img src="https://github.com/EduardoScudeler07.png" width="60"/><br /><sub><b>Eduardo Scudeler</b></sub>](https://github.com/EduardoScudeler07) | [<img src="https://github.com/0utLunar.png" width="60"/><br /><sub><b>Lucas Cirino</b></sub>](https://github.com/0utLunar) |
| :---: | :---: | :---: |

---

## 📄 License

This project is under the MIT License.

---
⭐ **Ratebit Community**
