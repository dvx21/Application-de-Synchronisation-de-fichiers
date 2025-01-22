import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Graphical user interface for the application.
 */
public class GUI {

	/**
	 * Creates a new instance of the GUI.
	 */
	public GUI() {

		// Set application name in the taskbar depending on the operating system
		if (System.getProperty("os.name").contains("Windows")) {
			System.setProperty("sun.java2d.taskbarName", AppConfig.INSTANCE.getAppName());
		} else if (System.getProperty("os.name").contains("Linux")) {
			System.setProperty("awt.appname", AppConfig.INSTANCE.getAppName());
		} else if (System.getProperty("os.name").equals("Mac OS X")) {
			System.setProperty("apple.awt.application.name", AppConfig.INSTANCE.getAppName());
		}

		// Create graphical components for source and target folders
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JLabel labelSource = new JLabel("Source folder:");
		JLabel labelTarget = new JLabel("Target folder:");
		JLabel labelIgnore = new JLabel("Ignore files types (separator: ', '):");
		JLabel labelIgnoredList = new JLabel(History.getIgnoredList().toString());
		JTextField sourceField = new JTextField(20);
		JTextField targetField = new JTextField(20);
		JTextField ignoreField = new JTextField(20);
		JButton sourceButton = new JButton("Browse...");
		JButton targetButton = new JButton("Browse...");
		JButton startButton = new JButton("Synchroniser");

		// Set component alignments
		labelSource.setHorizontalAlignment(SwingConstants.CENTER);
		labelTarget.setHorizontalAlignment(SwingConstants.CENTER);
		labelIgnore.setHorizontalAlignment(SwingConstants.CENTER);
		labelIgnoredList.setHorizontalAlignment(SwingConstants.CENTER);
		startButton.setPreferredSize(new Dimension(150, 30));
		sourceField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		targetField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		ignoreField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		// Add components to the input panel
		sourceField.setText(History.getConfig().getString("source"));
		targetField.setText(History.getConfig().getString("target"));
		ignoreField.setText(History.getConfig().getString("ignore"));
		
		sourceButton.setPreferredSize(new Dimension(200, 15));
		targetButton.setPreferredSize(new Dimension(200, 15));

		inputPanel.add(labelSource);
		inputPanel.add(sourceField);
		inputPanel.add(sourceButton);
		inputPanel.add(labelTarget);
		inputPanel.add(targetField);
		inputPanel.add(targetButton);
		inputPanel.add(labelIgnore);
		inputPanel.add(ignoreField);
		inputPanel.add(labelIgnoredList);

		sourceField.setBorder(BorderFactory.createCompoundBorder(sourceField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		targetField.setBorder(BorderFactory.createCompoundBorder(targetField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		ignoreField.setBorder(BorderFactory.createCompoundBorder(ignoreField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// Adding graphical components to the input panel
		sourceField.setText(History.getConfig().getString("source"));
		targetField.setText(History.getConfig().getString("target"));
		ignoreField.setText(History.getConfig().getString("ignore"));

		inputPanel.add(labelSource);
		inputPanel.add(sourceField);
		inputPanel.add(sourceButton);
		inputPanel.add(labelTarget);
		inputPanel.add(targetField);
		inputPanel.add(targetButton);
		inputPanel.add(labelIgnore);
		inputPanel.add(ignoreField);
		inputPanel.add(labelIgnoredList);

		sourceField.setBorder(BorderFactory.createCompoundBorder(sourceField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		targetField.setBorder(BorderFactory.createCompoundBorder(targetField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		ignoreField.setBorder(BorderFactory.createCompoundBorder(ignoreField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// Adding graphical components to the button panel
		buttonPanel.add(startButton);

		// Adding panels to the main panel
		mainPanel.add(inputPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		/**
		 * Defines actions for source folder selection buttons.
		 */
		sourceButton.addActionListener(new ActionListener() {

		    /**
		     * Invoked when source button is clicked.
		     * Opens a file chooser dialog to select a directory and updates sourceField.
		     * @param e the action event
		     */
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int result = chooser.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            sourceField.setText(chooser.getSelectedFile().getAbsolutePath());
		        }
		    }

		});

		/**
		 * Defines actions for target folder selection buttons.
		 */
		targetButton.addActionListener(new ActionListener() {

		    /**
		     * Invoked when target button is clicked.
		     * Opens a file chooser dialog to select a directory and updates targetField.
		     * @param e the action event
		     */
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int result = chooser.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
		        }
		    }

		});

		/**
		 * Document listener for the ignoreField.
		 * Updates the ignoredList label to display the current ignored values.
		 */
		ignoreField.getDocument().addDocumentListener(new DocumentListener() {

		    /**
		     * Invoked when the text in the document has changed but not the document's length.
		     * Does nothing in this implementation.
		     * @param e the document event
		     */
		    public void changedUpdate(DocumentEvent e) {
		        
		    }

		    /**
		     * Invoked when an insertion is made into the document.
		     * Updates the ignoredList label with the new ignored values.
		     * @param e the document event
		     */
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        labelIgnoredList.setText(Arrays.asList(ignoreField.getText().split(", ")).toString());
		    }

		    /**
		     * Invoked when a removal is made from the document.
		     * Updates the ignoredList label with the new ignored values.
		     * @param e the document event
		     */
		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        labelIgnoredList.setText(Arrays.asList(ignoreField.getText().split(", ")).toString());
		    }
		});


		/**
		 * ActionListener for the startButton. It checks if the source and target folders are valid
		 * and if the process is already running, it stops it. Otherwise, it sets the source path, target path,
		 * ignored list, and starts the synchronization process.
		 */
		startButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Create two File objects to test the labels
		        File sourceFolder = new File(sourceField.getText());
		        File targetFolder = new File(targetField.getText());

		        if (History.getProcessState()) {
		            // If process is running, stop it and update the button text
		            History.stopProcess();
		            startButton.setText("Synchronize");
		        } else {
		            // Check if sourceField and targetField are empty
		            if (sourceField.getText().isEmpty() || targetField.getText().isEmpty()) {
		                // Show an error message
		                JOptionPane.showMessageDialog(null,
		                        "The Source folder and Target folder fields are required", "Error",
		                        JOptionPane.ERROR_MESSAGE);

		                // Set the empty fields to red
		                if (sourceField.getText().isEmpty()) {
		                    sourceField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		                    sourceField.setBorder(BorderFactory.createCompoundBorder(sourceField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }

		                if (targetField.getText().isEmpty()) {
		                    targetField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		                    targetField.setBorder(BorderFactory.createCompoundBorder(targetField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }
		            } else if (!sourceFolder.exists() || !targetFolder.exists()) {
		                // Show an error message if source or target folder doesn't exist
		                if (!sourceFolder.exists()) {
		                    JOptionPane.showMessageDialog(null,
		                            "Folder '" + sourceField.getText() + "' not found!", "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                    sourceField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		                    sourceField.setBorder(BorderFactory.createCompoundBorder(sourceField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }

		                if (!targetFolder.exists()) {
		                    JOptionPane.showMessageDialog(null,
		                            "Folder '" + targetField.getText() + "' not found!", "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                    targetField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		                    targetField.setBorder(BorderFactory.createCompoundBorder(targetField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }
		            } else {
		                // If the fields are not empty and the folders exist, set the borders to white
		                if (!sourceField.getText().isEmpty()) {
		                    sourceField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		                    sourceField.setBorder(BorderFactory.createCompoundBorder(sourceField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }
		                if (!targetField.getText().isEmpty()) {
		                    targetField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		                    targetField.setBorder(BorderFactory.createCompoundBorder(targetField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		                }

		                // Set the source path, target path, ignored list, and start the process
		                History.setSourcePath(new File(sourceField.getText()).getAbsolutePath() + File.separator);
		                History.setTargetPath(new File(targetField.getText()).getAbsolutePath() + File.separator);
		                History.setIgnoredList(Arrays.asList(ignoreField.getText().split(", ")));
		                History.startProcess();
		                ignoreField.setText(History.getConfig().getString("ignore"));
		                startButton.setText("Stop");
		            }
		        }
		    }
		});


		// Add the input and button panels to the main panel
		mainPanel.add(inputPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Add top margin
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// Create the window
		JFrame frame = new JFrame(AppConfig.INSTANCE.getAppName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setMaximumSize(new Dimension(200, 800));
	}
}
