![NewDawn Banner](hero.png)
# NewDawn - Township Informal Job Opportunity App

## Live API
https://newdawn-api-production.up.railway.app/

##  Features

| Feature | Status | Description |
|---|---|---|
| Secure Authentication | ✅ | Register/login with bcrypt-hashed passwords and JWT sessions |
| User Profiles | ✅ | Editable profile with name, email, location |
| Post a Job | ✅ | Create listings with title, category, description, location, budget |
| Browse Jobs | ✅ | Search and filter by keyword, category, location |
| Submit Offers | ✅ | Workers submit offers with price, message, availability |
| View & Accept Offers | ✅ | Posters compare offers and select a worker |
| Job Status Tracking | ✅ | Jobs move OPEN → ACTIVE → COMPLETED |
| Settings Menu | ✅ | Language, notifications, and password change |
| **Offline Mode & Sync** | ✅ | **Uses Room & WorkManager to save jobs offline and sync when connected** |
| **Multi-Language Support**| ✅ | **Full translation support for English, isiZulu, and Afrikaans** |
| **Modern UI** | ✅ | **Custom light theme, cream pattern background, and app icon** |

## Tech Stack
**Android**
- Kotlin + Jetpack Compose (Material 3)
- MVVM architecture with ViewModels + StateFlow
- Retrofit 2 + Gson (networking)
- DataStore Preferences (JWT token storage)
- **Room Database (Offline caching)**
- **WorkManager (Background syncing)**
- Coil (image loading)
**Backend**
- Node.js + Express + PostgreSQL (Neon)
**Hosting**
- Railway
**Authentication**
-JWT + bcrypt

## Demo Video
[Link to YouTube video]

## Team
- Lead: Joshua Tsweleng (st10451745)
- Fortune Lemekwane 
- Phuti Magwai (St10452585)
