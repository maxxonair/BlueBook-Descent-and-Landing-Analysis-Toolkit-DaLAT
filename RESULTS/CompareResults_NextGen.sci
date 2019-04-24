clear 
RootFolder='/Users/x/Documents/003_Tools/006_ControlledLandingSim/BlueBook-DaLAT-3DoF/RESULTS/';

filename = fullfile(RootFolder, 'NextGen_mk1_60.res');
filename2 = fullfile(RootFolder, 'NextGen_mk1_150.res');
BB_ascent_5_6 = csvRead(filename, " ");
BB_ascent_6_6 = csvRead(filename2, " ");
//------------------------------------------------------------------------------
//                       Variable columns - BB
//------------------------------------------------------------------------------
t=1;
fpa_BB = 8;
vel_BB = 7;
m0_BB = 30;
thrust_BB= 28; 
alt_BB = 5;
deltav_BB=39;
mach_BB = 18;
//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
subplot(221)
plot(BB_ascent_6_6(:,vel_BB),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,vel_BB),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Velocity [m/s]");
ylabel("Altitude [km]");
hl = legend("Ballistic Coefficient = 60 kg/sqrm", "Ballistic Coefficient = 150 kg/sqrm");

subplot(222)
plot(BB_ascent_6_6(:,mach_BB),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,mach_BB),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Mach number [-]");
ylabel("Altitude [km]");
hl = legend("Ballistic Coefficient = 60 kg/sqrm", "Ballistic Coefficient = 150 kg/sqrm");
a=get("current_axes")//get the handle of the newly created axes
a.data_bounds=[0,0;4,30]; //set the boundary values for the x, y and z coordinates.
