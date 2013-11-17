package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteNode;

/**
 * 
 * @author Eran Tamari
 *
 */

public class GuiUtils {

	private JProgressBar progressBar;
	private JDialog dialog;
	private static GuiUtils guiUtils = new GuiUtils();
	private static String IMAGE_QUESTION = "/divinerIcons/About-48x48.png";
	private static final String IMAGE_SESSION = "/divinerIcons/Cookie-16x16.PNG";
	private static final String IMAGE_OUTPUT = "/divinerIcons/Output3-16x16.png";
	private static final String IMAGE_DB = "/divinerIcons/dB3-16x16.png";
	private static final String IMAGE_LOGO = "/divinerIcons/hacktics_logo.jpg";
	private static final String IMAGE_POST = "/divinerIcons/Post-48x48.png";
	private static final String IMAGE_GET = "/divinerIcons/Get-48x48.png";
	private static final String IMAGE_LOGO_ICON = "/divinerIcons/hacktics_icon.JPG";
	private static final String IMAGE_MAGNIFY_DISABLED = "/divinerIcons/magnify24.png";
	private static final String IMAGE_MAGNIFY_ENABLED = "/divinerIcons/magnifyEnabled24.png";
	private static final String IMAGE_EYE = "/divinerIcons/eye-blue24.png";
	private static final String IMAGE_STAR = "/divinerIcons/star24.png";
	private static final String IMAGE_AUTHENTICATED = "/divinerIcons/lock.png";
	private static final String IMAGE_UNAUTHENTICATED = "/divinerIcons/door.png";
	private static final String IMAGE_REPEATER = "/divinerIcons/injection.png";
	private static final String IMAGE_RIGHT = "/divinerIcons/right.png";
	private static final String IMAGE_LEFT = "/divinerIcons/left.png";
	private static final String IMAGE_ADD = "/divinerIcons/addPayload.png";
	private static final String IMAGE_Restore = "/divinerIcons/restoreDefault.png";
	private static final String IMAGE_SAVE = "/divinerIcons/save.png";
	private static final String IMAGE_LOAD = "/divinerIcons/load.png";
	
	public static GuiUtils getGuiUtils(){
		return guiUtils;
	}

	public void createPleaseWaitMessage(Dialog parent) {
		Progress progress = new Progress(parent);
		progress.start();
	}
	public void showPleaseWait() {
		dialog.setVisible(true);
	}
	private class Progress extends Thread	{
		
		private Dialog parent;
		
		public Progress(Dialog parent) {
			this.parent = parent;
		}
		public void run() {
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			dialog = new JDialog(parent);
			JPanel panel = new JPanel(new BorderLayout());
			JLabel head = new JLabel("<html><u>" + "Please Wait" + "</u></html>");
			head.setFont(new Font("Serif", Font.ITALIC, 18));
			panel.add(new JPanel(), BorderLayout.LINE_START);//place holder
			panel.add(head, BorderLayout.CENTER);
			panel.add(progressBar, BorderLayout.SOUTH);
			dialog.add(panel);
			dialog.setMinimumSize(new Dimension(200,100));
			dialog.setLocation(parent.getX() + (parent.getWidth() / 2), parent.getY() + (parent.getHeight() / 2));
		}
	}
	
	
	public void hideProgressBar() {
		dialog.setVisible(false);
	}
	
	public void showErrorDialog(Component comp, String message)
	{
		JOptionPane.showMessageDialog(comp, message, "ERROR", JOptionPane.ERROR_MESSAGE);
		// TODO :Get enlarged Alert image from Hila
	}
	public Image getDivinerIcon()
	{
		return Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMAGE_LOGO_ICON));
	}
	
	public Image getPayloadManagerIcon()
	{
		return Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMAGE_STAR));
	}
	
	public Image getMagnifyIcon()
	{
		return Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMAGE_MAGNIFY_ENABLED));
	}

	public ImageIcon getAddPayloadIcon()
	{
		return createImageIcon(IMAGE_ADD, "Add");	}
	
	public ImageIcon getLogoIcon()
	{
		return createImageIcon(IMAGE_LOGO, "Logo");
	}

	public ImageIcon getAuthenticatedIcon()
	{
		return createImageIcon(IMAGE_AUTHENTICATED, "Authentication Required");
	}

	public ImageIcon getUnauthenticatedIcon()
	{
		return createImageIcon(IMAGE_UNAUTHENTICATED, "Authticated not Required");
	}

	public ImageIcon getRightIcon()
	{
		return createImageIcon(IMAGE_RIGHT, "Right");
	}
	
	public ImageIcon getLeftIcon()
	{
		return createImageIcon(IMAGE_LEFT, "Left");
	}
	
	public ImageIcon getRepeaterIcon()
	{
		return createImageIcon(IMAGE_REPEATER, "Repeater");
	}
	
	public ImageIcon getSaveIcon()
	{
		return createImageIcon(IMAGE_SAVE, "Save");
	}
	
	public ImageIcon getLoadIcon()
	{
		return createImageIcon(IMAGE_LOAD, "Load");
	}
	
	public ImageIcon getRestoreIcon()
	{
		return createImageIcon(IMAGE_Restore, "Restore");
	}
	
	public ImageIcon getPostIcon()
	{
		return createImageIcon(IMAGE_POST, "Post");
	}

	public ImageIcon getGetIcon()
	{
		return createImageIcon(IMAGE_GET, "Get");
	}
	public ImageIcon getQuestionIcon()
	{
		return createImageIcon(IMAGE_QUESTION, "Question");
	}
	public ImageIcon getMagnifyEnabledIcon()
	{
		return createImageIcon(IMAGE_MAGNIFY_ENABLED, "Inspect");
	}

	public ImageIcon getMagnifyDisabledIcon()
	{
		return createImageIcon(IMAGE_MAGNIFY_DISABLED, "Inspect");
	}
	
	public ImageIcon getStarIcon()
	{
		return createImageIcon(IMAGE_STAR, "Payload Manager");
	}
	
	public Image getClairvoyanceIcon()
	{
		return Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMAGE_EYE));
	}
	
	public ImageIcon getEyeIcon()
	{
		return createImageIcon(IMAGE_EYE, "Show Code");
	}

	public ImageIcon getSessionIcon()
	{
		return createImageIcon(IMAGE_SESSION, "Session");
	}

	public ImageIcon getOutputIcon()
	{
		return createImageIcon(IMAGE_OUTPUT, "Output");
	}

	public ImageIcon getDBIcon()
	{
		return createImageIcon(IMAGE_DB, "Database");
	}

	public ImageIcon createImageIcon(String path,String description) {

		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
			
		} 
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public ArrayList<String> getDomainsFromZap() {
		ArrayList<String> result = new ArrayList<String>();
		Session session = Model.getSingleton().getSession();
		SiteNode root = (SiteNode) session.getSiteTree().getRoot();
		Enumeration<SiteNode> en = root.children();
		while (en.hasMoreElements()) {
			String site = en.nextElement().getNodeName();
			if (site.indexOf("//") >= 0) {
				site = site.substring(site.indexOf("//") + 2);
			}
			if (site.indexOf(":") >= 0) {
				site = site.substring(0, site.indexOf(":"));
			}
			result.add(site);
		}

		return result;
	}
}
