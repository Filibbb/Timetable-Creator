package ch.zhaw.pm2.napp.school.timetable;

import java.time.LocalTime;

import static ch.zhaw.pm2.napp.school.School.LESSON_LENGTH;

/**
 * This class represents a timeslot in a timetable, with a start time, end time and a weekday.
 * Timeslots have a start time and end time and toString method is a combination of strings of both.
 */
public class TimeSlot {
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Creates a timeslot with a start time, end time and a weekday.
     *
     * @param startTime the start time.
     * @param endTime   the end time.
     */
    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates a timeslot with a start time and a weekday.
     * The end time is determined by the length of classes specified by the school.
     *
     * @param startTime the start time.
     */
    public TimeSlot(LocalTime startTime) {
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(LESSON_LENGTH);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) obj;
        return startTime.equals(timeSlot.startTime) && endTime.equals(timeSlot.endTime);
    }

    @Override
    public String toString() {
        return this.startTime + "-" + this.endTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
