package dll_preloader;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class VersionDownloader extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LogManager.getLogger(StartInstallationChecker.class);
	
	private JPanel contentPane;
	
 	//private JProgressBar progressBar = null;
	
	private JLabel lblNewLabel;
	private JProgressBar progressBar;
	
	private void createEmptyFile(String filePath) {
        File file = new File(filePath);
        try {
            // Create the file if it doesn't already exist
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }
	
	private void extractZipFile(String zipFilePath, String destDir) throws IOException {
        File destDirectory = new File(destDir);
        
        // Create the destination directory if it doesn't exist
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        // Open the ZIP file
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            
            // Loop through each entry in the ZIP file
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // Create a file for each entry
                File outputFile = new File(destDir, entry.getName());

                // Create directories if necessary
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // Create output file and write data to it
                    File parentDir = outputFile.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    // Extract the file content
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }
	
	public void createGUI() 
	{
		try {
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String downloadUrl, String downloadLocation, String fileName)
	{
		URL url = null;
		try {
			url = new URL(downloadUrl);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage());
		}
        HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage());
		}
        
        // Get the total file size
        int contentLength = httpURLConnection.getContentLength();
        
        // Open InputStream and OutputStream
        File nestedDirectory = new File(downloadLocation);
        if (nestedDirectory.mkdirs()) {
            LOGGER.info("Parent directories and the folder created successfully!");
        } else {
        	LOGGER.info("Failed to create directories.");
        }
        try (InputStream inputStream = httpURLConnection.getInputStream();
        		
            FileOutputStream outputStream = new FileOutputStream(downloadLocation + fileName)) {
        	
            byte[] buffer = new byte[4096];  // 4KB buffer
            int bytesRead;
            long totalBytesRead = 0;
            
            // Read from the input stream and write to the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                // Calculate the progress percentage
                int progress = (int) (totalBytesRead * 100 / contentLength);
                LOGGER.info("\rDownloading: " + progress + "%");
                progressBar.setValue(progress);
            }
        } catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage());
		} finally {
            httpURLConnection.disconnect();
        }

        LOGGER.info("\nDownload completed!");
	}
	
	public void launchApp(ProjectsNames proj)
	{
		String appName = proj == ProjectsNames.TSE ? ProjectsReferences.TSE_APPNAME : ProjectsReferences.CB_APPNAME;
		
		try {
        	File f = new File(".\\jre");
			if(f.exists() && f.isDirectory()) {
				Runtime.getRuntime().exec(".\\jre\\bin\\java.exe -jar .\\oldversion\\" + appName);
			}
			else
			{
				Runtime.getRuntime().exec("java -jar .\\oldversion\\" + appName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		}
	}
	
	public void downloadPreviousVersion(ProjectsNames proj)
	{
		// Code to execute after the JFrame loadsURL url = null;
		
		if (new File(".\\oldversion\\installation.ok").exists())
		{
			launchApp(proj);
	        
	        System.exit(NORMAL);
		}
		else
		{
			downloadFile(proj == ProjectsNames.TSE ? ProjectsReferences.TSE_OLDVERSION_ONLYAPP : ProjectsReferences.CB_OLDVERSION_ONLYAPP, ".\\oldversion\\temp\\", "prev.zip");
			
	        lblNewLabel.setText("Extracting zip...");
	        
	        try {
				extractZipFile(".\\oldversion\\temp\\prev.zip", ".\\oldversion\\");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			}
	        
	        lblNewLabel.setText("Done, Launching Old Version...");
	        
	        createEmptyFile(".\\oldversion\\installation.ok");
	        
	        launchApp(proj);
	        
	        System.exit(NORMAL);
		}
		
        
	}
	
	public void downaloadExternalInstaller(ProjectsNames proj)
	{
		
		lblNewLabel.setText("Downloading Update Assistant...");
		
		String pathDownload = System.getProperty("user.home").concat("\\.javatools\\installer\\");
		
		downloadFile(proj == ProjectsNames.TSE ? ProjectsReferences.TSE_NEWVERSION_UPGRADE_ASSISTANT : ProjectsReferences.CB_NEWVERSION_UPGRADE_ASSISTANT, pathDownload, "external_upgrade.zip");
		
        lblNewLabel.setText("Extracting zip...");
        
        try {
			extractZipFile( pathDownload.concat("\\external_upgrade.zip"), pathDownload);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		}
        
        lblNewLabel.setText("Done, Opening folder...");
        LOGGER.info("Opening " + pathDownload);
        JOptionPane.showMessageDialog(null, "Open \"Update Assistant.exe\" as Administrator and select the tool installer to start the upgrade.");
        Desktop desktop = Desktop.getDesktop();
        try 
        {
        	desktop.open(new File(pathDownload));
        }
        catch(Exception e)
        {
        	LOGGER.error("Impossible to open directory");
        	LOGGER.error(e.getMessage());
        	LOGGER.error(e.getStackTrace());
        	JOptionPane.showMessageDialog(null, "Failed to open directory.\nThe Update Assistant is saved in\n" + pathDownload);
        }
        
        System.exit(NORMAL);
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * Create the frame.
	 */
	public VersionDownloader() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		lblNewLabel = new JLabel("Downloading...");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, -32, SpringLayout.SOUTH, contentPane);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		progressBar = new JProgressBar();
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, progressBar);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, progressBar);
		sl_contentPane.putConstraint(SpringLayout.NORTH, progressBar, 31, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, progressBar, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, progressBar, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, contentPane);
		contentPane.add(progressBar);

	}

	
	public void launchOldVersionDownload(ProjectsNames projToDownload)
	{
		this.setVisible(true);
		
		new Thread(new Runnable() {
            @Override
            public void run() {
            	
		        downloadPreviousVersion(projToDownload);
            	
            }
        }).start();
	}
	
	public void launchExternalUpdaterDownload(ProjectsNames projToDownload)
	{
		this.setVisible(true);
		
		new Thread(new Runnable() {
            @Override
            public void run() {
            	
            	downaloadExternalInstaller(projToDownload);
            	
            }
        }).start();
	}
	
	
}
