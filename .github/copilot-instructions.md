# GitHub Copilot Instructions for Public Transport Declarer

This project is a public transport declarer for the Netherlands, specifically designed to process OV-Chipkaart cost declarations.

## Project Context
- **Language**: Kotlin
- **Build System**: Maven
- **Core Technologies**: GraalVM (native-image), OpenPDF, Picocli, Arrow, Apache POI.
- **Testing**: Kotest, JUnit 5.
- **Goal**: Parse PDF/CSV cost reports from OV-Chipkaart and generate summarized reports (CSV) for work-related declarations.

## Coding Standards
- Follow idiomatic Kotlin patterns.
- Use Picocli for CLI command definitions.
- Use Arrow for functional programming patterns where applicable.
- Ensure compatibility with GraalVM native-image. Avoid excessive reflection or configure it in `reflect-config.json` if needed.
- Maintain consistency with the existing modular structure:
    - `public-transport-declarer`: Core domain and domain-specific logic.
    - `public-daily-transport-declarer`: Logic for processing daily reports.
    - `oncall-excel-interpreter`: Logic for interpreting on-call schedules in Excel.

## Domain Knowledge
- **OV-Chipkaart**: The Dutch public transport card system.
- **GrensLimit**: A threshold (usually 10 EUR) below which daily travel costs are ignored.
- **Check-in/Check-out**: Travel segments are defined by these events.
- **Route Filtering**: Exclusive filters using `routes.txt` to include only specific journeys.

## Testing Guidelines
- Use Kotest for descriptive tests.
- Ensure all new features are covered by unit tests.
- Use `DailyPdfParserMatchTest` as a reference for parsing logic tests.
