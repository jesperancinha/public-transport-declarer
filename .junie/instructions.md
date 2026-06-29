# Junie Instructions for Public Transport Declarer

This project is a Kotlin-based tool for processing Dutch public transport (OV-Chipkaart) records.

## Project Expertise
- **Domain**: Dutch public transport declaration system.
- **Tech Stack**: Kotlin 2.3.0, Maven, GraalVM Native Image, OpenPDF, Picocli, Arrow, Apache POI.
- **Testing**: Kotest for unit and integration testing.

## Junie-Specific Guidelines
- **Development**: When adding features, ensure they are compatible with GraalVM Native Image (minimize reflection).
- **Maven**: Use the provided `Makefile` or Maven commands for building. Note the multiple modules:
    - `public-transport-declarer`: Core domain.
    - `public-daily-transport-declarer`: Daily report generation.
    - `oncall-excel-interpreter`: Excel parsing for on-call duties.
- **Testing**: Always run tests using `run_test` or `mvn test`. Prefer Kotest's `StringSpec` or `DescribeSpec` if creating new tests.
- **Verification**: Use the existing reports in `public-daily-transport-declarer/reports` as reference for expected output formats.

## Key Constraints
- Maintain the strict functional approach where Arrow is already used.
- Ensure Picocli annotations are correctly used for CLI arguments.
- Keep `TechStack.md` and `Readme.md` updated with any new AI-related "skills".
- When updating AI instructions, maintain consistency across `.github/copilot-instructions.md`, `.cursorrules`, `.junie/instructions.md`, `.jetbrains/ai-assistant.md`, and `CLAUDE.md`.
