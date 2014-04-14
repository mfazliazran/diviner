
/*

 */

package org.zaproxy.zap.extension.diviner;


import com.hacktics.diviner.gui.Diviner;

import java.net.MalformedURLException;
import java.net.URL;
//import java.util.Locale;
//import java.util.ResourceBundle;


import org.apache.log4j.Logger;

import javax.swing.JMenuItem;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.spider.Spider;

/*
 * This class is defines the extension.
 */
public class DivinerExtension extends ExtensionAdaptor {

    private JMenuItem mnuDiviner = null;
   // private ResourceBundle messages = null;

 
    /** The Constant log. */
	private static final Logger log = Logger.getLogger(DivinerExtension.class);
	
    public DivinerExtension() {
        super();
        initialize();
    }

    /**
     * @param name
     */
    public DivinerExtension(String name) {
        super(name);
    }

        /**
         * This method initializes this
         * 
         * @return void
         */
        private void initialize() {
        this.setName("DivinerExtention");
        // Load extension specific language files - these are held in the extension jar
         // messages = ResourceBundle.getBundle("com.hacktics.diviner.DivinerExtention.Messages",Locale.ENGLISH);//  Constant.getLocale());
        
        }
        
        public void hook(ExtensionHook extensionHook) {
            super.hook(extensionHook);
            
            if (getView() != null) {
                // Register our top menu item, as long as we're not running as a daemon
                // Use one of the other methods to add to a different menu list
                extensionHook.getHookMenu().addToolsMenuItem(getmnuDiviner());
            }

        }

        private JMenuItem getmnuDiviner() {
        if (mnuDiviner == null) {
                mnuDiviner = new JMenuItem();
                mnuDiviner.setText("Launch Diviner");

                mnuDiviner.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                        // This is where you do what you want to do.
                        // In this case we'll just show a popup message.
                        //View.getSingleton().showMessageDialog(getMessageString("Diviner"));
                	try {
                		Diviner diviner = new com.hacktics.diviner.gui.Diviner();
                	}
                	catch (Exception Ex) {
                		log.error("There was an error while loading Diviner: ", Ex);
                	}
                	
                }
            });
        }
        return mnuDiviner;
    }

        public String getMessageString (String key) {
    	    ////diviner
    	     //   Diviner diviner = new com.hacktics.diviner.gui.Diviner();

    	        return key;//return messages.getString(key);
        }
        @Override
        public String getAuthor() {
                return "Hacktics";
        }

        @Override
        public String getDescription() {
                return "Diviner by Hacktics";
        }

        @Override
        public URL getURL() {
                try {
                        return new URL(Constant.ZAP_EXTENSIONS_PAGE);
                } catch (MalformedURLException e) {
                        return null;
                }
        }
}