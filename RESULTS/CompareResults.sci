clear 
RootFolder='C:\Users\Max Braun\Documents\Max_Braun_2017\05_Tools\LandingSim\BlueBook-DaLAT-3DoF\RESULTS\';

filename = fullfile(RootFolder, 'Ascent_36kN_FULLControl_mk1.res');
filename2 = fullfile(RootFolder, 'Ascent_36kN_mk1.res');
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
//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
subplot(221)
plot(BB_ascent_6_6(:,t),(BB_ascent_6_6(:,fpa_BB).*180/%pi-90)-(BB_ascent_5_6(:,fpa_BB).*180/%pi-90),'b');
//plot(BB_ascent_5_6(:,t),BB_ascent_5_6(:,fpa_BB).*180/%pi-90,'r');
xlabel("Time [s]]");
ylabel("flight path angle [deg]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");

subplot(222)
plot(BB_ascent_6_6(:,t),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,t),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Time [s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
subplot(223)
plot(BB_ascent_6_6(:,vel_BB),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,vel_BB),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Velocity [m/s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
subplot(224)
plot(BB_ascent_6_6(:,t),BB_ascent_6_6(:,thrust_BB)/1000,'b');
plot(BB_ascent_5_6(:,t),BB_ascent_5_6(:,thrust_BB)/1000,'r');
xlabel("Time [s]]");
ylabel("Thrust [kN]");
hl = legend("BB mk1 Engines 6 out of 6 - 36kN ", "BB mk1 Engines 5 out of 6 - 30kN");

disp("Total delta-v case 1: ",BB_ascent_6_6($,deltav_BB)," [m/s]");
disp("Total delta-v case 2: ",BB_ascent_5_6($,deltav_BB)," [m/s]");
