package com.example.taskmanager;

import java.util.*;
class DateI
{
    Date date;
    /* Constructor */
    DateI(Date date)
    {
        /* This keyword is used to refer current object */
        this.date = date;
    }
}
class sortCompare implements Comparator<DateI>
{
    // Method of this class
    @Override
    public int compare(DateI a, DateI b)
    {
        /* Returns sorted data in ascending order */
        return a.date.compareTo(b.date);
    }
}