Modern Android Pokedex

This project is a modern Android application built to demonstrate best practices in application architecture, a 100% Kotlin and Jetpack Compose tech stack, and a focus on clean, scalable, and testable code. It serves as a technical showcase adhering to MVVM, SOLID principles, and Material Design 3.

The application fetches data from the public pokeapi.co and displays it in a Pokedex format.

Features

Pokemon List: Displays a paginated list of the first 100 Pokemon, loaded from the network and cached locally for offline access.

Paging 3: Implements the Jetpack Paging 3 library with a RemoteMediator for robust network-and-database pagination.

Offline Caching: Uses Room DB to cache the Pokemon list, providing a seamless offline-first experience.

Pokemon Details: A dedicated screen showing detailed information about a selected Pokemon, including its image, types, abilities, and base stats.

Reactive Search: A real-time search bar on the home screen that filters the locally cached list of Pokemon.

Material Design 3: A sleek, responsive UI built entirely in Jetpack Compose, fully compliant with Material Design 3 principles (including light and dark themes).

Architecture

This application follows a modern Android architecture, heavily influenced by Clean Architecture principles, with a clear separation of concerns.

MVVM (Model-View-ViewModel): The presentation layer uses the MVVM pattern.

Views (Composables): Reactively observe state from ViewModels using StateFlow and PagingData.

ViewModels: Contain no references to Android framework UI components. They fetch and manage UI-related state, delegating business logic to the repository.

Repository Pattern: The PokemonRepository acts as a single source of truth, abstracting data sources (network and local database) from the rest of the app.

SOLID Principles: Code is designed with SOLID principles for maintainability and testability.

Dependency Injection: Uses Hilt to manage dependencies, simplifying the graph and making components easily testable.

Asynchronicity: Uses Kotlin Coroutines and Flows for all asynchronous operations, ensuring a non-blocking and responsive main thread.

Tech Stack & Libraries

This project demonstrates expertise in the following technologies:

Core Language: Kotlin

Asynchronous: Kotlin Coroutines, Kotlin Flow

Architecture: MVVM, SOLID, Repository Pattern

Dependency Injection: Hilt

UI: Jetpack Compose, Material Design 3 (MD3)

Navigation: Jetpack Navigation for Compose

Networking: Retrofit, OkHttp

JSON Parsing: Kotlinx.serialization

Persistence (Database): Room

Pagination: Jetpack Paging 3 (with RemoteMediator)

Image Loading: Coil

How to Build

To build and run the project, you need:

Android Studio (latest stable version recommended)

An active internet connection (to fetch data on the first run)

Simply clone the repository, open it in Android Studio, and let Gradle sync the dependencies. You can then run the app on an emulator or a physical device.
