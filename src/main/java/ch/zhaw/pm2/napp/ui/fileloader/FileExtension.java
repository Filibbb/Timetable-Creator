package ch.zhaw.pm2.napp.ui.fileloader;

/**
 * A simple file extension object containing a name and a description that can be used for the generic {@link FileLoaderComponentController} component
 *
 * @param fileExtensionName        - the name of the extension f.e. .csv or .json or similar
 * @param fileExtensionDescription - a description regarding the file-extension. This description is used within the file-selector dialog as helping text.
 */
public record FileExtension(String fileExtensionName, String fileExtensionDescription) {
}
