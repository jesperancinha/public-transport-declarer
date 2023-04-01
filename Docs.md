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

So the costs are almost always unpredictable and this forces us to go through the PDFs provided by the OV company and
manually make some very tedious work of getting all these small costs together per day.
This is what this program attempts to do.

The first version is out, but it is not super reliable yet. it's only an alpha-version.

However, you can already run some trials, because it already makes the sums per day correctly.

I recommend to always double-check the generated public transportation declaration.

## How to run

We can run the executable jar like this:

```shell
java -jar public-transport-declarer.jar -h
```

This will result in the help instructions:

```shell
Usage: public transport declarer [-hV] [-d=<destination>] [-g=<limit>]
                                 [-l=<notIncluded>] [-o=<origin>]
                                 [-r=<routeFile>]
Makes an accurate calculation of the public transport usage to make work
related declarations
  -d, --destination=<destination>
                             The destination CSV file with the costs list per
                               datetime/value
  -g, --grenslimit=<limit>   Grens comes from dutch and it means limit. Daily
                               values under this will be ignored. Defaults to 10
  -h, --help                 Show this help message and exit.
  -l, --list=<notIncluded>   A list of all stations to ignore. Defaults to an
                               empty list
  -o, --origin=<origin>      The complete declaration file of your public tr
                               ansportation provider
  -r, -routes=<routeFile>    This file contains an exclusive filter where only
                               the listed routes are valid and considered for
                               calculation:
                             The route file can contain two types of formats:
                                 On considering only routes:
                                     station -> station -> station ...
                                     Example: Nieuwegein -> Gouda
                                 On considering the same for one specific day:
                                     local date -> station -> station ...
                                     Example: 2022/12/08 -> Nieuwegein ->
                               Amstelveen

  -V, --version              Print version information and exit.
```

The input commands are several. Let's have a look at each of them.

## Origin(-o or --origin)

