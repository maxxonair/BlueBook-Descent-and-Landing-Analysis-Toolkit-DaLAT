clear 
//------------------------------------------------------------------------------
//                       Variable columns - BB
//------------------------------------------------------------------------------
t=1;
fpa_BB = 8;
vel_BB = 7;
m0_BB = 30;
res_prop = 34; 
thrust_BB= 28; 
deltav_BB=39;
alt_BB = 4;
//------------------------------------------------------------------------------
RootFolder='C:\Users\Max Braun\Documents\Max_Braun_2017\05_Tools\LandingSim\LandingSim-3DOF\burst_container\';
//initialize the list
BB_ascent = list();
step=1
tstart=1
tend = 190
for i=tstart:step:tend
    file_01 = 'burstres_';
    file_02 = '.txt';
    number = string(i);
    file_x="";
    file_x = "burstres_"+number+".txt";
//disp(file_x)
interim= csvRead(fullfile(RootFolder, file_x), " ");
    RESULT(i,1)=i
    RESULT(i,2)=interim($,res_prop)
    RESULT(i,3)=interim($,deltav_BB)
//BB_ascent(i) = interim;
end
//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
subplot(221)
for i=tstart:step:tend
plot(RESULT(:,1),RESULT(:,2),'b');
end
xlabel("Engine lost Take-off + [s]]");
ylabel("Residual Propellant in LLO [perc]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");

subplot(222)
for i=tstart:step:tend
plot(RESULT(:,1),RESULT(:,3),'b');
end
xlabel("Engine lost Take-off + [s]]");
ylabel("Delta-v [m/s]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
/*
subplot(223)
plot(BB_ascent_6_6(:,vel_BB),BB_ascent_6_6(:,alt_BB)/1000,'b');
plot(BB_ascent_5_6(:,vel_BB),BB_ascent_5_6(:,alt_BB)/1000,'r');
xlabel("Velocity [m/s]]");
ylabel("Altitude [km]");
//hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");

subplot(224)
for i=tstart:step:tend
plot(BB_ascent(i)(:,t),BB_ascent(i)(:,thrust_BB)/1000);
end
xlabel("Time [s]]");
ylabel("Thrust [N]");
hl = legend("BB mk4 Engines 6 out of 6", "BB mk4 Engines 6 out of 6");
*/

