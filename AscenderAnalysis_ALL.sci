clear 
RootFolder='C:\Users\Max Braun\Documents\Max_Braun_2017\05_Tools\LandingSim\LandingSim-3DOF\burst_container\';
//initialize the list
BB_ascent = list();
step=10
tstart=1
tend = 190
for i=tstart:step:tend
    file_01 = 'burstres_';
    file_02 = '.txt';
    number = string(i);
    file_x = "" ;
    file_x = "burstres_"+number+".txt";
//disp(file_x)
interim= csvRead(fullfile(RootFolder, file_x), " ");
BB_ascent(i) = interim;
end
//filename2 = fullfile(RootFolder, 'burstres_180.txt');
//BB_ascent[i] = csvRead(filename[i], " ");
//BB_ascent_6_6 = csvRead(filename2, " ");
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
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,fpa_BB).*180/%pi-90,rand(1,3));
end
xlabel("Time [s]]");
ylabel("flight path angle [deg]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
subplot(222)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,alt_BB)/1000);
end
xlabel("Time [s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
/*
subplot(223)
plot(BB_ascent_6_6(:,vel_BB),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,vel_BB),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Velocity [m/s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
*/
subplot(224)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,thrust_BB)/1000);
end
xlabel("Time [s]]");
ylabel("Thrust [N]");
hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");


