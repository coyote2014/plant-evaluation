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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.ui.about.AboutDialog;
import de.atomfrede.tools.evalutation.ui.res.Messages;

public class AppWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
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
		frame = new JFrame();
		// frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(Messages.getString("AppWindow.0") + " " + Messages.getString("AppWindow.version.code")); //$NON-NLS-1$

		frame.setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Messages.getString("AppWindow.1")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmEvaluate = new JMenuItem(
				Messages.getString("AppWindow.2")); //$NON-NLS-1$
		mnFile.add(mntmEvaluate);

		JMenuItem mntmExit = new JMenuItem(Messages.getString("AppWindow.3")); //$NON-NLS-1$
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu(Messages.getString("AppWindow.4")); //$NON-NLS-1$
		menuBar.add(mnEdit);

		JMenuItem mntmOptions = new JMenuItem(Messages.getString("AppWindow.5")); //$NON-NLS-1$
		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new OptionsDialog(frame);
			}
		});
		mnEdit.add(mntmOptions);

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

		test();

	}

	private void test() {
		frame.getContentPane().setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(new MainPanel(frame));

		frame.getContentPane().add(new JScrollPane(builder.getPanel()),
				BorderLayout.CENTER);

		Rectangle bounds = new Rectangle(builder.getPanel().getPreferredSize());
		bounds.grow(20, 35);
		frame.setResizable(true);

		frame.setBounds(bounds);
		frame.setLocationRelativeTo(null);

	}
}
