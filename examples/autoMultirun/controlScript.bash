#!/bin/bash
#--------------------------------------------------------------------------
#		MultiRun script 
# Date: 12/11/19
# Author: M Braun
#
# Description: This script demonstrates the ability to automatically create
# a large set of trajectory simulations using a simple script. The script
# stores the result files in the specified output folder. 
#
#--------------------------------------------------------------------------
# for loop to define number of runs
counter=1
counterMax=2

while [ $counter -le $counterMax ]
do
echo $counter
# Simulation setup
# Default setup for now

# Simulation launch
java -jar ./SIM.jar

# Rename File and move file to resultfolder
mv ./results.txt ./examples/autoMultirun/resultFolder/"result_${counter}"

((counter++))
done
