![NewDawn Banner](hero.png)
# NewDawn - Township Informal Job Opportunity App

> **Connecting communities to opportunities.**

![Android CI](https://github.com/Tsweleng10/newdawn-app/actions/workflows/build.yml/badge.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)
![Compose](https://img.shields.io/badge/Compose-Material%203-blue)
![API](https://img.shields.io/badge/API-Railway-success)
![Database](https://img.shields.io/badge/DB-PostgreSQL-blue)

---

## Table of Contents

- [What is NewDawn?](#-what-is-newdawn)
- [How It Works (User Flow)](#-how-it-works-user-flow)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Tech Stack](#-tech-stack)
- [API Reference](#-api-reference)
- [Getting Started (Local Setup)](#-getting-started-local-setup)
- [GitHub Workflow & CI/CD](#-github-workflow--cicd)
- [Demo Video](#-demo-video)
- [Team](#-team)
- [AI Usage Disclosure](#-ai-usage-disclosure)

---

## What is NewDawn?

**NewDawn** is an Android application designed to address township unemployment by connecting people who need short-term, informal work done with skilled workers in their local community.

**The Problem:** In many South African communities, there is a mismatch between people who need help (e.g., gardening, painting, cleaning) and people who have the skills but lack a platform to find these jobs. Existing platforms like Cirvo and VukaWork focus mostly on formal employment, leaving a gap for cash-based, community-level work.

**The Solution:** NewDawn provides a hyper-local, peer-to-peer marketplace. It allows any user to post a job or find work. The app is designed to be lightweight, accessible, and supports offline functionality for areas with poor internet connectivity.

---

## 🔄 How It Works (User Flow)

NewDawn features a dual-role system. You are not locked into being just a "worker" or a "poster" — you can be both.

1. **Onboard:** The user registers or logs in securely.
2. **The Dashboard (Home):** The user is greeted and presented with two main options: **FIND WORK** or **POST A JOB**, alongside recommended local jobs.
3. **Posting a Job:** A user fills out a quick form (Title, Category, Description, Location, Budget). *(If offline, the job is saved to the phone and synced later.)*
4. **Finding Work:** Workers can browse all open jobs, search by keyword, or filter by category.
5. **Submitting an Offer:** When a worker finds a job, they can submit an offer with their proposed price, a short message, and their availability.
6. **Accepting an Offer:** The job poster views all received offers, compares them, and selects a worker.
7. **Job Progress:** The job status moves from `OPEN` to `ACTIVE`.
8. **Completion:** The poster marks the job as completed.
9. **Settings:** Users can change their app language (English, isiZulu, Afrikaans) or update their password.

---

## Key Features

| Feature | Status | Description |
|---|---|---|
| **Secure Auth** | ✅ | Register/login with bcrypt-hashed passwords and JWT sessions. |
| **Multi-Role User Profiles** | ✅ | Editable profiles with name, email, and location. |
| **Job Management** | ✅ | Create, browse, search, and filter local job listings. |
| **Offer System** | ✅ | Workers submit offers; posters compare and accept them. |
| **Offline Mode & Sync** | ✅ | Uses **Room** & **WorkManager** to save jobs offline and sync to the API when internet returns. |
| **Multi-Language Support** | ✅ | Full UI translation for **English**, **isiZulu**, and **Afrikaans**. |
| **Modern UI** | ✅ | Custom light theme with a cream pattern background, custom app icon, and smooth Compose navigation. |
| **Robust Error Handling** | ✅ | Prevents crashes on bad input, handles API timeouts gracefully, and provides friendly empty states. |

---

## 🏗 System Architecture

The application follows the **MVVM (Model-View-ViewModel)** architecture and uses the Repository pattern. It is split into two distinct repositories (Frontend and Backend).

```text
┌───────────────────────────────────────────────────────┐
│                    Android App                        │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────┐   │
│  │  Compose UI  │→ │  ViewModels  │→ │ Repository │   │
│  └──────────────┘  └──────────────┘  └─────┬──────┘   │
└────────────────────────────────────────────┼──────────┘
                                             │
                                    Retrofit HTTP + JWT
                                             │
┌────────────────────────────────────────────▼──────────┐
│                  Node.js REST API                     │
│  Routes → Middleware (Auth) → Controllers → pg (SQL)  │
└────────────────────────────────────────────┬──────────┘
                                             │
                                    ┌────────▼────────┐
                                    │   PostgreSQL    │
                                    │   (Neon Cloud)  │
                                    └─────────────────┘
```

- The Android app never communicates with the database directly.
- Every request is authenticated via a JWT token in the `Authorization` header.

---

## Tech Stack

### Android Frontend

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM + StateFlow
- **Networking:** Retrofit 2 + Gson
- **Local Storage:** DataStore (JWT token), Room Database (offline job caching)
- **Background Tasks:** WorkManager (syncing offline jobs)
- **Image Loading:** Coil

### Backend & DevOps

- **Backend Runtime:** Node.js 20 + Express.js
- **Database:** PostgreSQL (hosted on Neon Serverless)
- **Authentication:** JWT (JSON Web Tokens) + bcrypt (10 salt rounds)
- **Hosting:** Railway
- **CI/CD:** GitHub Actions (automated builds on every push)

---

## API Reference

**Live Base URL:** `https://newdawn-api-production.up.railway.app/`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT |
| `GET` | `/api/auth/me` | Get current user profile |
| `PUT` | `/api/auth/settings` | Update language/notifications |
| `PUT` | `/api/auth/change-password` | Change user password |
| `GET` | `/api/jobs` | List all open jobs (supports search & filter) |
| `POST` | `/api/jobs` | Create a new job listing |
| `GET` | `/api/jobs/my` | List jobs posted by the current user |
| `POST` | `/api/offers/job/{jobId}` | Submit an offer on a specific job |
| `GET` | `/api/offers/job/{jobId}` | View all offers for a specific job |
| `PUT` | `/api/offers/{id}/accept` | Accept an offer |

---

## Getting Started (Local Setup)

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- A physical Android device (API 24+) or an emulator

### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/Tsweleng10/newdawn-app.git
   ```

2. Open the project in Android Studio.
3. Wait for Gradle to sync all dependencies.
4. Connect your Android device and click **Run ▶**.

> **Note:** The app is pre-configured to connect to the live Railway API. No local backend setup is required to test the app.

---

## GitHub Workflow & CI/CD

We used a strict **Feature Branch + Pull Request** workflow to ensure code quality and prevent conflicts.

- **Branching:** Each major feature was built on its own branch (e.g., `feature/language-support`, `Offline-Mode`).
- **Pull Requests:** Features were merged into `main` via Pull Requests, requiring code review and conflict resolution.
- **GitHub Actions:** As configured in `.github/workflows/build.yml`, every push to `main` triggers an automated CI pipeline. It sets up JDK 17, compiles the Kotlin code, and runs unit tests to ensure the app builds on a clean machine.

---

## Demo Video

▶️ [Watch the full demonstration on YouTube](https://youtu.be/sS6t60LT4C4)

The demonstration covers:

- User registration (with bcrypt proof in the database)
- Dashboard and navigation
- Posting a job (including the Offline Mode fallback)
- Browsing and submitting an offer
- Accepting an offer and multi-language switching

---

## 👥 Team

| Name | Student Number | Role |
|---|---|---|
| Joshua Tsweleng | st10451745 | Lead Dev — Backend API, Authentication, Offers, Settings, Integration |
| Fortune Lemekwana | st10450241 | Offline Mode — Room Database & WorkManager Syncing |
| Phuti Magwai | st10452585 | UI & Navigation — Jobs browsing, Home layout |

---

## 🤖 AI Usage Disclosure

In accordance with the OPSC6312 assessment requirements:

- **AI Tools Used:** DeepSeek (architecture, debugging), ChatGPT (README formatting).
- **Usage:** AI was used to generate initial boilerplate code for the Retrofit client and Express routes. All AI suggestions were manually reviewed, tested against the live database, and adjusted to fit the specific requirements of the app (e.g., correcting `full_name` vs `fullName` in JSON serialization).
- **Logging:** The app uses `Log.d()` and `Log.e()` extensively throughout ViewModels to track API requests, successes, and failures during development.

