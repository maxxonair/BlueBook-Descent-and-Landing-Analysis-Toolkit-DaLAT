clear 
RootFolder='C:\Users\Max Braun\Documents\Max_Braun_2017\05_Tools\LandingSim\BlueBook-DaLAT-3DoF\RESULTS\';

filename = fullfile(RootFolder, 'Ascent_30kN_mk1.res');
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
alt_BB = 4;
deltav_BB=39;
//------------------------------------------------------------------------------
//                       Create Reference Vector
//------------------------------------------------------------------------------
k=1
    x1 = BB_ascent_6_6(:,alt_BB);
    y1 = BB_ascent_6_6(:,fpa_BB);
    xy1 = [x1,y1];
    xy1=xy1'
    x2 = BB_ascent_5_6(:,alt_BB);
    y2 = BB_ascent_5_6(:,fpa_BB);
    xy2 = [x2,y2];
    xy2 = xy2'
for i=-4000:100:50000
    xx(k,1)=i;
k=k+1; 
end
    REFERENCE(:,1)= xx;
    REFERENCE(:,2)= interpln(xy1,xx);
    REFERENCE(:,3)= interpln(xy2,xx);

//------------------------------------------------------------------------------
//                                Plot 
//------------------------------------------------------------------------------
f = scf()
subplot(221)
plot(BB_ascent_6_6(:,alt_BB),BB_ascent_6_6(:,fpa_BB).*180/%pi-90,'b');
plot(BB_ascent_5_6(:,alt_BB),BB_ascent_5_6(:,fpa_BB).*180/%pi-90,'r');
plot(REFERENCE(:,1),REFERENCE(:,2).*180/%pi-90,'g');
plot(REFERENCE(:,1),REFERENCE(:,3).*180/%pi-90,'y');
xlabel("Altitude [km]");
ylabel("flight path angle [deg]");
