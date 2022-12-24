# Public Transport Declarer Notes

```kotlin
println("Contents of the PDF :$handler")
println("Metadata of the PDF:")
val metadataNames = metadata.names()
for (name in metadataNames) {
    println(name + " : " + metadata[name])
}
```