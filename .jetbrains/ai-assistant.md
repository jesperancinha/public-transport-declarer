# JetBrains AI Assistant Instructions for Public Transport Declarer

This project is a Kotlin-based tool for processing Dutch public transport (OV-Chipkaart) records.

## Project Context
- **Language**: Kotlin 2.3.0
- **Build System**: Maven
- **Core Technologies**: GraalVM (native-image), OpenPDF, Picocli, Arrow, Apache POI.
- **Testing**: Kotest, JUnit 5.

## AI Assistant Guidelines
- Follow idiomatic Kotlin patterns.
- Use Picocli for CLI command definitions.
- Use Arrow for functional programming patterns where applicable.
- Ensure compatibility with GraalVM native-image.
- Maintain consistency with the existing modular structure:
    - `public-transport-declarer`: Core domain.
    - `public-daily-transport-declarer`: Daily report generation.
    - `oncall-excel-interpreter`: Excel parsing for on-call duties.

## Domain Knowledge
- **OV-Chipkaart**: The Dutch public transport card system.
- **GrensLimit**: A threshold (usually 10 EUR) below which daily travel costs are ignored.
- **Route Filtering**: Exclusive filters using `routes.txt` to include only specific journeys.
