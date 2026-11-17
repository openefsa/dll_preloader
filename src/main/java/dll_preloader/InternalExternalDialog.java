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


public class InternalExternalDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private InternalExternalDialog _frame;
	private ProjectsNames projectToInstall;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//InternalExternalDialog frame = new InternalExternalDialog(pr);
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
	

	public InternalExternalDialog(ProjectsNames proj) {
		projectToInstall = proj;
		setTitle("Question");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Select your installed version");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, -15, SpringLayout.EAST, contentPane);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, panel);
		sl_contentPane.putConstraint(SpringLayout.NORTH, panel, -45, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panel, 15, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, contentPane);
		contentPane.add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JButton externalButton = new JButton("External");
		externalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VersionDownloader frame = new VersionDownloader();
				frame.launchExternalUpdaterDownload(projectToInstall);
				//frame.createGUI();
				dispose();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, externalButton, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, externalButton, 10, SpringLayout.WEST, panel);
		panel.add(externalButton);
		
		JButton btnInternal = new JButton("EFSA");
		btnInternal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "To install the internal version you must download the new update on Company Portal.");
				dispose();
				System.exit(NORMAL);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnInternal, 0, SpringLayout.NORTH, externalButton);
		sl_panel.putConstraint(SpringLayout.EAST, btnInternal, -10, SpringLayout.EAST, panel);
		panel.add(btnInternal);
	}
}
