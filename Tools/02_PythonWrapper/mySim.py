import subprocess
import os

class mySim:

    def setInitState(long, lat, radius, velocity, fpa, azimuth):
        ber = long

    def myRun(self):
        cwd = os.getcwd() #current directory
        # Set working directory to .jar directory to make absolute file paths in jar working
        os.chdir('../../')
        # Run process
        subprocess.call(['java', '-jar', './SIM.jar'])
        os.chdir(cwd)
