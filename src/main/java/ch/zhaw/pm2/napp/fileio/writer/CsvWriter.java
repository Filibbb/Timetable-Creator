package ch.zhaw.pm2.napp.fileio.writer;

import ch.zhaw.pm2.napp.logger.LogConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.Instant;
import java.util.logging.Logger;

import static java.text.MessageFormat.format;
import static java.util.logging.Logger.getLogger;

/**
 * CsvWriter
 * <p>
 * This class works a csv writer and writes and creates custom csv files in a specified TimeTableCreator directory in the user home and writes given csv values into a custom file.
 * The file itself is named after a custom name and a unique long timestamp to make sure each file is unique.
 *
 * @author buechad1
 */
public class CsvWriter {

    private static final Logger LOGGER = getLogger(LogConfiguration.class.getCanonicalName());

    private static final String DIRECTORY_NAME = "TimeTableCreator";
    public static final String CSV_FILE_ENDING = ".csv";

    /**
     * Writes and creates a csv formatted file with the given csvString input. Files are named after given csvName and timestamp as long to make sure files stay unique.
     *
     * @param csvName   - the specified csv name could be an id of a class or similar.
     * @param csvString - the csvStrings line by line as array
     * @throws IOException - can throw an IOException or a {@link FileCreationException} which is also an io exception if file could not be created.
     */
    public void writeCsv(String csvName, String[] csvString) throws IOException {
        final File writableCsv = createWritableCsvFile(csvName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(writableCsv, true))) {
            for (String csvLine : csvString) {
                writer.append(csvLine);
                writer.newLine();
                LOGGER.finer(format("New line added to export file : <{0}>", csvLine));
            }
        }
    }

    private File createWritableCsvFile(String csvName) throws IOException {
        final File directory = new File(createDirectoryPath());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new FileCreationException("Directory could not be created.");
        }
        final String pathname = createFilePath(csvName);
        final File writableCsv = new File(pathname);
        if (!writableCsv.createNewFile()) {
            LOGGER.severe(format("File could not be created. Check access rights or if directory was created: {0}", writableCsv.getPath()));
            throw new FileCreationException("File could not be created.");
        }
        LOGGER.info(format("CSV File was created under {0}", writableCsv.getPath()));
        return writableCsv;
    }

    private String createFilePath(String csvName) {
        return new StringBuilder()
                .append(createDirectoryPath())
                .append(FileSystems.getDefault().getSeparator())
                .append(csvName)
                .append(Instant.now().toEpochMilli())
                .append(CSV_FILE_ENDING)
                .toString();
    }

    private String createDirectoryPath() {
        return new StringBuilder()
                .append(System.getProperty("user.home"))
                .append(FileSystems.getDefault().getSeparator())
                .append(DIRECTORY_NAME)
                .toString();
    }
}
