package ch.zhaw.pm2.napp.school.timetable;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Timetable class contains the timetable and the logic to check if the TimeSlot is free.
 * The timetable a HashMap containing the day of the week as key and a list of lessons with timeslots each.
 *
 * @author fupat002
 */
public class Timetable {
    private final Map<DayOfWeek, List<Lesson>> timetable;
    private final TimetableModel timetableModel;

    /**
     * Creates an empty timetable.
     */
    public Timetable() {
        this.timetable = new HashMap<>();
        this.timetableModel = new TimetableModel();
    }

    /**
     * Searches all available time slots suitable for a subject.
     * It depends on how many lessons have to be taught per week and
     * ensures that they can be taught one after the other.
     *
     * @param subject   for which you need the time slots
     * @param dayOfWeek the day from which you want the TimeSlots
     * @return a list of all available time slots for a subject
     */
    public List<List<TimeSlot>> getFreeTimeSlotsForSubjectPerDay(Subject subject, DayOfWeek dayOfWeek) {
        return getFreeTimeSlotsAfterTheOtherPerDay(subject.weeklyLessons(), dayOfWeek);
    }

    private List<List<TimeSlot>> getFreeTimeSlotsAfterTheOtherPerDay(int numberOfTimeSlots, DayOfWeek dayOfWeek) {
        List<List<TimeSlot>> freeTimeSlotsAfterTheOtherPerDay = new ArrayList<>();
        List<TimeSlot> allFreeTimeSlotsPerDay = getFreeTimeSlots(dayOfWeek);
        List<TimeSlot> timeSlotsModelPerDay = timetableModel.getTimetableModel().get(dayOfWeek);
        int placesPerDayForSubject = allFreeTimeSlotsPerDay.size() - (numberOfTimeSlots - 1);//Example: With allFreeTimeSlotsPerDay.size() = 10 and numberOfTimeSlots 2 there are 9 possible ways the two TimeSlots could fit in.
        List<TimeSlot> tempFreeTimeSlots = new ArrayList<>();

        for (int freeTimeSlotsIndex = 0; freeTimeSlotsIndex < placesPerDayForSubject; freeTimeSlotsIndex++) {
            TimeSlot freeTimeSlot = allFreeTimeSlotsPerDay.get(freeTimeSlotsIndex);

            for (int modelTimeSlotIndex = 0; modelTimeSlotIndex < timeSlotsModelPerDay.size(); modelTimeSlotIndex++) {
                TimeSlot modelTimeSlot = timeSlotsModelPerDay.get(modelTimeSlotIndex);
                tempFreeTimeSlots.addAll(compareFreeTimeSlots(numberOfTimeSlots, allFreeTimeSlotsPerDay, timeSlotsModelPerDay, freeTimeSlotsIndex, freeTimeSlot, modelTimeSlotIndex, modelTimeSlot));
            }
            if (tempFreeTimeSlots.size() == numberOfTimeSlots) {
                freeTimeSlotsAfterTheOtherPerDay.add(tempFreeTimeSlots);
                tempFreeTimeSlots = new ArrayList<>(); //reset temporary list
            }
        }

        return freeTimeSlotsAfterTheOtherPerDay;
    }

    private List<TimeSlot> compareFreeTimeSlots(int numberOfTimeSlots, List<TimeSlot> allFreeTimeSlotsPerDay, List<TimeSlot> timeSlotsPerDay, int freeTimeSlotsIndex, TimeSlot freeTimeSlot, int modelTimeSlotIndex, TimeSlot modelTimeSchlot) {
        List<TimeSlot> tempFreeTimeSlots = new ArrayList<>();
        if (modelTimeSchlot.equals(freeTimeSlot)) {
            tempFreeTimeSlots.add(freeTimeSlot);
            tempFreeTimeSlots.addAll(nextFreeTimeSlot(numberOfTimeSlots, allFreeTimeSlotsPerDay, timeSlotsPerDay, freeTimeSlotsIndex, modelTimeSlotIndex));
        }
        return tempFreeTimeSlots;
    }

    private List<TimeSlot> nextFreeTimeSlot(int numberOfTimeSlots, List<TimeSlot> allFreeTimeSlotsPerDay, List<TimeSlot> timeSlotsPerDay, int freeTimeSlotsIndex, int modelTimeSlotIndex) {
        List<TimeSlot> tempFreeTimeSlots = new ArrayList<>();
        for (int i = 1; i < numberOfTimeSlots; i++) {
            TimeSlot nextFreeTimeSlot = allFreeTimeSlotsPerDay.get(freeTimeSlotsIndex + i);
            TimeSlot nextModelTimeSchlot = timeSlotsPerDay.get(modelTimeSlotIndex + i);
            if (nextFreeTimeSlot.equals(nextModelTimeSchlot)) {
                tempFreeTimeSlots.add(nextFreeTimeSlot);
            }
        }
        return tempFreeTimeSlots;
    }

    private List<TimeSlot> getFreeTimeSlots(DayOfWeek dayOfWeek) {
        List<TimeSlot> freeTimeSlotsAtDay = new ArrayList<>();

        for (TimeSlot freeTimeSlot : timetableModel.getTimetableModel().get(dayOfWeek)) {
            if (isFree(dayOfWeek, freeTimeSlot)) {
                freeTimeSlotsAtDay.add(freeTimeSlot);
            }
        }
        return freeTimeSlotsAtDay;
    }

    private boolean isFree(DayOfWeek dayOfWeek, TimeSlot timeSlotToCheck) {
        if (timetable.get(dayOfWeek) == null) {
            return true;
        }
        return !timetable.get(dayOfWeek).stream().map(Lesson::timeSlot).collect(Collectors.toList()).contains(timeSlotToCheck);
    }

    public int getAmountOfPossibleLessonsPerWeek() {
        Map<DayOfWeek, List<TimeSlot>> timetableSlots = timetableModel.getTimetableModel();
        int weeklyLessons = 0;
        for (int i = 1; i <= timetableSlots.size(); i++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(i);
            weeklyLessons += timetableSlots.get(dayOfWeek).size();
        }
        return weeklyLessons;
    }

    /**
     * Calculates the amount of possible set lessons per week based on weekdays and max lessons per day
     *
     * @return the amount of lessons per week.
     */
    public int getAmountOfSetLessonsPerWeek() {
        int weeklyLessons = 0;
        for (int i = 1; i <= timetable.size(); i++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(i);
            if (timetable.get(dayOfWeek) != null) {
                weeklyLessons += timetable.get(dayOfWeek).size();
            }
        }
        return weeklyLessons;
    }

    /**
     * Adds a day with a lesson manually to the timetable
     *
     * @param dayOfWeek the day of week to add to the timetable
     * @param lesson    the lesson that should be added to the list of timeslots
     */
    public void addToTimetable(DayOfWeek dayOfWeek, Lesson lesson) {
        if (timetable.containsKey(dayOfWeek)) {
            timetable.get(dayOfWeek).add(lesson);
        } else {
            List<Lesson> lessonList = new ArrayList<>();
            lessonList.add(lesson);
            timetable.put(dayOfWeek, lessonList);
        }
    }

    public Map<DayOfWeek, List<Lesson>> getTimetable() {
        return timetable;
    }
}