The origin file is a PDF file. In the future, this program will allow to receive different sorts of pdfs of different
companies. For now it only accepts the cost declaration of the [OV-Chipkaart](https://www.ov-chipkaart.nl) from The
Netherlands.
Just use the full path of the file. It can be an absolute path or a relative path.

## Destination (-d or --destination)

The destination file the resulting calculation of the whole travel cost for a whole day after going through all filters.
Here is an example:

```csv
Date, Description, Cost
2022-12-01, Office work, 19.55
2022-12-06, Office work, 19.62
2022-12-07, Office work, 19.48
2022-12-08, Kotlin Free Training Day, 19.95
2022-12-09, Office work, 16.84
2022-12-12, Office work, 15.51
2022-12-13, Office work, 19.48
2022-12-14, Office work, 19.55
2022-12-15, Office work, 18.55
2022-12-16, Office work, 19.86
2022-12-19, Office work, 15.51
```

What this file represents is the sum of all costs per day considering all transport mediums after using the provided
filter.

It can easily be turned into this:

| Date       | Description              | Cost  |
|------------|--------------------------|-------|
| 2022-12-01 | Office work              | 19.55 | 
| 2022-12-06 | Office work              | 19.62 | 
| 2022-12-07 | Office work              | 19.48 | 
| 2022-12-08 | Kotlin Free Training Day | 19.95 |
| 2022-12-09 | Office work              | 16.84 | 
| 2022-12-12 | Office work              | 15.51 | 
| 2022-12-13 | Office work              | 19.48 | 
| 2022-12-14 | Office work              | 19.55 | 
| 2022-12-15 | Office work              | 18.55 | 
| 2022-12-16 | Office work              | 19.86 | 
| 2022-12-19 | Office work              | 15.51 | 

## List (-l or --list)

With this optional value, we can filter stations out that we don't want to use in our calculation. It uses basic text
search and the filtering works by checking if the `station` string or the `company` string contain any of the comma
separated elements. If they do, then that travel segment will not be taken into account for the day calculation.

## Grenslimit (-g or --grenslimit)

GrensLimit is just a limit to where we say that we can ignore travelling days. If the calculation of a travelling day
results under this limit, then this means practically that we didn't spend the usual minimum expected value to travel to
work. In this case, those days are ignored and not included in the resulting CSV file.

## Routes (-r or --routes)

In order to provide a good filtering over the routes that are actually billable, we mostly need to consider 2 types of
routes:

1. A permanent one - This is route we usually take to work. No matter how many transport mediums we use, this route
   stays almost 100% of the time the same. We start at our address, and we end up at our work address. We also take into
   account the return route.
2. A route used for a certain activity sponsored by our company - This could be a course, a conference, a talk, a
   meetup or anything that your company agrees to be a part of your company interests.

Routes are defined by:

```text
Optional date with format yyyy-MM-dd > origin station > destination station > comment
```

We want to define this as easily as possible. With this switch, we specify a filename that contains route definitions.
Let's look at one example:

```text
2022-12-08 > Nieuwegein > Amstel > Kotlin Free Training Day
Nieuwegein > Gouda > Office work
Utrecht > Gouda > Office work
Antonius > Gouda > Office work
```

In this case we are defining 4 routes.

The first route has a date as the first element. This means that this route will
only be considered it it matches for this day and is included in the general declaration.

The second route will make sure to select all routes that are detected to run between Nieuwegein and Gouda.

In the same way the following routes define routes between Utrecht and Gouda and Antonius and Gouda.

When downloading the generic declarations provided by the OV-Chipkaart (as an example), stations may have different
names. The names we declare in this file are actually just strings that are matched by `contains`.

It is also important to be aware that this filter is exclusive. This means if this switch is used, only the routes
defined in it will be considered when calculating your public transportation declaration. However, this is a much better
way to make sure that you get all the necessary routes in an accurate way.

## Run examples

```shell
java -jar target/public-transport-declarer.jar -o declaratieoverzicht_22122022110627.pdf -r routes.txt -d report-test.csv
```


## Installing the native executable

The current version is compatible with general Linux distributions and was tested on:

`Linux 5.15.0-56-generic #62-Ubuntu SMP Tue Nov 22 19:54:14 UTC 2022 x86_64 x86_64 x86_64 GNU/Linux`

Since it is using a generic GraalVM plugin, it is probaly already compatible with other OS's like Windows or MAC-OS, but I cannot guarantee that yet because I still wasn't able to get around to test this on those systems.

To install locally in a Linux distribution please have a first look at the installation script located in the [Makefile](./Makefile) located at the root of this project.

```makefile
install-locally:
	cd public-transport-declarer && make install-locally
```

This will callL

```makefile
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	mvn clean install -Pnative
	cp target/public-transport-declarer .
create-native-no-fallback: create-native
install-locally: create-native-no-fallback
	sudo cp public-transport-declarer /usr/local/bin
```

Which in general terms, it means that it will make a jar build, a native build and finally make a copy of the resulting executable to your machine's `/usr/local/bin`.
This last step is manual, and you'll need to use `sudo` for it.
This way we take advantage of the system PATH to access the program.
This is the reason you should first look at this script and make sure that it's ok to copy this file into that folder. If not, then just run:

```shell
make create-native
```

After this step you'll get the executable and then better decide where in the System PATH is a better location for it.

## More Help

```shell
java -jar target/public-transport-declarer.jar --help
java -jar target/public-transport-declarer.jar -h
```

###### Version

```shell
java -jar target/public-transport-declarer.jar -V
java -jar target/public-transport-declarer.jar --version
```

###### Example 1

```shell
java -jar target/public-transport-declarer.jar -o declaratieoverzicht_24122022170148.pdf -g 10 -d report-test-01.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport
java -jar target/public-transport-declarer.jar -o declaratieoverzicht_22122022110627.pdf -g 10 -d report-test-02.csv -l Arnhem,Velp,Schipol,Airport
java -jar target/public-transport-declarer.jar -o declaratieoverzicht_28022023190418.pdf -g 10 -d report-test-01.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche
java -jar target/public-transport-declarer.jar -o src/test/resources/transacties_31032023194834.csv -g 10 -d report-test-01.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche
java -jar target/public-transport-declarer.jar -o src/test/resources/transacties_31032023194834.csv -g 10 -d report-test-01.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche -r routes.txt
```
