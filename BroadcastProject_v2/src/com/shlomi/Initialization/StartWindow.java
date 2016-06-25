package com.shlomi.Initialization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.shlomi.StartNewClient;
import com.shlomi.StartNewServer;

public final class StartWindow {

	private JFrame frame;
	private JTextField textField;
	
	public StartWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame = new JFrame();
		frame.setBounds(100, 100, 198, 256);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblServer = new JLabel("Enter server IP:");
		lblServer.setBounds(21, 90, 146, 20);
		frame.getContentPane().add(lblServer);
		
		textField = new JTextField();
		textField.setBounds(21, 114, 146, 26);
		frame.getContentPane().add(textField);
		
		JRadioButton radioServer = new JRadioButton("New server");
		radioServer.setBounds(21, 12, 155, 29);
		frame.getContentPane().add(radioServer);
		
		JRadioButton radioClient = new JRadioButton("Join server");
		radioClient.setBounds(21, 49, 209, 29);
		frame.getContentPane().add(radioClient);
		
		ButtonGroup group = new ButtonGroup();
		group.add(radioClient);
		group.add(radioServer);
		
		JButton btnNewButton = new JButton("Go");
		
		ActionListener goActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(radioServer.isSelected()) {
					try {
						
						if(textField.getText().length() <= 0) {
							JOptionPane.showMessageDialog(frame, "You must enter IP.");
							return;
						}
						
						startServer(textField.getText());
						frame.setVisible(false);
						frame.dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Error starting server. Check again.");
					}
				}//if
				else {
					try {
						
						if(textField.getText().length() <= 0) {
							JOptionPane.showMessageDialog(frame, "You must enter IP.");
							return;
						}
						
						new StartNewClient(textField.getText());
						while(StartNewClient.IS_RUNNIG == false) {
							//wait untill client finishes
						}
						frame.setVisible(false);
						frame.dispose();
					} catch (IOException e1) {
						//Stop all threads if failed to initialize client.
						JOptionPane.showMessageDialog(frame, "Error starting client. Try again.");
						StartNewClient.stopThreads();
					}
				}//else
			}
				
		};
		
		btnNewButton.addActionListener(goActionListener);
		
		btnNewButton.setBounds(21, 156, 115, 29);
		frame.getContentPane().add(btnNewButton);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		textField.addActionListener(goActionListener);
		textField.requestFocus();
		
		radioClient.setSelected(true);
	}

	private void startServer(String bindToThisAddress) throws IOException {
		new StartNewServer(bindToThisAddress);
	}
	
	public void echo(String msg) {
		System.out.println(msg);
	}
}
