package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class blba {
	private JFrame f;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	public blba(){
		
		f = new JFrame("Hospital Management");
		f.getContentPane().setBackground(Color.LIGHT_GRAY);
		f.setSize(1000, 800);
		f.setLocation(300,200);
		f.getContentPane().setLayout(null);
		
		JLabel lblPatientid = new JLabel("PatientID: ");
		lblPatientid.setBounds(37, 92, 74, 14);
		f.getContentPane().add(lblPatientid);
		
		JLabel lblDoctorid = new JLabel("DoctorID: ");
		lblDoctorid.setBounds(37, 174, 74, 26);
		f.getContentPane().add(lblDoctorid);
		
		JLabel lblNurseid = new JLabel("NurseID: ");
		lblNurseid.setBounds(37, 240, 46, 14);
		f.getContentPane().add(lblNurseid);
		
		JLabel lblDivision = new JLabel("Division: ");
		lblDivision.setBounds(37, 322, 46, 14);
		f.getContentPane().add(lblDivision);
		
		JLabel lblTitle = new JLabel("Title: ");
		lblTitle.setBounds(37, 401, 46, 14);
		f.getContentPane().add(lblTitle);
		
		JLabel lblData = new JLabel("Data: ");
		lblData.setBounds(506, 58, 143, 26);
		f.getContentPane().add(lblData);
		
		textField = new JTextField();
		textField.setBounds(121, 89, 214, 20);
		f.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(121, 398, 214, 20);
		f.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(121, 319, 214, 20);
		f.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(121, 237, 214, 20);
		f.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(506, 89, 415, 481);
		f.getContentPane().add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(560, 637, 117, 63);
		f.getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(772, 637, 110, 63);
		f.getContentPane().add(btnCancel);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(121, 177, 214, 20);
		f.getContentPane().add(textField_6);
	}
}
