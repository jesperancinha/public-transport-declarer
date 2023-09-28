#!/usr/bin/env bash

fileName=$(ls -l transact*.csv | tail -n1 | cut -d" " -f10)
if [[ $fileName == "" ]]; then
  fileName=$(ls -l transact*.csv | tail -n1 | cut -d" " -f9)
fi
echo $fileName
java -jar target/public-transport-declarer.jar -o $fileName -g 10 -d report.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche -r routes.txt
