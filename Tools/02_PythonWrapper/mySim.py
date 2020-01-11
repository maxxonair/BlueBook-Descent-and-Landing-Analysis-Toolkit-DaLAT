import subprocess
import os

class mySim:

    def setInitialState(long, lat, radius, velocity, fpa, azimuth):
        ber = long

    def getInitialState()
        initialState = long
        return initialState

    def run(self):
        cwd = os.getcwd() #current directory
        # Set working directory to .jar directory to make absolute file paths in jar working
        os.chdir('../../')
        # Run process
        subprocess.call(['java', '-jar', './SIM.jar'])
        os.chdir(cwd)
