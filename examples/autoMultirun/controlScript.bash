#!/bin/bash

# for loop to define number of runs
counter=1
counterMax=2

while [ $counter -le $counterMax ]
do
echo $counter
# Simulation setup

# Simulation launch
java -jar ../../SIM.jar

# Rename File 
mv ../../results.txt ./resultFolder/result_$counter

# Move to result folder 
#mv /result.txt /resultFolder/


((counter++))
done
