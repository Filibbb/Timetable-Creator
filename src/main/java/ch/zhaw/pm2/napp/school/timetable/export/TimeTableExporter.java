package ch.zhaw.pm2.napp.school.timetable.export;

import ch.zhaw.pm2.napp.fileio.writer.CsvWriter;
import ch.zhaw.pm2.napp.school.schoolclasses.AbstractSchoolEntity;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableConversionException;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableExportException;

import java.io.IOException;
import java.util.List;

import static ch.zhaw.pm2.napp.school.timetable.TimeTableConverterUtil.convertTimetablesToCsvString;

/**
 * TimeTableExporter
 * <p>
 * This class is responsible for creating csv files based timetables for all given school entities. School entities can be people, classes or teachers.
 *
 * @author buechad1
 */
public class TimeTableExporter {

    private final CsvWriter csvWriter = new CsvWriter();

    /**
     * Exports the timetables into csv files based on the given school entities.
     *
     * @param schoolEntities - a complete collection of all school entities or the selected ones.
     * @param <T>            - a generic parameter of type {@link AbstractSchoolEntity}
     * @throws TimetableExportException - throws a custom {@link TimetableExportException} if an exception occurs while creating the csv file
     */
    public <T extends AbstractSchoolEntity> void exportTimeTables(List<T> schoolEntities) throws TimetableExportException, TimetableConversionException {
        try {
            for (T schoolEntity : schoolEntities) {
                csvWriter.writeCsv(schoolEntity.getId(), convertTimetablesToCsvString(schoolEntity.getTimetable()));
            }
        } catch (IOException e) {
            throw new TimetableExportException(e.getMessage());
        }
    }
}
