package com.netforceinfotech.inti.general;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tenzin on 1/16/2017.
 */

public class DateCheck {




   public static boolean isDateGreater(String firstDate,String secondDate){

       try{

           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

           Date firstdate = formatter.parse(firstDate);

           Date seconddate = formatter.parse(secondDate);

           if (firstdate.compareTo(seconddate)<=0)
           {
               System.out.println("date2 is greater or equal to the date...");
               return true;
           }

       }catch (ParseException e1){
           e1.printStackTrace();

       }

        return  false;

   }


    public static long getDateDifference(String fromDate,String toDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date efromDate = dateFormat.parse(fromDate);
            Date eToDate = dateFormat.parse(toDate);

            long dateDiff = eToDate.getTime()/(24*60*60*1000) -efromDate.getTime()/(24*60*60*1000);

            return dateDiff;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }





}
