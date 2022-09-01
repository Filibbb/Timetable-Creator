package ch.zhaw.pm2.napp.fileio.loader;

import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.io.File;
import java.util.*;

/**
 * SchoolClassesCsvLoader
 * <p>
 * this class is based on {@link AbstractCsvLoader} and therefore provides the functionality to retrieve a List of Objects
 * created from the values of a csv File.
 * </p>
 * <p>
 * This class specifically creates a List of SchoolClass Objects.
 * </p>
 * <p>
 *
 * @author wartminc
 * @version 1.0.0
 * </p>
 */
public class SchoolClassesCsvLoader extends AbstractCsvLoader<SchoolClass> {

    private static final int FIRST_SUBJECT_INDEX = 1;
    private static final int CLASS_ID_INDEX = 0;
    private int firstTeacherIdIndex;
    private int firstStudentIdIndex;
    private final List<Person> allPeople;
    private final Set<Subject> allSubjects = new HashSet<>();

    /**
     * Initializing csv Loader with File to be processed and adds values passed through allClasses and allPeople to the
     * equivalent fields of the instance
     *
     * @param file      File to be processed
     * @param allPeople a List of all people (students and Teachers) at the school
     * @throws BadCsvFormatException with message and Line of csv, is thrown if the csv Values do not match the requirements
     */
    public SchoolClassesCsvLoader(File file, List<Person> allPeople) throws BadCsvFormatException {
        this.allPeople = allPeople;
        populateResources(readLinesOfFile(file));
    }

    /**
     * A String array representing a Line of the csv File is converted to a SchoolClass Object.
     *
     * @param schoolClassProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return a SchoolClass Object
     * @throws BadCsvFormatException is thrown if the SchoolClass_ID field is Empty, ,ay alo be thrown by getPeopleIndices
     */
    @Override
    protected SchoolClass createResource(String[] schoolClassProperties) throws BadCsvFormatException {
        SchoolClass schoolClass;
        assignPeopleIndices(schoolClassProperties);
        List<Subject> classSubjectList = createOrGetSubjects(Arrays.copyOfRange(schoolClassProperties, FIRST_SUBJECT_INDEX, firstTeacherIdIndex), allSubjects);
        allSubjects.addAll(classSubjectList);
        try {
            schoolClass = new SchoolClass(schoolClassProperties[CLASS_ID_INDEX], classSubjectList);
        } catch (IllegalArgumentException e) {
            throw new BadCsvFormatException("SchoolClass_ID Fields is Empty");
        }
        assignTeachers(schoolClass, schoolClassProperties);
        assignStudents(schoolClass, schoolClassProperties);
        return schoolClass;
    }

    /**
     * Reads a schoolClassProperties String array and writes the indices of the first Teacher Element and the first Student
     * Element to the corresponding Fields (firstStudentIdIndex, firstTeacherIdIndex)
     *
     * @param schoolClassProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @throws BadCsvFormatException is thrown if there is either no Teacher or no Student Field entered.
     */
    private void assignPeopleIndices(String[] schoolClassProperties) throws BadCsvFormatException {
        List<String> schoolClassPropertyList = Arrays.stream(schoolClassProperties).toList();
        Optional<String> firstTeacherId = schoolClassPropertyList.stream().filter(property -> property.contains("TEACHER_")).findFirst();
        Optional<String> firstStudentId = schoolClassPropertyList.stream().filter(property -> property.contains("STUDENT_")).findFirst();
        if (firstStudentId.isEmpty()) {
            throw new BadCsvFormatException("No Student assigned to SchoolClass");
        }
        if (firstTeacherId.isEmpty()) {
            throw new BadCsvFormatException("No Teacher assigned to SchoolClass");
        }
        firstTeacherIdIndex = schoolClassPropertyList.indexOf(firstTeacherId.get());
        firstStudentIdIndex = schoolClassPropertyList.indexOf(firstStudentId.get());
    }

    public Set<Subject> getAllSubjects() {
        return allSubjects;
    }

    /**
     * Iterates over the Teacher Entries of the schoolClassProperties String and assigns the corresponding
     * Person to the class which is passed as schoolClass parameter
     *
     * @param schoolClass           class where Teachers need to be added
     * @param schoolClassProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @throws BadCsvFormatException - is thrown if there was a Teacher Entry with an invalid MatriculationNumber
     */
    private void assignTeachers(SchoolClass schoolClass, String[] schoolClassProperties) throws BadCsvFormatException {

        for (int i = firstTeacherIdIndex; i < firstStudentIdIndex; i++) {
            String teacherId = schoolClassProperties[i].substring(schoolClassProperties[i].indexOf("_") + 1);
            Optional<Person> teacherToAdd = allPeople.stream().filter( person -> person.getId().equals(teacherId)).findFirst();
            try{
                if(teacherToAdd.isPresent()) {
                    schoolClass.addTeacher((Teacher) teacherToAdd.get());
                } else {
                    throw new BadCsvFormatException("Teacher with Matriculation Number: \"" + teacherId + "\" does not exist");
                }
            } catch (ClassCastException e) {
                throw new BadCsvFormatException("Person with Matriculation Number: \"" + teacherId + "\" is Not a Teacher");
            }
        }
    }

    /**
     * Iterates over the Student Entries of the schoolClassProperties String and assigns the corresponding
     * Person to the class which is passed as schoolClass parameter
     *
     * @param schoolClass           class where Students need to be added
     * @param schoolClassProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @throws BadCsvFormatException - is thrown if there was a Student Entry with an invalid MatriculationNumber
     */
    private void assignStudents(SchoolClass schoolClass, String[] schoolClassProperties) throws BadCsvFormatException {
        for (int i = firstStudentIdIndex; i < schoolClassProperties.length; i++) {
            String studentId = schoolClassProperties[i].substring(schoolClassProperties[i].indexOf("_") + 1);
            Optional<Person> studentToAdd = allPeople.stream().filter( person -> person.getId().equals(studentId)).findFirst();
            if(studentToAdd.isPresent()) {
                schoolClass.addStudent(studentToAdd.get());
            } else {
                throw new BadCsvFormatException("Student with Matriculation Number: \"" + studentId + "\" does not exist");
            }
        }
    }
}