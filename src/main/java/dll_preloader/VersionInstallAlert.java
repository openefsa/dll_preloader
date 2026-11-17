package dll_preloader;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;


public class VersionInstallAlert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VersionInstallAlert _frame;
	
	private ProjectsNames projectToInstall;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//VersionInstallAlert frame = new VersionInstallAlert();
					//frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	

	public VersionInstallAlert(ProjectsNames proj) {
		projectToInstall = proj;
		setTitle("Warning");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblNewLabel = new JLabel("The version installed requires other steps to be performed in order to become functional.\r\n");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, -15, SpringLayout.EAST, contentPane);
		contentPane.add(lblNewLabel);
		
		JLabel lblAnAutomaticProcedure = new JLabel("An Automatic procedure can be applied to resolve the issue (Admin Rights required)\r\n");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblAnAutomaticProcedure, 1, SpringLayout.SOUTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblAnAutomaticProcedure, 0, SpringLayout.WEST, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblAnAutomaticProcedure, -15, SpringLayout.EAST, contentPane);
		lblAnAutomaticProcedure.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblAnAutomaticProcedure);
		
		JLabel lblYouCanChoose = new JLabel("You can choose to ignore the steps for now and continue using last version");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblYouCanChoose, 1, SpringLayout.SOUTH, lblAnAutomaticProcedure);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblYouCanChoose, 0, SpringLayout.WEST, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblYouCanChoose, -15, SpringLayout.EAST, contentPane);
		lblYouCanChoose.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblYouCanChoose);
		
		JPanel panel = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, panel);
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel, -45, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panel, 15, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, contentPane);
		contentPane.add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JButton btnNewButton = new JButton("Auto Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InternalExternalDialog frame = new InternalExternalDialog(proj);
				frame.setVisible(true);
				//frame.createGUI();
				dispose();
				//try {
					/*
					 String command;
					File f = new File(".\\jre");
					if(f.exists() && f.isDirectory()) {
						command = "Start-Process \".\\jre\\bin\\java.exe\" -Verb runAs";
					}
					else
					{
						command = "Start-Process \"java -jar .\\tse-tool.jar\" -Verb runAs";
					}
					ProcessBuilder processBuilder = new ProcessBuilder("powershell", command);
		            Process process = processBuilder.start();
					 */
		        /*
					File f = new File("%ProgramFiles%");
					if (f.canWrite()) {
						JOptionPane.showMessageDialog(null, "App is Admin"); 
					}
					else
					{
						JOptionPane.showMessageDialog(null, "App is not Admin");
					}
					*/
				/*} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getLocalizedMessage());
				}*/
			}
		});
		panel.add(btnNewButton);
		
		JButton btnOpen = new JButton("Do not update");
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton, 0, SpringLayout.NORTH, btnOpen);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton, -6, SpringLayout.WEST, btnOpen);
		sl_panel.putConstraint(SpringLayout.WEST, btnOpen, -180, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOpen, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnOpen, -10, SpringLayout.EAST, panel);
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VersionDownloader frame = new VersionDownloader();
				frame.launchOldVersionDownload(proj);
				//frame.createGUI();
				dispose();
			}
		});
		panel.add(btnOpen);
		
		JButton btnShowInstructions = new JButton("How to install");
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton, 31, SpringLayout.EAST, btnShowInstructions);
		sl_panel.putConstraint(SpringLayout.EAST, btnShowInstructions, 162, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnShowInstructions, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnShowInstructions, 10, SpringLayout.WEST, panel);
		btnShowInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				    
						try {
							Desktop.getDesktop().browse(projectToInstall == ProjectsNames.TSE ? new URI(ProjectsReferences.TSE_WIKI_INSTRUCTIONS) : new URI(ProjectsReferences.CB_WIKI_INSTRUCTIONS));
						} catch (Exception errorlaunch) {
							// TODO Auto-generated catch block
							errorlaunch.printStackTrace();
						} 
					
				}
			}
		});
		panel.add(btnShowInstructions);
	}
}
