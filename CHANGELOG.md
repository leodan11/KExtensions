# Changelog

This file documents all significant changes, improvements, and fixes
in the **Kotlin Extensions** library. Each released version includes details
about new features, refactors, fixes, and breaking changes, so developers
can update their projects safely and efficiently.

---

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
- Bumped library version to 3.0.0
- Updated `@Since` annotations for new and renamed functions
