
# Burst file to run several trajectories in a loop 
import subprocess
import os
from shutil import copyfile 
import shutil
import time

ResultFolder = './burst_container/' 

start_cutoff_time = 5 
# start engine cut-off time [s]
end_cutoff_time = 450
# end engine cut-off

def Clear_ResultFolder():
		for the_file in os.listdir(ResultFolder):
			file_path = os.path.join(ResultFolder, the_file)
			try:
				if os.path.isfile(file_path):
					os.unlink(file_path)
			except Exception as e:
				print(e)

def Adjust_CutOff_Time(timestamp):
	os.remove("./INP/ErrorFile.inp")
	f = open("./INP/ErrorFile.inp","w+")
	f.write("0 %d 0.17" % timestamp)
	f.close()

def Run_Simulation():
	subprocess.check_call(['java', '-jar', 'SIM.jar'], shell=True)
        #subprocess.call(['java', '-jar', 'SIM.jar'])

def Rename_and_Copy_Resultfiles(timestamp):
	dst ="burstres_"+str(timestamp)+".txt"
	src ="results.txt"
	#os.rename(src, dst)
	#src=dst
	dst=ResultFolder+dst
	copyfile(src, dst)
#-----------------------------------------------------------------------
#                      Main Loop
#-----------------------------------------------------------------------
try:
   # Clear_ResultFolder()
   val=1
except:
    print("No files in result forlder")
for i in range(start_cutoff_time,end_cutoff_time):
    # |1|
    start = time.time()
    Adjust_CutOff_Time(i)
    # |2|
    Run_Simulation()
    # |3|
    Rename_and_Copy_Resultfiles(i)
    end = time.time()
    print("File ",str(i)," created",str((end-start)))
		 
