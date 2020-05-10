BlueBook Descent and Landing Analysis Toolkit


![Github GUI](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/develop/documentation/INPUT_Documentation/GUIImage10.png)

BlueBook Descent and Landing Analysis Tool 
27/09/2019

- 3 degree of freedom and 6 degree of freedom vehicle dynamics simulation, designed for various analyses between orbit and surface with respect to a variety of planetary bodies, with and without atmoshpere. 
- Customizable to various problems (very simple to very complex), e.g re-entry trajectories, landing trajectories, ascent trajectories, atmospheric flight, controlled landing, system failure analysis, subsystem performance analysis ... 
- Integrated controller setup to simulate simplified control strategies, as well as the option to include external code.
- Integrated gravity model based on spherical harmonics (asymmetric gravity, rotating planet)
- Integrated atmosphere model (free molecular flow and continuum model)
- Integrated system error model to simulate uncertainties, as well as partial subsystem failure(s), allows analyse system robustness.# BlueBook Descent and Landing Analysis Toolkit

General Infos:

This project is divided in three core modules:

1) The Simulation module (SIM): The SIM.jar is the core simulation. >>java -jar SIM.jar will start the simulation. This setup allows to shift pre- and postprocessing to a different environment (e.g. Matlab, python a.s.o). The folder INP/ contains all relevant input files to start the simulation

![Github SIM](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/FlowChart/flowchart.png)

![Github SIM](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/develop/documentation/UML_Diagram.png)

2) The GUI module (GUI): The GUI BlueBook_DaLAT.jar allows to perform pre- and postprocessing and visualise results. 

![Github GUI](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/GUIImage4.png)

![Github GUI2](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/GUIImage9.png)

The images show a quick preview of the user interface dashboard. The chart area on the right hand side is adjustable. The user can select from a variety of 2D charts and 3D content. Currently there are two 3D windows implemented. The first image shows the target body view in the upper right corner. This window also shows the computed trajectory and the respective position of the spacecraft on the trajectory that corresponds to the selected position on the graph in the lower left corner (Note this only works if time is selected as the x-value). The second 3D content (in the lower right corner) shows the attitude of the spacecraft with respect to the local North-East-Down system.

The GUI is designed to simplify pre- and postprocessing work and allow to set up simulations from low to high levels of complexity in a streamlined manner and interpret the results quickly. 

![Github GUI3](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/GUIImage5.png)

The image shows the simulation input section to prepare a simulation. For more clarity this section is devided in subtabs. The shown basic setup section allows to setup the initial state of the spacecraft including position, velocity and attitude  vectors. Furthermore settings concerning the integration method, the used coordinate systems and the time resolution can be made here. 

To model the propulsion sub-system a graphical block modelling approach is currently tested. The editor library for this has been developed for this GUI. 

![Github GUI7](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/GUIImage7.png)

The image shows a simple propulsion system setup created with this editor. 

To generate basic geometry input data (vital for 6DoF propusive flight) a small geometry module can be used. 

![Github GUI8](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/GUIImage8.png)

3) The VisualEngine (VE). The VisualEngine is a game engine environment (based on the LWJGL 2 library) developed to visualise simulation results and allow real time simulations (using the SIM module) with user inputs (pilot in the loop). The VE is currently in early development and has only a short real time flight demo implemented (FlyMeToTheMoon.jar). 

![Github VisualEngine](https://raw.githubusercontent.com/maxxonair/BlueBook-Descent-and-Landing-Analysis-Toolkit-DaLAT/master/documentation/INPUT_Documentation/VisualEngine.png)

Note: 

BlueBook is developed to be a crossplatform application compatible to all operating systems. The project is currently developed and tested mainlay on MacOS, which can cause minor bugs and GUI issues when running on other platforms. However, the system is from time to time tested on Windows and Linux and should run on both systems without major issues. Currently some bugs can be experienced when running the GUI from Windows, the SIM module should run without problems on all operating systems. Work is ongoing to eliminate bugs and run all modules smoothly on all operating systems. 

To run the VisualEngine the program has to be compiled on the respective operating system. 
To set up the project in Eclipse the natives for the LWJGL jars have to be set accordingly. 
Choose the project >> Properties >> Java Build Path >> select lwjgl.jar files >> double click natives > set path to Project/lib/natives/(select OS)

Currently natives for macOS, Windows, Linux and solaris are part of the project (in the lib folder). For additional ones visit:
http://legacy.lwjgl.org/download.php.html

