package ch.zhaw.pm2.napp.school.schoolclasses;

import ch.zhaw.pm2.napp.school.timetable.Subject;
import ch.zhaw.pm2.napp.school.timetable.TimeSlot;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Teacher
 * <p>
 * this class is based on {@link Person} and therefore is a Dataclass containing all Information to a Person.
 * Other then it's superclass this Class is capable of holding additional Information, such as workload and availableWeekDays
 * </p>
 *
 * @author buechad1, wartminc
 * @version 1.0.0
 */
public class Teacher extends Person {

    private final double workload;
    private final List<DayOfWeek> availableWeekDays;

    /**
     * Instances a Teacher object which contains all required data.
     *
     * @param matriculationNumber Unique Identifier of a Person
     * @param lastName            Last Name
     * @param firstName           First Name
     * @param schoolVisitorRole   List of SchoolVisitorRole (Teachers can also be Students)
     * @param workload            how many percent of the default pensum can be covered by this Teacher
     * @param availableWeekDays   on which days this Teacher can work
     * @param teachableSubjects   List of subjects this Teacher can teach
     */
    public Teacher(String matriculationNumber, String lastName, String firstName, List<SchoolVisitorRole> schoolVisitorRole, double workload, List<DayOfWeek> availableWeekDays, List<Subject> teachableSubjects) throws IllegalArgumentException {
        super(matriculationNumber, lastName, firstName, schoolVisitorRole, teachableSubjects);
        this.workload = workload;
        this.availableWeekDays = availableWeekDays;
    }

    /**
     * Returns whether the teacher can teach. Depending on the schedule, the subject and the workload
     *
     * @param dayOfWeek the day of the week when you want to know if the teacher is free.
     * @param timeSlot  at which the teacher should be free
     * @param subject   that is to be taught
     * @return whether the teacher can teach or not
     */
    public boolean isFreeToTeach(DayOfWeek dayOfWeek, TimeSlot timeSlot, Subject subject) {
        if (getTimetable() == null) {
            return true;
        }
        return isAvailable(dayOfWeek, timeSlot, subject) && canTeachThisSubject(subject) && isWorkLoadNotTooHigh(subject);
    }

    private boolean canTeachThisSubject(Subject subject) {
        return getModuleSchedule().requiredSubjects().contains(subject);
    }

    private boolean isWorkLoadNotTooHigh(Subject subject) {
        double workloadInLessons = getTimetable().getAmountOfPossibleLessonsPerWeek() * workload;
        return getTimetable().getAmountOfSetLessonsPerWeek() + subject.weeklyLessons() <= workloadInLessons;
    }

    private boolean isAvailable(DayOfWeek dayOfWeek, TimeSlot timeSlot, Subject subject) {
        if (availableWeekDays.contains(dayOfWeek)) {
            for (List<TimeSlot> list : getTimetable().getFreeTimeSlotsForSubjectPerDay(subject, dayOfWeek)) {
                if (list.contains(timeSlot)) {
                    return true;
                }
            }
        }
        return false;
    }
}