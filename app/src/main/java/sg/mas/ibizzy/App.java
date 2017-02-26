package sg.mas.ibizzy;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sergey on 24.02.17.
 */

public class App extends Application {
   public static  Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
