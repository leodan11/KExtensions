# Changelog

This file documents all significant changes, improvements, and fixes
in the **Kotlin Extensions** library. Each released version includes details
about new features, refactors, fixes, and breaking changes, so developers
can update their projects safely and efficiently.

---

## [3.0.1] - 2026-05-05

### ✨ Added
- New extensions to improve overall functionality:
  - `StringExtension`
  - `BitmapExtension`
  - `ContextExtension`
  - `FragmentExtension`
- New System Settings extensions
- Fluent extensions for PermissionManager:
  - Support for camera and location permissions
- DSL-style helpers in PermissionResult for improved usability

### 🔄 Changed
- Improved permissions API:
  - Added permissions module
  - Renamed permissions() to permissionManager()
- Unified bitmap handling:
  - Replaced createBitmap and toBitmapUnsafe with toBitmap(forceMeasure)
- Improved overall KDoc consistency across the API
- Updated dependencies in libs.versions.toml
- Maintained compatibility with API 21

### 🧱 Structure
- Modular reorganization for better separation of concerns (core, permissions, extensions, etc.)


## [3.0.0] - 2026-02-23

### ⚠️ Breaking Changes
- Renamed percentage extension functions:
  - `toPercentNormalized()` – expects 0.0..1.0
  - `toPercentFromValue()` – expects 0..100
- Reordered function parameters: mandatory first, optional last
- Reorganized package structure and modules:
  - Classes moved to `models`, `components`, `contracts`
  - Lifecycle utilities grouped into their respective modules
- Replaced string-based hashing API with `HashAlgorithm` sealed class
- Updated some Context, Activity, and Fragment extensions
- Backward compatibility **is not guaranteed**; users must update code accordingly

### ✨ Added
- HTML String extensions:
  - `String.toSpannedFromHtml()`
  - `String.toHtml(flags: Int)`
- Activity & Fragment core utility extensions
- `percentageOf()` helper for percentage calculations
- UI-friendly helper functions like `toDisplayPairList`

### ♻️ Refactored
- Reorganized extensions by module (String, Context, etc.)
- Improved and standardized KDoc documentation, Dokka-ready
- Applied consistent formatting and indentation across modules
- Removed duplicated hashing logic and improved error handling
- Reordered function parameters for clarity and consistency

### 🐛 Fixed
- Changed `MenuProvider` default state from `RESUMED` to `STARTED`

### 🔧 Maintenance
- Updated dependencies in `libs.versions.toml`
- Updated `@Since` annotations for new and renamed functions
