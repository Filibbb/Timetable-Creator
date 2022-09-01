package ch.zhaw.pm2.napp.fileio.loader;

import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.logger.LogConfiguration;
import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * A class to load csv Files from Filesystem and hold a List of resource Objects such as Building or Person
 *
 * @param <T> resource Object Type
 * @author wartminc
 * @version 1.0.0
 */
public abstract class AbstractCsvLoader<T> {
    private static final Logger LOGGER = getLogger(LogConfiguration.class.getCanonicalName());
    private static final int SUBJECT_NAME_INDEX = 0;
    private static final int SUBJECT_WEEKLY_LESSONS_INDEX = 1;
    private static final String SUBJECT_DELIMITER = ",";

    private final List<T> resources = new ArrayList<>();

    public List<T> getResources() {
        return resources;
    }

    /**
     * Iterates over each Line of the csv File to create an object for each of then and stores them in th resources List
     *
     * @param linesOfFile A List of Strings, each representing one Line of the csv file
     * @throws BadCsvFormatException Exceptions thrown in createResource are not caught in this Method
     */
    protected void populateResources(List<String> linesOfFile) throws BadCsvFormatException {
        int currentLine = 1;
        try {
            for (String propertyString : linesOfFile) {
                String[] properties = propertyString.split(";");
                resources.add(createResource(properties));
                currentLine++;
            }
        } catch (BadCsvFormatException e) {
            throw new BadCsvFormatException(e.getMessage() + " - in Line: " + currentLine);
        }
    }

    /**
     * One String array representing a Line of the csv File has to be converted to a resource Object.
     *
     * @param properties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return a resource Object
     * @throws BadCsvFormatException Inconsistent Data in the csv File
     */
    protected abstract T createResource(String[] properties) throws BadCsvFormatException;

    /**
     * Iterates over the properties of the given Resource String array, which needs to be Subject Entries only,
     * to create a List of Subjects and returns it.
     * If there is a Subject to be added from the String, and this Subject already exists in the give allSubjects Set,
     * the pre-existing Subject will be added to the List, rather than creating a new one. This is done to prevent duplicate
     * Subjects with the Same Properties.
     *
     * @param properties  String array of Resource properties
     * @param allSubjects A Set of pre-existing Subjects
     * @return A List of Subjects created from properties or extracted from allSubjects
     * @throws BadCsvFormatException is thrown, if there is an invalid input in the "subjectHours" Field (forwarded from returnOrCreateSubjectOfList)
     */
    protected List<Subject> createOrGetSubjects(String[] properties, Set<Subject> allSubjects) throws BadCsvFormatException {
        List<Subject> returnSubjects = new ArrayList<>();
        for (String propertyString : properties) {
            String[] subjectProperties = propertyString.split(SUBJECT_DELIMITER);
            try {
                returnSubjects.add(returnOrCreateSubjectOfList(subjectProperties[SUBJECT_NAME_INDEX], subjectProperties[SUBJECT_WEEKLY_LESSONS_INDEX], allSubjects));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new BadCsvFormatException("Bad Subject Format");
            }
            allSubjects.addAll(returnSubjects);
        }
        return returnSubjects;
    }

    /**
     * If the given List contains a Subject Object with the according given "subjectName" this Object is returned.
     * If this is not the case, a new Subject Object is created and returned
     *
     * @param subjectName  Name of the Subject, the list is searched for
     * @param subjectHours Hours of the Subject to be added, is the Objects needs to be created
     * @param listToSearch List to search for the Subject Object
     * @return The Subject Object, either found in the List or created new
     * @throws BadCsvFormatException is thrown, if there is an invalid input in the "subjectHours" Field
     */
    private Subject returnOrCreateSubjectOfList(String subjectName, String subjectHours, Set<Subject> listToSearch) throws BadCsvFormatException {
        if (!listToSearch.isEmpty()) {
            for (Subject subject : listToSearch) {
                if (subjectName.equals(subject.name())) {
                    return subject;
                }
            }
        }
        try {
            return new Subject(subjectName, Integer.parseInt(subjectHours));
        } catch (NumberFormatException e) {
            throw new BadCsvFormatException("Cannot parse subject hours");
        }
    }

    protected List<String> readLinesOfFile(File file) throws BadCsvFormatException {
        if (file == null || !file.exists()) {
            throw new BadCsvFormatException("Selected file(s) does not exist. Please select one.");
        }
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            return fileReader.lines().toList();
        } catch (IOException e) {
            LOGGER.severe("There was an error reading the CSV File: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}