clear 
filename = fullfile('C:\Users\Max Braun\Documents\Max_Braun_2017\09_LunarExplorationAnalysis\02_Engineering\AscentStudy\', 'Astos.csv');
filename2 = fullfile('C:\Users\Max Braun\Documents\Max_Braun_2017\09_LunarExplorationAnalysis\02_Engineering\AscentStudy\', 'BB_mk3.txt');
ASTOS_ascent_01 = csvRead(filename, ",");
BB_ascent_02 = csvRead(filename2, " ");
//------------------------------------------------------------------------------
//                      Variable columns - ASTOS
//------------------------------------------------------------------------------
time1= 107;     //[s]
time2= 108;     //[s]
fpa = 123;      //[deg]
//altitude = ;
vel = 126;      // km/s
isp = 133;      //[s]
m=158;          //[kg]
mprop = 187;    //[kg]
thrust=244; //[kN]
alt = 33;

//------------------------------------------------------------------------------
//                       Variable columns - BB
//------------------------------------------------------------------------------
t=1;
fpa_BB = 8;
vel_BB = 7;
m0_BB = 30;
thrust_BB= 28; 
alt_BB = 4;
//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
subplot(221)
plot(ASTOS_ascent_01(:,time1),ASTOS_ascent_01(:,fpa),'b');
plot(BB_ascent_02(:,t),BB_ascent_02(:,fpa_BB).*180/%pi-90,'r');
xlabel("Time [s]]");
ylabel("flight path angle [deg]");
hl = legend("Astos 9.2", "BB mk3");

subplot(222)
plot(ASTOS_ascent_01(:,time1),ASTOS_ascent_01(:,vel).*1000,'b');
plot(BB_ascent_02(:,t),BB_ascent_02(:,vel_BB),'r');
xlabel("Time [s]]");
ylabel("Velocity [m/s]");
hl = legend("Astos  9.2", "BB mk3");
subplot(223)
plot(ASTOS_ascent_01(:,time1),ASTOS_ascent_01(:,thrust),'b');
plot(BB_ascent_02(:,t),BB_ascent_02(:,thrust_BB)/1000,'r');
xlabel("Time [s]]");
ylabel("Thrust [kN]");
hl = legend("Astos  9.2", "BB mk3");
subplot(224)
plot(ASTOS_ascent_01(:,time1),ASTOS_ascent_01(:,alt),'b');
plot(BB_ascent_02(:,t),BB_ascent_02(:,alt_BB)/1000,'r');
xlabel("Time [s]]");
ylabel("Altitude [m]");
hl = legend("Astos  9.2", "BB mk3");
