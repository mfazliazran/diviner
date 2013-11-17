package com.hacktics.diviner.gui.controllers;

import java.beans.ConstructorProperties;

import javax.swing.Action;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class JStatefulButton extends JButton {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 4461835677296733857L;
	private enum State {
		Pushed,
		Released
	}
	
	private State m_PushedState = State.Released;
	
	private void handleState()
	{
		if (m_PushedState == State.Pushed) {
			m_PushedState = State.Released;
			
//			model.se
		}
		else if (m_PushedState == State.Released) {
			m_PushedState = State.Pushed;
		}
	}

	/**
     * Creates a button with no set text or icon.
     */
    public JStatefulButton() {
        super(null, null);
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon  the Icon image to display on the button
     */
    public JStatefulButton(Icon icon) {
        super(null, icon);
    }

    /**
     * Creates a button with text.
     *
     * @param text  the text of the button
     */
    @ConstructorProperties({"text"})
    public JStatefulButton(String text) {
        super(text, null);
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     * @since 1.3
     */
    public JStatefulButton(Action a) {
        super();
        setAction(a);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public JStatefulButton(String text, Icon icon) {
        // Create the model
        setModel(new DefaultButtonModel());

        // initialize
        init(text, icon);
    }
	
	
}
