BlueBook Descent and Landing Analysis Toolkit

BlueBook Descent and Landing Analysis Tool 
15/09/2019

- 3 degree of freedom and 6 degree of freedom vehicle dynamics simulation, designed for various analyses between orbit and surface with respect to a variety of planetary bodies, with and without atmoshpere. 
- Customizable to various problems (very simple to very complex), e.g re-entry trajectories, landing trajectories, ascent trajectories, atmospheric flight, controlled landing, system failure analysis ... 
- Integrated controller setup to simulate simplified control strategies.
- Integrated gravity model (asymmetric gravity, rotating planet)
- Integrated atmosphere model (free molecular flow and continuum model)
- Integrated system error model to simulate partial subsystem failure(s) and analyse system robustness.# BlueBook Descent and Landing Analysis Toolkit

Note: 

The project is currently developed mainlay on MacOS, hence the compiled jar files are compiled for macOS. However, the system is from time to time tested on Windows and Linux and should run on both systems as well. Currently some bugs can be experienced when running the GUI from Windows, the SIM module should run without problems on all operating systems. Work is ongoing to eliminate bugs and run all modules smoothly on all operating systems. 

To run the VisualEngine the program has to be compiled on the respective operating system. 
To set up the project in Eclipse the natives for the LWJGL jars have to be set accordingly. 
Choose the project >> Properties >> Java Build Path >> select lwjgl.jar files >> double click natives > set path to Project/lib/natives/(select OS)

Currently natives for macOS and Windows are part of the project. For additional ones see:
http://legacy.lwjgl.org/download.php.html

