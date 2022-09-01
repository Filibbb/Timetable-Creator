package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.ModuleSchedule;
import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.building.Room;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ch.zhaw.pm2.napp.school.School.WORKDAYS_PER_WEEK;

/**
 * This class contains the algorithm to generate a timetable. <br>
 * <p>
 * The TimetableGenerator uses the free TimeSlots in the school class {@link Timetable} and the school class {@link ModuleSchedule} to search for suitable teachers and rooms. <br>
 * <ul> A suitable {@link Room} is one that is not occupied at this time and that has enough space for all students & teachers.<br>
 * A suitable {@link Teacher} is one who can teach the subject, who is free at that time, who is in school that day and who still has capacity based on the workload.<ul>
 */
public class TimetableGenerator {
    private final List<Teacher> allTeachers;
    private final List<Building> schoolBuildings;

    /**
     * Creates a timetable generator.
     *
     * @param allTeachers     from the school
     * @param schoolBuildings from the school
     */
    public TimetableGenerator(List<Teacher> allTeachers, List<Building> schoolBuildings) {
        this.allTeachers = allTeachers;
        this.schoolBuildings = schoolBuildings;
    }

    /**
     * Generates a timetable for a school class.
     *
     * @param schoolClass The school class for which the timetable is to be created
     * @throws TimetableException if no teacher, room or free time slot was found for a subject.
     */
    public void generateTimeTableForClass(SchoolClass schoolClass) throws TimetableException {
        for (Subject subject : schoolClass.getModuleSchedule().requiredSubjects()) {
            boolean placeForSubjectFound = false;
            int weekDay = 1;
            while (weekDay <= WORKDAYS_PER_WEEK && !placeForSubjectFound) {
                DayOfWeek dayOfWeek = DayOfWeek.of(weekDay);
                List<TimeSlot> freeSubjectTimeSlotSchoolClass = schoolClass.getTimetable().getFreeTimeSlotsForSubjectPerDay(subject, dayOfWeek).stream().findFirst().orElse(new ArrayList<>());
                Room availableRoom = getAvailableRooms(dayOfWeek, freeSubjectTimeSlotSchoolClass, schoolClass.size()).stream().findFirst().orElse(null);
                Teacher availableTeacher = getAvailableTeachers(dayOfWeek, freeSubjectTimeSlotSchoolClass, subject).stream().findFirst().orElse(null);

                for (TimeSlot availableTimeSlot : freeSubjectTimeSlotSchoolClass) {
                    if (availableTeacher != null && availableRoom != null) {
                        Lesson lesson = new Lesson(subject, availableTeacher, availableTimeSlot, availableRoom);
                        schoolClass.getTimetable().addToTimetable(dayOfWeek, lesson);
                        addTimetableToEveryStudentInSchoolClass(dayOfWeek, lesson, schoolClass);
                        availableTeacher.getTimetable().addToTimetable(dayOfWeek, lesson);
                        availableRoom.setRoomAvailability(dayOfWeek, availableTimeSlot);
                        placeForSubjectFound = true;
                    }
                }
                weekDay++;
            }
            if (!placeForSubjectFound) {
                throw new TimetableException("No free timeslot found for the subject \"" + subject.name() + "\" and the school class \"" + schoolClass.getId() + "\". Check the resources.");
            }
        }
    }

    private void addTimetableToEveryStudentInSchoolClass(DayOfWeek dayOfWeek, Lesson lesson, SchoolClass schoolClass) {
        for (Person student : schoolClass.getStudents()) {
            student.getTimetable().addToTimetable(dayOfWeek, lesson);
        }
    }

    private List<Teacher> getAvailableTeachers(DayOfWeek dayOfWeek, List<TimeSlot> timeSlots, Subject subject) {
        List<Teacher> availableTeachers = new ArrayList<>();

        for (TimeSlot timeSlot : timeSlots) {
            List<Teacher> availableTeachersForOneTimeSlot = allTeachers.stream().filter(teacher -> teacher.isFreeToTeach(dayOfWeek, timeSlot, subject)).collect(Collectors.toList());

            if (availableTeachers.isEmpty()) {
                availableTeachers.addAll(availableTeachersForOneTimeSlot);
            } else {
                List<Teacher> availableTeachersForSeveralTimeSlots = new ArrayList<>();
                for (Teacher availableTeacherForOneTimeSlot : availableTeachersForOneTimeSlot) {
                    for (Teacher availableTeacher : availableTeachers) {
                        if (availableTeacherForOneTimeSlot.equals(availableTeacher)) {
                            availableTeachersForSeveralTimeSlots.add(availableTeacher);
                        }
                    }
                }
                availableTeachers = availableTeachersForSeveralTimeSlots;
            }
        }
        return availableTeachers;
    }

    private List<Room> getAvailableRooms(DayOfWeek dayOfWeek, List<TimeSlot> timeSlots, int size) {
        List<Room> availableRooms = new ArrayList<>();

        for (TimeSlot timeSlot : timeSlots) {
            List<Room> availableRoomsForOneTimeSlot = getAllRooms().stream().filter(room -> room.isAvailable(dayOfWeek, timeSlot, size)).collect(Collectors.toList());

            if (availableRooms.isEmpty()) {
                availableRooms.addAll(availableRoomsForOneTimeSlot);
            } else {
                List<Room> availableRoomsForSeveralTimeSlots = new ArrayList<>();
                for (Room availableRoomForOneTimeSlot : availableRoomsForOneTimeSlot) {
                    for (Room availableRoom : availableRooms) {
                        if (availableRoomForOneTimeSlot.equals(availableRoom)) {
                            availableRoomsForSeveralTimeSlots.add(availableRoom);
                        }
                    }
                }
                availableRooms = availableRoomsForSeveralTimeSlots;
            }
        }
        return availableRooms;
    }

    private List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        for (Building building : schoolBuildings) {
            rooms.addAll(building.getAllRooms());
        }
        return rooms;
    }
}
