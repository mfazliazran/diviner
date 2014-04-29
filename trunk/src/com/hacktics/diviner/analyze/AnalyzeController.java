package com.hacktics.diviner.analyze;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import com.hacktics.diviner.analyze.Analyzer;
import com.hacktics.diviner.analyze.DatabaseAnalyzer;
import com.hacktics.diviner.analyze.GenericAnalyzer;
import com.hacktics.diviner.csrf.ResponseParser;
import com.hacktics.diviner.gui.Diviner;
import com.hacktics.diviner.gui.GuiUtils;
import com.hacktics.diviner.gui.ProgressOutput;
import com.hacktics.diviner.gui.scanwizard.Scenarios;


import java.beans.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

/**
 * 
 * @author Eran Tamari
 *
 */

public class AnalyzeController extends JPanel implements ActionListener, PropertyChangeListener {


	private static final long serialVersionUID = 1391206145422520160L;
	private JProgressBar progressBar;
	private JButton startButton;
	private JButton showResultsButton;
	private ProgressOutput taskOutput;
	private Task task;
	private static final String START = "Start";
	static final String SHOW_RES = "Show Results!";
	private JFrame frame;
	private Scenarios scenarios;
	private static final int COMPLETE = 100;
	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			while(!Thread.currentThread().isInterrupted()) {  				//Stop thread - usually when the user closes the progress frame
					doAnalyze();   //Perform once
					break;
			}
			return null;
		}

;		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			showResultsButton.setEnabled(true);
			setCursor(null); //turn off the wait cursor
			taskOutput.append("Done!\n");
			//  progressBar.setIndeterminate(false);
		}
	}


	public AnalyzeController(Scenarios scenarios){
		super(new BorderLayout());

		frame = new JFrame();
		frame.addWindowListener(new CloseListener());
		this.scenarios = scenarios;
		startButton = new JButton(START);
		startButton.addActionListener(this);

		showResultsButton = new JButton(SHOW_RES);
		showResultsButton.addActionListener(this);
		showResultsButton.setEnabled(false);

		progressBar = new JProgressBar(0, COMPLETE);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBorderPainted(true);

		taskOutput = new ProgressOutput(5, 20);
		taskOutput.setMargin(new Insets(5,5,5,5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(showResultsButton);
		panel.add(progressBar);
		panel.add(startButton);

		add(panel, BorderLayout.SOUTH);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

	//Handles the closing event of the Scan Wizard
	public class CloseListener extends WindowAdapter
	{
		//fix NullPointerException of progressBar
		public void windowClosing(WindowEvent e) 
		{
			progressBar.setIndeterminate(false);
            e.getWindow().setVisible(false);
        }
		
		public void windowClosed(WindowEvent e)
		{
			task.cancel(true);
		}
	}

	public JFrame getFrame()
	{
		return this.frame;
	}

	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed(ActionEvent evt) {

		String command = evt.getActionCommand();
		switch (command){

		case START:
			startButton.setEnabled(false);

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//Instances of javax.swing.SwingWorker are not reusuable, so
			//we create new instances as needed.
			task = new Task();
			task.addPropertyChangeListener(this);
			task.execute();
			break;

		case SHOW_RES:
			frame.dispose();
			break;
		}

	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {

	}

	public JTextArea getProgressOutput()
	{
		return taskOutput;
	}

	/**
	 * Create the GUI and show it. As with all GUI code, this must run
	 * on the event-dispatching thread.
	 */
	public void createAndShowGUI() {
		//Create and set up the window.

		frame.setMinimumSize(new Dimension(600,700));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setIconImage(GuiUtils.getGuiUtils().getDivinerIcon());
		//Create and set up the content pane.
		JComponent newContentPane = this;
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}



	//Runs the selected scenarios
	private void doAnalyze()
	{

		int progressBarInterval = (100 / scenarios.getEnabledScenariosCount());
		taskOutput.append("STARTING DIVINER ANALYSIS\n");
		//Set CSRF tokens from ZAP history
		ResponseParser.loadCsrfTokensFromHistory(); 
		taskOutput.append("Loading CSRF Tokens\n\n");

		for (int scenariosCounter = 0; scenariosCounter < scenarios.getNumOfScenarios(); scenariosCounter++)
		{
			for (int historyCounter = 0; historyCounter < scenarios.getNumOfHistiryModes(); historyCounter++)
			{
				if (scenarios.isScenarioSelectedAt(scenariosCounter) && scenarios.isHistorySelectedAt(historyCounter)) {
					runScenario(HISTORY_MODE.values()[historyCounter], SCENARIO_MODE.values()[scenariosCounter], progressBarInterval * (scenariosCounter + 1));
				}
			}
		}

		DatabaseAnalyzer dbAnalyzer = new DatabaseAnalyzer(taskOutput);
		dbAnalyzer.analyze();
		progressBar.setValue(COMPLETE);
		try {
			FileWriter fstream = new FileWriter(Diviner.getLogFile());
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(taskOutput.getText());
			out.close();
		}
		catch (Exception e) {	/* no log file was selected */}
	}

	private void runScenario(HISTORY_MODE histMode, SCENARIO_MODE scenario, int progressBarInterval) {
		advanceProgress(progressBarInterval);
		GenericAnalyzer analyzer = new Analyzer(histMode, scenario, taskOutput, scenarios.isVerifyMode()); 


		//Handle the token mode
		if (Plugins.isAppendMode()) {
			analyzer.analyze(true);
		}
		if (Plugins.isReplaceMode()) {
			analyzer.analyze(false);
		}
	}

	private void advanceProgress(int progressBarInterval) {
		ProgressValue interval = new ProgressValue(progressBarInterval);
		interval.start();
	}

	public JButton getShowResultsButton()
	{
		return showResultsButton;
	}
	//Increase progress bar value in a different thread
	private class ProgressValue extends Thread
	{
		int interval;
		Random random;
		public ProgressValue(int interval)
		{
			this.interval = interval;
			random = new Random();
		}

		public void run()
		{
			int nextValue = progressBar.getValue();
			while (progressBar.getValue() < interval)//Don't reach 100% - only when doAnalyze finished value is set to 100
			{
				nextValue = progressBar.getValue() + random.nextInt(2);
				if (nextValue < COMPLETE)
				{
					progressBar.setValue(nextValue);
				}
				else
				{
					break;	//thread will terminate just before 100%
				}
				try {
					Thread.sleep(random.nextInt(scenarios.getEnabledHistoryModeCount() * scenarios.getEnabledScenariosCount() * 2000));
				} catch (InterruptedException ignore) {}
			}
		}
	}
}

