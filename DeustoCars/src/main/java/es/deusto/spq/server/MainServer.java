package com.deusto.app.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainServer implements ActionListener, Runnable {

	public MainServer(String hostname, String port) {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String hostname = args[0];
		String port = args[1];

		new MainServer(hostname, port);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}