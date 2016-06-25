package com.shlomi.Initialization.OtherWindows;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.shlomi.StartNewServer;

public class ServerShutdownWindow {

	private JFrame frmShutdownServer;

	/**
	 * Create the application.
	 */
	public ServerShutdownWindow(String broadcastServerInfo, String receiverServerInfo, String senderServerInfo) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		initialize(broadcastServerInfo, receiverServerInfo, senderServerInfo);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param senderServerInfo 
	 * @param receiverServerInfo 
	 * @param broadcastServerInfo 
	 */
	private void initialize(String broadcastServerInfo, String receiverServerInfo, String senderServerInfo) {
		frmShutdownServer = new JFrame();
		frmShutdownServer.setTitle("Shutdown Server");
		frmShutdownServer.setBounds(100, 100, 450, 300);
		frmShutdownServer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmShutdownServer.getContentPane().setLayout(new BorderLayout(0, 50));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JButton btnShutdownServer = new JButton("Shutdown Server");
		btnShutdownServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(frmShutdownServer, "Are you sure you want to shutdown the server?", "Shutdown server", JOptionPane.YES_NO_OPTION);

				if(result == 0) {
					frmShutdownServer.setVisible(false);
					frmShutdownServer.dispose();
					StartNewServer.shutdownServer();
				}
				
			}
		});
		panel.add(btnShutdownServer);
		
		JLabel lblReceiverServer = new JLabel(receiverServerInfo);
		lblReceiverServer.setToolTipText("Receiver Server Socket Information");
		panel.add(lblReceiverServer);
		lblReceiverServer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Receiver Server"));
		
		JLabel lblBroadcastServer = new JLabel(broadcastServerInfo);
		lblBroadcastServer.setToolTipText("Broadcast Server Socket Information");
		panel.add(lblBroadcastServer);
		lblBroadcastServer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Broadcast Server"));
		
		JLabel lblServerSender = new JLabel(senderServerInfo);
		lblServerSender.setToolTipText("Sender Server Socket Information");
		panel.add(lblServerSender);
		lblServerSender.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Sender Server"));
		
		frmShutdownServer.setResizable(false);
		frmShutdownServer.getContentPane().add(panel);
		frmShutdownServer.setVisible(true);
	}

}
