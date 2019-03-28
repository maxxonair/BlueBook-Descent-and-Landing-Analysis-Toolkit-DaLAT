clear 
RootFolder='C:\Users\Max Braun\Documents\Max_Braun_2017\05_Tools\LandingSim\BlueBook-DaLAT-3DoF\burst_container\';
//initialize the list
BB_ascent = list();
step=25
tstart = 1
tend   = 450
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
m0_BB = 29;
thrust_BB= 34;
ctrl_error_BB = 42; 
alt_BB = 4;
//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
//------------------------------------------------------------------------------
subplot(221)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,fpa_BB).*180/%pi-90);
end
xlabel("Time [s]]");
ylabel("flight path angle [deg]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
//------------------------------------------------------------------------------
subplot(222)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,alt_BB)/1000);
end
xlabel("Time [s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
//------------------------------------------------------------------------------
subplot(223)
for i=tstart:step:tend  
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,ctrl_error_BB).*180/%pi);
a=gca()
end
a.data_bounds=[0,-20;600,10];
xlabel("Time [s]]");
ylabel("Controller Error [deg]");
//------------------------------------------------------------------------------
subplot(224)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,thrust_BB)/1000);
aa=gca()
end
xlabel("Time [s]]");
ylabel("Thrust [N]");
aa.data_bounds=[0,28;600,37];
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
//------------------------------------------------------------------------------
