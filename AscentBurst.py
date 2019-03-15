# Burst file to run several trajectories in a loop 
import subprocess
import os
from shutil import copyfile 



start_cutoff_time=1 
# start engine cut-off time [s]
end_cutoff_time = 4 
# end engine cut-off

def Adjust_CutOff_Time(time):
	#Adjust Engine off time in Input
	alpha=1

def Run_Simulation():
	subprocess.call(['java', '-jar', 'SIM.jar'])

def Rename_and_Copy_Resultfiles(timestamp):
	dst ="burstres_"+str(timestamp)+".txt"
	src ="results.txt"
	os.rename(src, dst)
	src=dst
	dst="/burst_container"
	#copyfile(src, dst)

for i in range(start_cutoff_time,end_cutoff_time):
	# |1|
	Adjust_CutOff_Time(i)
	# |2|
	Run_Simulation()
	# |3|
	Rename_and_Copy_Resultfiles(i)
		 
