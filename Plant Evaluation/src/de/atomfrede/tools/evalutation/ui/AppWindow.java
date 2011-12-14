/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	AppWindow.java is part of Plant Evaluation.
 *
 *  Plant Evaluation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plant Evaluation is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Plant Evaluation.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.atomfrede.tools.evalutation.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.options.ui.OptionsDialog;
import de.atomfrede.tools.evalutation.ui.about.AboutDialog;
import de.atomfrede.tools.evalutation.ui.res.Messages;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.JarUtil;

/**
 * The central frame that contains all ui elements and the main class that
 * launches the application.
 */
public class AppWindow {

	public static JFrame _frame;
	private JFrame frame;
	private MainPanel mainPanel;

	private final Log log = LogFactory.getLog(AppWindow.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					JarUtil.startFileLogging();
					AppWindow window = new AppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static void setLookAndFeel() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	/**
	 * Create the application.
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		log.info("Plant Evaluator started");
		frame = new JFrame();

		frame.setIconImage(Icons.IC_APPLICATION_X_LARGE.getImage());

		AppWindow._frame = frame;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (reallyExit() == JOptionPane.YES_OPTION) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		});

		frame.setTitle(Messages.getString("AppWindow.0") + " " + Messages.getString("AppWindow.version.code")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		frame.setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Messages.getString("AppWindow.1")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem(Messages.getString("AppWindow.3")); //$NON-NLS-1$
		mntmExit.setIcon(Icons.IC_LOGOUT_SMALL);
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

			}
		});
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu(Messages.getString("AppWindow.4")); //$NON-NLS-1$
		menuBar.add(mnEdit);

		JMenuItem mntmOptions = new JMenuItem(Messages.getString("AppWindow.5")); //$NON-NLS-1$
		mntmOptions.setIcon(Icons.IC_PREFERENCES_SYSTEM_SMALL);
		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new OptionsDialog(frame);
			}
		});
		mnEdit.add(mntmOptions);

		JMenu mnTools = new JMenu(Messages.getString("AppWindow.mnTools.text")); //$NON-NLS-1$
		menuBar.add(mnTools);

		JMenuItem mntmPostprocessing = new JMenuItem(Messages.getString("AppWindow.mntmPostprocessing.text")); //$NON-NLS-1$
		mnTools.add(mntmPostprocessing);

		JMenu mnHelp = new JMenu(Messages.getString("AppWindow.6")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem(Messages.getString("AppWindow.7")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new AboutDialog(frame);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmAbout);

		createContent();

	}

	/**
	 * Display a simple dialog, so the user does not close the application by
	 * accident
	 * 
	 * @return
	 */
	private int reallyExit() {
		Object[] options = { Messages.getString("AppWindow.11"), Messages.getString("AppWindow.12") }; //$NON-NLS-1$ //$NON-NLS-2$

		int result = JOptionPane.showOptionDialog(frame, Messages.getString("AppWindow.13"), //$NON-NLS-1$
				Messages.getString("AppWindow.14"), JOptionPane.YES_NO_OPTION, //$NON-NLS-1$
				JOptionPane.WARNING_MESSAGE, Icons.IC_DIALOG_WARNING_LARGE, options, options[1]);

		return result;
	}

	/**
	 * Fill the frame with content
	 */
	private void createContent() {
		frame.getContentPane().setLayout(new JideBorderLayout());

		FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		mainPanel = new MainPanel(frame);
		builder.append(mainPanel);

		frame.getContentPane().add(new JScrollPane(builder.getPanel()), BorderLayout.CENTER);

		Rectangle bounds = new Rectangle(builder.getPanel().getPreferredSize());
		bounds.grow(20, 35);
		frame.setResizable(true);

		frame.setBounds(bounds);
		frame.setLocationRelativeTo(null);

	}
}
