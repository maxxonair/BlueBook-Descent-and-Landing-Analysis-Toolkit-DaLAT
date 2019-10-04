BlueBook Descent and Landing Analysis Toolkit


![Github GUI](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/INP/INPUT_Documentation/GUIImage.png)

BlueBook Descent and Landing Analysis Tool 
27/09/2019

- 3 degree of freedom and 6 degree of freedom vehicle dynamics simulation, designed for various analyses between orbit and surface with respect to a variety of planetary bodies, with and without atmoshpere. 
- Customizable to various problems (very simple to very complex), e.g re-entry trajectories, landing trajectories, ascent trajectories, atmospheric flight, controlled landing, system failure analysis ... 
- Integrated controller setup to simulate simplified control strategies.
- Integrated gravity model (asymmetric gravity, rotating planet)
- Integrated atmosphere model (free molecular flow and continuum model)
- Integrated system error model to simulate partial subsystem failure(s) and analyse system robustness.# BlueBook Descent and Landing Analysis Toolkit

General Infos:

This project is divided in three core modules:

1) The Simulation module (SIM): The SIM.jar is the core simulation. >>java -jar SIM.jar will start the simulation. This setup allows to shift pre- and postprocessing to a different environment (e.g. Matlab, python a.s.o). The folder INP/ contains all relevant input files to start the simulation

![Github SIM](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/INP/INPUT_Documentation/FlowChart/flowchart.png)

2) The GUI module (GUI): The GUI BlueBook_DaLAT.jar allows to perform pre- and postprocessing and visualise results. 

3) The VisualEngine (VE). The VisualEngine is a game engine environment (based on the LWJGL 2 library) developed to visualise simulation results and allow real time simulations with user inputs. The VE is currently in development and has only a short real time flight demo implemented (FlyMeToTheMoon.jar). 

Note: 

The project is currently developed mainlay on MacOS, hence the compiled jar files are compiled for macOS. However, the system is from time to time tested on Windows and Linux and should run on both systems as well. Currently some bugs can be experienced when running the GUI from Windows, the SIM module should run without problems on all operating systems. Work is ongoing to eliminate bugs and run all modules smoothly on all operating systems. 

To run the VisualEngine the program has to be compiled on the respective operating system. 
To set up the project in Eclipse the natives for the LWJGL jars have to be set accordingly. 
Choose the project >> Properties >> Java Build Path >> select lwjgl.jar files >> double click natives > set path to Project/lib/natives/(select OS)

Currently natives for macOS and Windows are part of the project. For additional ones see:
http://legacy.lwjgl.org/download.php.html

