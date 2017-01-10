package ravnik.org.meetatsport;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nejc Ravnik on 03-Jan-17.
 */
public class Logger {

    public void appendLog(Context context,String TAG,String data)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());


        final File path =context.getFilesDir().getAbsoluteFile() ;
        if(!path.exists()) {

            path.mkdirs();
        }

        final File file = new File(path, "logs");


        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append(currentDateandTime+":"+TAG+":"+data);
            buf.newLine();
            buf.close();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }


}
