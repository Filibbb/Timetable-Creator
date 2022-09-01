package ch.zhaw.pm2.napp.school;

import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.util.List;

/**
 * The module schedule includes all subjects that a student must attend or that a teacher can teach.
 */
public record ModuleSchedule(List<Subject> requiredSubjects) {
}
