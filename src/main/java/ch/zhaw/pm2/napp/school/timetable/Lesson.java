package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.building.Room;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;

/**
 * This class represents a lesson with all the necessary information
 * <p>
 * <p>
 * Creates a new lesson object
 *
 * @param subject  the subject of the lesson
 * @param teacher  the teacher giving the lesson
 * @param timeSlot start time of the lesson
 * @param room     the room where the lesson takes place
 */

public record Lesson(Subject subject, Teacher teacher, TimeSlot timeSlot, Room room) {
}
