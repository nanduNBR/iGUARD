package nandubrajan.ceknpy.iguard;

import java.util.Random;

/**
 * Created by Nandu B Rajan on 09-02-2018.
 */

public class constant {
    static String key;
    static String name;
    static String email;
    static String role;
    public static String lockid;

    Random rand = new Random();
    int  n = rand.nextInt(99999999) +19999999;
    Long s = Long.valueOf(n*992);

   public static String base_url = "http://192.168.43.222/iguard/";

}
