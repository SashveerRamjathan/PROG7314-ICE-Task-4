# 📱 Pulse News Android App

Welcome to **Pulse News** for Android!

A modern, comprehensive news application that keeps you connected to the world's pulse. Powered by NewsAPI, Pulse News delivers breaking headlines, in-depth search capabilities, and source exploration — all wrapped in a sleek, user-friendly interface designed for today's mobile-first news consumption.

## 📚 Table of Contents

- [✨ Features](#-features)
- [🎯 Why NewsAPI? A Strategic Choice](#-why-newsapi-a-strategic-choice)
- [📱 Mobile Relevance & User Value](#-mobile-relevance--user-value)
- [⚡ Getting Started](#-getting-started)
- [🛠️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🔑 API Configuration](#-api-configuration)
- [🌐 NewsAPI Integration](#-newsapi-integration)
- [🤓 Usage](#-usage)
- [👤 Author & Module Info](#-author--module-info)
- [👥 Contributing](#-contributing)
- [🙏 Acknowledgements](#-acknowledgements)

## ✨ Features

- **🔍 Advanced Article Search**: Search across 150,000+ global news sources with keyword filtering, date ranges, and sorting options
- **📈 Categorized Headlines**: Browse top headlines by category (Business, Technology, Sports, Health, Entertainment, Science, General)
- **🌐 Source Exploration**: Discover and explore news sources with detailed metadata and coverage information
- **📅 Flexible Date Filtering**: Filter articles by today, this week, this month, or custom date ranges
- **🎯 Smart Sorting**: Sort results by relevancy, popularity, or publication date for optimal content discovery
- **🔄 Real-time Updates**: Access the latest breaking news and fresh content as it's published
- **📱 Modern UI/UX**: Edge-to-edge design with smooth animations and intuitive navigation
- **🚀 Performance Optimized**: Efficient loading states, error handling, and responsive design for all Android devices
- **🛡️ Robust Error Handling**: Comprehensive error management with user-friendly feedback for various scenarios

## 🎯 Why NewsAPI? A Strategic Choice

### Global Coverage Meets Local Needs

NewsAPI was chosen for its unparalleled global reach with over 150,000 news sources across 55+ countries in 14 languages. For South African users and international audiences alike, this means access to both local perspectives (like News24, IOL, TimesLive) and global viewpoints (BBC, CNN, Reuters) — essential for understanding how local events impact the world and vice versa.

### Developer-First Architecture

Unlike traditional web scraping approaches that break frequently, NewsAPI provides:

- **Structured JSON responses** that eliminate parsing headaches
- **RESTful design** that scales effortlessly from development to production
- **Comprehensive filtering** (keywords, dates, sources, languages) for precise content curation
- **Rate limiting transparency** that helps manage API costs effectively

### Real-World Reliability

The API's 99.9% uptime and battle-tested infrastructure mean users can rely on fresh news content without service interruptions. This reliability is crucial for a news app where users expect immediate access to breaking stories and current events.

### Future-Proof Flexibility

NewsAPI's robust endpoint structure (`/everything`, `/top-headlines`, `/sources`) provides multiple content discovery paths:

- **Search-driven discovery** for research and specific interests
- **Category-based browsing** for casual news consumption
- **Source exploration** for media literacy and bias awareness

## 📱 Mobile Relevance & User Value

### Addressing Modern News Consumption Patterns

Mobile devices now account for 70%+ of news consumption globally, with users spending an average of 12 minutes per day reading news on smartphones. Pulse News directly addresses this shift by providing:

#### 🎯 Personalized Discovery
- Smart filtering allows users to focus on topics that matter to them (e.g., "renewable energy in South Africa")
- Source diversity helps users discover new perspectives beyond their usual news bubble
- Date-based exploration enables catching up on missed stories or researching trending topics

#### ⚡ Efficient Information Access
- Category-based headlines provide quick access to breaking news in specific sectors
- Source metadata enables informed media consumption by showing publication details and coverage areas

### Professional & Educational Value

The app serves multiple user personas:

#### 🎓 Students & Researchers
- Research current events for academic projects
- Analyze media coverage patterns across different sources
- Access historical articles within NewsAPI's 30-day window (free tier)

#### 📱 General News Consumers
- Stay informed without overwhelming information overload
- Discover new sources and broaden news consumption habits
- Customize news experience based on personal interests and time availability

## ⚡ Getting Started

Get Pulse News running on your Android device in just a few steps:

### 🧰 Prerequisites

- Android Studio (Flamingo or newer recommended)
- Kotlin >= 1.8
- Android SDK API 24+ (Android 7.0)
- NewsAPI Account for API key

### 🏗️ Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/SashveerRamjathan/PROG7314-ICE-Task-4.git
   cd PROG7314-ICE-Task-4
   ```

2. **Configure your API key:**
   - Create an account at [NewsAPI.org](https://newsapi.org)
   - Add your API key to `gradle.properties`:
   ```properties
   NEWS_API_KEY="your_api_key_here"
   ```

3. **Open in Android Studio:**
   - File > Open > Select the cloned folder
   - Sync project with Gradle files

4. **Build & Run:**
   - Click the ▶️ Run button in Android Studio
   - Install on your device or launch the emulator

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: Repository Pattern
- **Networking**: Retrofit + OkHttp + Gson
- **Design**: Material Design 3

## 📁 Project Structure

```
PROG7314-ICE-Task-4/
├── app/
│   ├── src/main/java/com/st10361554/prog7314_ice_task_4/
│   │   ├── adapters/          # RecyclerView adapters for articles and sources
│   │   ├── models/           # Data classes for API responses (Article, NewsSource, etc.)
│   │   ├── news_utils/       # Constants and utility classes (SearchConstants, NewsConstants, DateUtils)
│   │   ├── repository/       # NewsRepository for data layer abstraction
│   │   ├── retrofit/         # API service interfaces and Retrofit configuration
│   │   ├── MainActivity.kt   # Main navigation hub
│   │   ├── SearchActivity.kt # Advanced article search with filtering
│   │   ├── HeadlinesActivity.kt # Category-based top headlines
│   │   └── SourcesActivity.kt   # News source exploration
│   ├── src/main/res/
│   │   ├── layout/           # Activity and item layouts
│   │   ├── values/           # Strings, colors, themes
│   │   └── drawable/         # Icons and graphics
│   └── build.gradle
├── gradle.properties         # API key configuration
├── README.md
└── .gitignore
```

## 🔑 API Configuration

### Setting Up Your NewsAPI Key

1. Register for a free account at [NewsAPI.org](https://newsapi.org)
2. Copy your API key from the dashboard
3. Add to `gradle.properties`:
   ```properties
   NEWS_API_KEY="your_actual_api_key_here"
   ```

**Security Note**: The API key is automatically injected into BuildConfig and used via Authorization header

## 🌐 NewsAPI Integration

### Endpoints Used

#### 📰 Everything Search (`/v2/everything`)

```kotlin
// Search for articles with advanced filtering
val response = newsRepository.searchEverything(
    query = "artificial intelligence",
    fromDate = "2025-01-01", 
    toDate = "2025-08-12",
    sortBy = "relevancy"
)
```

- **Purpose**: Comprehensive article search across all sources
- **Features**: Keyword search, date filtering, sorting options
- **Use Case**: Research, specific topic exploration, content discovery

#### 🏆 Top Headlines (`/v2/top-headlines`)

```kotlin
// Get top headlines by category
val response = newsRepository.getTopHeadlinesByCategory("technology")
```

- **Purpose**: Latest breaking news and important stories
- **Features**: Category filtering, real-time updates
- **Use Case**: Daily news consumption, trending story discovery

#### 📡 Sources (`/v2/top-headlines/sources`)

```kotlin
// Get all available news sources
val response = newsRepository.getAllSources()
```

- **Purpose**: Explore available news publishers and their metadata
- **Features**: Source details, categories, geographic coverage
- **Use Case**: Media literacy, source diversity exploration

### Response Handling

The app processes structured JSON responses containing:

- **Articles**: Title, description, URL, publication date, source info
- **Metadata**: Total results, request status, error handling
- **Citations**: Source attribution and link-through capabilities

## 🤓 Usage

### 🔍 Advanced Search

1. Tap "Search" from the main menu
2. Enter keywords (supports Boolean operators: "AI AND machine learning")
3. Apply filters:
   - **Sort by**: Relevancy, Popularity, or Newest First
   - **Date range**: Today, This Week, This Month, or All Time
4. Browse results with full article previews and source information

### 📈 Browse Headlines

1. Tap "Headlines" from the main menu
2. Select category: Business, Technology, Sports, Health, Entertainment, Science, or General
3. Read headlines with automatic updates and fresh content

### 🌐 Explore Sources

1. Tap "Sources" from the main menu
2. Browse available publishers with detailed metadata
3. Discover new sources across different categories and regions

### 💡 Pro Tips

- Use specific keywords for better search results
- Combine date filters with category browsing for targeted news discovery
- Explore different sources to gain diverse perspectives on stories
- Bookmark interesting articles by following the provided URLs

## 👤 Author & Module Info

- **Name**: Sashveer Lakhan Ramjathan
- **Student Number**: ST10361554
- **Group**: 2
- **Module**: PROG7314
- **Assessment**: ICE Task 4

## 👥 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🙏 Acknowledgements

- **📰 NewsAPI** for providing comprehensive, reliable news data access
- **🏗️ Retrofit & OkHttp teams** for excellent networking libraries
- **🎨 Material Design team** for modern, accessible UI guidelines
- **🌍 Global news sources** for providing diverse, quality journalism
- **👨‍💻 Open source community** for the incredible tools and libraries that make development possible

---

*Stay connected to the world's pulse — built with ❤️ for the modern news consumer.*
