package ai.ghosty.paycheck.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/*نیازی به گرفتن زمان نیست از زمان سیستم من استفاده میکنم 
 */
public class Logger {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE = "app.log";


    public static void log(String message, LogLevel level) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);

        File file = new File(LOG_FILE);

        //اگر فایل وجود نداشت اونو میسازه 
        if (file.getParentFile() != null) 
            file.getParentFile().mkdirs();
       
        //در این کد اطلاعات رو به فایل اضافه میکنه 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Could not write to log file: " + e.getMessage());
        }
    }
}
