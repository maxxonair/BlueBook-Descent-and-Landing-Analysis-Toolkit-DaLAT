package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import GUI.BlueBookVisual;
import GUI.Dashboard.ChartSetting;

public class WriteInput {

	
	
    public static void writeDashboradSetting(List<ChartSetting> chartSetting) {

        try {
            File fac = new File(ReadInput.dashboardSettingFile);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }

            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i< chartSetting.size(); i++)
            {
            	wr.write(chartSetting.get(i).type+BlueBookVisual.BB_delimiter+chartSetting.get(i).x+BlueBookVisual.BB_delimiter+chartSetting.get(i).y+System.getProperty( "line.separator" ));
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
}
