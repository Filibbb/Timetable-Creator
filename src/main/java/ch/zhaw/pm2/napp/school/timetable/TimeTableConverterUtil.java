package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableConversionException;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeTableConverterUtil
 * A static utility class helping with the conversion of a timetable object and its lessons into a csv formatted string.
 *
 * @author buechad1
 */
public class TimeTableConverterUtil {
    private TimeTableConverterUtil() {
    }

    /**
     * Converts the give timetable object into a String Array containing each individual line of in a csv formatted way
     *
     * @param timetable - the timetable {@link Timetable} object to convert into csv based strings
     * @return an array containing strings of each csv line that was created based on the given timetable object
     * @throws TimetableConversionException if given timetable is null an exception is thrown as conversion cannot be done.
     */
    public static String[] convertTimetablesToCsvString(Timetable timetable) throws TimetableConversionException {
        if (timetable == null) {
            throw new TimetableConversionException("No timetable found. Make sure timetable has been created already.");
        }
        final List<String> csvLines = new ArrayList<>();
        timetable.getTimetable().forEach((dayOfWeek, lessons) -> {
            final StringBuilder stringBuilder = new StringBuilder();
            csvLines.add(stringBuilder.append(dayOfWeek.name())
                    .append(";")
                    .append(convertLessonsToString(lessons))
                    .toString());
        });
        return csvLines.toArray(String[]::new);

    }

    private static String convertLessonsToString(List<Lesson> lessons) {
        final StringBuilder stringBuilder = new StringBuilder();
        lessons.forEach(lesson -> stringBuilder.append("[")
                .append(lesson.timeSlot())
                .append(" ")
                .append(lesson.subject().name())
                .append(" ")
                .append(lesson.room().getRoomIdentifier())
                .append(" ")
                .append(lesson.teacher().getFirstName())
                .append(" ")
                .append(lesson.teacher().getLastName())
                .append("]")
                .append(";"));
        return stringBuilder.toString();
    }
}
