/**
 * 
 */
package com.hacktics.diviner.gui;

import java.awt.Image;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

//import com.hacktics.diviner.constants.Resources;

/**
 * The BaseJFrame abstract class should be inherited by all frames.
 * It defines the general structure, and provides common methods which are
 * required for common functions of all frames.
 *
 * @author Shay Chen
 * @since 1.0
 */
public abstract class BaseJFrame extends JFrame {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
    */
    private static final long serialVersionUID = 7572761440449688201L;


    //****************
    //* CONSTRUCTORS *
    //****************

    /**
     * Default Constructor.
     */
    public BaseJFrame() {
        super();
        this.initializeBaseJFrame(); //initial current frame
    } //end of default constructor


    /**
     * Custom Size Constructor.
     * @param width The width of the frame
     * @param height The height of the frame
     */
    public BaseJFrame(final int width, final int height) {
        super();
        this.setSize(width, height);
        this.setLocationToCenter();
        this.initializeBaseJFrame(); //initial current frame
    } //end of constructor


    //***********
    //* METHODS *
    //***********

    /**
     * Performs standard frame initialization activities on the JFrame,
     * such as common event binding and loading the application icon.
     */
    public final void initializeBaseJFrame() {
        setActive(true);
        setLookAndFeel();

        //set the frame icon
        try {
            /*Image img = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource(
                    Resources.HACKTICS_LOGO));
            this.setIconImage(img);*/
        } catch (Exception e) {
            System.out.println(e);
        }

        //add event: window closing button
        this.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(final WindowEvent e) {
                    setActive(false);
                    dispose();
                    if (getMainWindow()) {
                        System.exit(0);
                    }
                }
            }
        );
    } //end of method initialize


    /**
     * Sets the look and feel of the frame & components to "Nimbus".
     */
    private void setLookAndFeel() {
    	try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
    } //end of method


    /**
     * Sets the location of the frame to the center of the screen.
     */
    public final void setLocationToCenter() {
        Dimension screenResultion = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension jframeSize = this.getSize();
        this.setLocation(
            (screenResultion.width - jframeSize.width) / 2,
            (screenResultion.height - jframeSize.height) / 2);
    } //end of method


    //********************
    //* ABSTRACT METHODS *
    //********************

    /**
     * Changes the static activity status of the frame.
     * The static variable should be form-specific,
     * and can be used to prevent duplicate windows.
     * @param status The state of the class
     */
    public abstract void setActive(boolean status);

    /**
     * Changes the main window flag of the frame.
     * @param flag The type of the window (main/child)
     */
    public abstract void setMainWindow(boolean flag);

    /**
     * Returns the activity state of the frame.
     * @return true if active, false if inactive
     */
    public abstract boolean getActive();

    /**
     * Returns the main window flag of the frame.
     * @return true if main window, false if not
     */
    public abstract boolean getMainWindow();

    /**
     * Stops all the frame process.
     */
    protected abstract void stopProcesses();


    //*****************
    //* INNER CLASSES *
    //*****************

    /**
     * Inner class: event of close window and exit application.
     */
    class ActionExit implements ActionListener {
        /**
         * Closes the window and (potentially) exits the application.
         * @param e Action Event
         */
        public void actionPerformed(final ActionEvent e) {
            setActive(false);
            dispose();
            if (getMainWindow()) {
                System.exit(0);
            }
        }
    } //end of inner class action exit


    /**
     * Inner class: event of close window button.
     */
    class ActionClose implements ActionListener {
        /**
         * Closes the window and (potentially) exits the application.
         * @param e Action Event
         */
        public void actionPerformed(final ActionEvent e) {
            setActive(false);
            dispose();
        }
    } //end of inner class action close

} //end of class BaseJFrame

