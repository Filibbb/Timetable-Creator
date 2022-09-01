package ch.zhaw.pm2.napp.school.timetable;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.zhaw.pm2.napp.school.School.*;

/**
 * This class includes the model of a school timetable, so that the long and short breaks can be taken into account.
 */
public class TimetableModel {
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int FOURTH = 3;
    private static final int FIFTH = 4;
    private static final int SIXTH = 5;
    private static final int SEVENTH = 6;
    private static final int EIGHTH = 7;
    private static final int NINTH = 8;

    /**
     * Creates a timetable model with maxed out timeslots and breaks in between
     *
     * @return a map with each day of week and a list of timeslots with breaks
     */
    public Map<DayOfWeek, List<TimeSlot>> getTimetableModel() {
        Map<DayOfWeek, List<TimeSlot>> emptyTimetable = new HashMap<>();
        for (int weekDay = 1; weekDay <= WORKDAYS_PER_WEEK; weekDay++) {
            List<TimeSlot> day = new ArrayList<>();
            //Morning
            day.add(new TimeSlot(SCHOOL_OPENING_HOURS));
            day.add(new TimeSlot(day.get(FIRST).getEndTime().plusMinutes(SHORT_BREAK)));
            day.add(new TimeSlot(day.get(SECOND).getEndTime().plusMinutes(LONG_BREAK)));
            day.add(new TimeSlot(day.get(THIRD).getEndTime().plusMinutes(SHORT_BREAK)));
            //Midday
            day.add(new TimeSlot(day.get(FOURTH).getEndTime().plusMinutes(LONG_BREAK)));
            day.add(new TimeSlot(day.get(FIFTH).getEndTime().plusMinutes(SHORT_BREAK)));
            //Afternoon
            day.add(new TimeSlot(day.get(SIXTH).getEndTime().plusMinutes(LONG_BREAK)));
            day.add(new TimeSlot(day.get(SEVENTH).getEndTime().plusMinutes(SHORT_BREAK)));
            day.add(new TimeSlot(day.get(EIGHTH).getEndTime().plusMinutes(LONG_BREAK)));
            day.add(new TimeSlot(day.get(NINTH).getEndTime().plusMinutes(SHORT_BREAK), SCHOOL_CLOSING_HOURS));
            emptyTimetable.put(DayOfWeek.of(weekDay), day);
        }
        return emptyTimetable;
    }
}
