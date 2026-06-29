# Claude Instructions for Public Transport Declarer

This project is a public transport declarer for the Netherlands, specifically designed to process OV-Chipkaart cost declarations.

## Project Expertise
- **Domain**: Dutch public transport declaration system.
- **Tech Stack**: Kotlin 2.3.0, Maven, GraalVM Native Image, OpenPDF, Picocli, Arrow, Apache POI.
- **Testing**: Kotest for unit and integration testing.

## Development Guidelines
- **Functional Programming**: Maintain the strict functional approach where Arrow is already used.
- **CLI**: Ensure Picocli annotations are correctly used for CLI arguments.
- **Native Image**: When adding features, ensure they are compatible with GraalVM Native Image (minimize reflection).
- **Modules**:
    - `public-transport-declarer`: Core domain.
    - `public-daily-transport-declarer`: Daily report generation.
    - `oncall-excel-interpreter`: Excel parsing for on-call duties.

## Verification
- Always run tests using `mvn test`.
- Prefer Kotest's `StringSpec` or `DescribeSpec` for new tests.
- Reference existing reports in `public-daily-transport-declarer/reports` for output format expectations.
