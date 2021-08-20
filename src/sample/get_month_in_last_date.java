package sample;

import java.util.Hashtable;

public class get_month_in_last_date {
public static Hashtable<Integer,Integer>month_data=new Hashtable<>();
   public  int year;

    public get_month_in_last_date(int month,int year){
        month_data.put(1,31);


        month_data.put(2,leap_year(year));
        month_data.put(3,31);
        month_data.put(4,30);
        month_data.put(5,31);
        month_data.put(6,30);
        month_data.put(7,31);
        month_data.put(8,31);
        month_data.put(9,30);
        month_data.put(10,31);
        month_data.put(11,30);
        month_data.put(12,31);




    }
    public int leap_year(int year){
        if(year%4==0 || year%400==0){
            return 29;
        }
        else{
            return 28;
        }
    }
    public int get_last_date(int month){
        return month_data.get(month);

    }
}
