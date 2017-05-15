package com.example.nguyenduchai.joggerdemo.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nguyen Duc Hai on 5/13/2017.
 */

public class Util {
    public static String getCurrentDateTime()
    {
        // get datetime
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = simpleDateFormat.format(new Date());
        return strDate;
    }

}
