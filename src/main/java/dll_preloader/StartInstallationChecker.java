package dll_preloader;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartInstallationChecker {

	private static final Logger LOGGER = LogManager.getLogger(StartInstallationChecker.class);

	public interface Callback {
		void CallbackFunction();
	}

	public static void StartChecker(Callback successCallback, ProjectsNames proj) {
		StartChecker(successCallback, null, proj);
	}

	/**
	 * Checks for installer location using the file located in
	 * user.home\.javatools\installer.data successCallback gets called when every
	 * operation gets executed successfully, the app knows the location of the
	 * installer and can be opened. failureCallback gets called when some folders or
	 * files cannot be found.
	 * 
	 * @param successCallback
	 * @param failureCallback
	 */
	public static void StartChecker(Callback successCallback, Callback failureCallback, ProjectsNames proj) {
		String libPath = "";
		String installerConfigPath = System.getProperty("user.home").concat("\\.javatools\\installer.data");
		String jarPath = System.getProperty("user.dir");

		LOGGER.info("[Init - Phase Start] Loading SWT Libraries");
		LOGGER.info("[Init - Phase 1] Searching Installer Folder");
		LOGGER.info("> Searching for Installer Path in Home Config File...");
		LOGGER.info("> User Home is: " + System.getProperty("user.home"));
		LOGGER.info("> Searching Path in: " + installerConfigPath);
		File f = new File(installerConfigPath);
		if (f.exists() && !f.isDirectory()) {
			LOGGER.info("> Installer Path Found");
			try (Stream<String> lines = Files.lines(f.toPath(), Charset.defaultCharset())) {
				libPath = lines.findFirst().get();
			} catch (Exception e) {
				LOGGER.error("[Exception] " + e.getLocalizedMessage() + " \nStacktrace: " + e.getStackTrace());
			}
			LOGGER.info("[Init - Phase 2] Installer folder found at \"" + libPath + "\"");

			LOGGER.info("> Installation Detected.");
			LOGGER.info("> Libs Folder found at \"" + libPath.concat("\\libs") + "\"");
			System.setProperty("swt.library.path", libPath.concat("\\libs"));
			LOGGER.info("> Libs Folder set.");

			// path successo
			successCallback.CallbackFunction();
		} else {
			LOGGER.info("[Init - Phase 2] File not found, Libs will be searched for in User Home.");
			// JOptionPane.showMessageDialog(null, "Impossible to get Libraries");
			VersionInstallAlert frame = new VersionInstallAlert(proj);
			frame.setVisible(true);

			if (failureCallback != null) {
				failureCallback.CallbackFunction();
			}
		}
		LOGGER.info("[Init - Phase Final] Starting Application.");
	}

}
