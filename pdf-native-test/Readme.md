# PDF Native Test

The public transport declarer project suffered a bit of a setback after realizing that the PDF readout with tika pdf
isn't entirely accepted by a native framework. This comes because tika pdf reader uses Java Desktop AWT classes that are
not easily convertible to native code.

This project is here to research which library can take its place.

## Commands

```shell
touch text.txt && echo "I'm a big big cat" >> text.txt && convert text.txt -page Letter a.pdf
```