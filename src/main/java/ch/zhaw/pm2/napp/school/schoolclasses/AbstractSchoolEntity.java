package ch.zhaw.pm2.napp.school.schoolclasses;

import ch.zhaw.pm2.napp.school.ModuleSchedule;
import ch.zhaw.pm2.napp.school.timetable.Subject;
import ch.zhaw.pm2.napp.school.timetable.Timetable;

import java.util.List;

/**
 * This AbstractSchoolEntity is a base class containing a module schedule and a timetable.
 * This class is an abstract base for a {@link Teacher}, {@link Person} and {@link SchoolClass}
 * As each unique class all share the timetables and module schedules, this class prepares them for each individual case.
 * {@link Teacher}, {@link Person} can have a unique timetable which does not necessary must be the same as the school class timetable
 *
 * @author buechad1
 */
public abstract class AbstractSchoolEntity {
    private final String id;
    private final ModuleSchedule moduleSchedule;
    private Timetable timetable;
    private List<Subject> subjects;

    /**
     * Creates an AbstractSchoolEntity with a {@link ModuleSchedule} and {@link Timetable} that are required for school entity
     *
     * @param subjects - List<Subject> containing all required Subjects that are required to create a moduleSchedule
     * @param id       - the id of the school entity. Could be marticular number or class id or similar.
     */
    protected AbstractSchoolEntity(List<Subject> subjects, String id) {
        if (!id.isEmpty()) {
            this.moduleSchedule = new ModuleSchedule(subjects);
            this.timetable = new Timetable();
            this.id = id;
            this.subjects = subjects;
        } else {
            throw new IllegalArgumentException("Missing ID");
        }
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public ModuleSchedule getModuleSchedule() {
        return moduleSchedule;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public String getId() {
        return id;
    }
}
