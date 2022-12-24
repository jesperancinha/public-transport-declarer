# Public Transport Declarer

Welcome to this program! Great to see you reading this document. This is a set of instructions on how to use the public
transport declarer.

## Motivation

This program was written because originally in The Netherlands, if we use the openbaarvervoer (public transportation),
we have to check in and check-out a lot of times and use several kinds of transportation.

The nation-widely used ov-chipkaart, allows to use the tram, NS trains, Arriva trains, the Metro in all major cities,
buses, inne-city trams, bikes etc.

If we use the OV to commute to work, the declaration of all multiple segments of transport becomes quite a hassle and
they normally involve very small costs per segment, that together result in very high costs.

For example in one day we can have something like:

1. Nieuwegein -> Utrecht = 2 euros
2. Utrecht -> Gouda = 9 euros
3. Gouda -> Utrecht = 9 euros
4. Utrecht -> Nieuwegein = 3 euros

However, the transportation costs vary per season, per time of day, per kind of day and sometimes just because of
unexpected changes.

So the costs are almost always unpredictable and this forces us to go through the PDF's provided by the OV company and
manually make some very tedious work of getting all these small costs together per day.
This is what this program attempts to do.

The first version is out but it is not super reliable yet. it's only an alpha-version.

However you can already run some trials, because it already makes the sums per day correcty.

What still needs to be done is:

1. Intelligent route calculation via travel nodes

## How to run

We can run the executable jar like this:

```shell
java -jar public-transport-declarer.jar -h
```

This will result in the help instructions:

```shell
Usage: public transport declarer [-hV] [-d=<destination>] [-g=<limit>]
                                 [-l=<notIncluded>] [-o=<origin>]
Makes an accurate calculation of the public transport usage to make work
related declarations
  -d, --destination=<destination>
                             The destination CSV file with the costs list per
                               datetime/value
  -g, -grenslimit=<limit>    Grens comes from dutch and it means limit. Daily
                               values under this will be ignored. Defaults to 10
  -h, --help                 Show this help message and exit.
  -l, --list=<notIncluded>   A list of all stations to ignore. Defaults to an
                               empty list
  -o, --origin=<origin>      The complete declaration file of your public
                               transportation provider
  -V, --version              Print version information and exit.
```

The input commands are several. Let's have a look at each of them.

## Origin

The origin file is a PDF file. In the future, this program will allow to receive different sorts of pdfs of different
companies. For now it only accepts the cost declaration of the [OV-Chipkaart](https://www.ov-chipkaart.nl) from The
Netherlands.
Just use the full path of the file. It can be an absolute path or a relative path.

## Destination

The destination file the resulting calculation of the whole travel cost for a whole day after going through all filters.
Here is an example:

```csv
Date, Cost
2022-12-01, 19.55
2022-12-06, 19.62
2022-12-07, 19.48
2022-12-08, 19.95
2022-12-09, 16.84
2022-12-12, 15.51
2022-12-13, 19.48
2022-12-14, 19.55
2022-12-15, 18.55
2022-12-16, 19.86
2022-12-19, 15.51
```

What this file represents is the sum of all costs per day considering all transport mediums after using the provided
filter

## List

With this optional value, we can filter stations out that we don't want to use in our calculation. It uses basic text
search and the filtering works by checking if the `station` string or the `company` string contain any of the comma
separated elements. If they do, then that travel segment will not be taken into account for the day calculation.

## Grenslimit

GrensLimit is just a limit to where we say that we can ignore travelling days. If the calculation of a travelling day
results under this limit, then this means practically that we didn't spend the usual minimum expected value to travel to
work. In this case, those days are ignored and not included in the resulting CSV file.
 