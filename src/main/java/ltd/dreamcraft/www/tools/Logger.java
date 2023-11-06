package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.DragonAuthMe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void log(String message) {
        Date now = new Date();
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = dateSdf.format(now);
        String timeStr = timeSdf.format(now);

        File logsFolder = new File(DragonAuthMe.in().getDataFolder() + "/logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
        File logs = new File(logsFolder, dateStr + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(logs, true);
            fileWriter.write("[" + timeStr + "] " + message + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
