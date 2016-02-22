package com.rjw.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SimpleDialog extends JDialog {

	public SimpleDialog(MapEditor mapEditor, String title, String message) {

		super();

		Box b = Box.createVerticalBox();
		b.add(Box.createGlue());
		b.add(new JLabel(message));
		b.add(Box.createGlue());
		getContentPane().add(b, "Center");

		JPanel p2 = new JPanel();
		JButton ok = new JButton("Ok");
		p2.add(ok);
		getContentPane().add(p2, "South");

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});

		setSize(250, 150);
		
	}
	
}//SimpleAboutDialog