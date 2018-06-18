package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import Device.Disk;
import Device.Raid;
import io.Raport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainController {
	Disk [] disks;
	
	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField readFileTextField;
	
	@FXML
	private Label inputDataLabel, readErrorLabel;

	@FXML
	private ListView<String> disk1, disk2, disk3;
	
	@FXML
	public void initialize() {
		readErrorLabel.setVisible(false);
		
		//all will be move to controller 
		disks=new Disk[3];
		disks[0]= new Disk("C:","0111");
		disks[1]= new Disk("D:","1111");
		disks[2]= new Disk("E:","0000");
		
		System.out.println("Starting data:");
		System.out.println(disks[0]);
		System.out.println(disks[1]);
		System.out.println(disks[2]);
		
		Raid.changeRandomBit(disks[0]);
		Raid.changeRandomBit(disks[1]);
		Raid.changeRandomBit(disks[2]);
		
		System.out.println("After injection error bit:");
		System.out.println(disks[0]);
		System.out.println(disks[1]);
		System.out.println(disks[2]);
		
		System.out.println("After adding parity bit:");
		Raid.addParityBit(disks[0]);
		Raid.addParityBit(disks[1]);
		Raid.addParityBit(disks[2]);
		
		System.out.println(disks[0]);
		System.out.println(disks[1]);
		System.out.println(disks[2]);
		
//		FXMLLoader loader= new FXMLLoader();
//		loader.setLocation(this.getClass().getResource("../../../resources/fxml/signin.fxml"));
		//AnchorPane anchorPane= null;
//		try {
//			anchorPane=loader.load();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		SignInController signInCntroller= loader.getController();
//		signInCntroller.setMainController(this);
//		setScreen(anchorPane);
	}

	@FXML
	public void readData() {
		String fileName=readFileTextField.getText();
//		String fileName="input.txt";
		boolean doesEndWithTxt= fileName.endsWith(".txt");
		if (!doesEndWithTxt)
			fileName+=".txt";
		
		String input="";
		
		  try {
			File file = new File(fileName);
			  Scanner in = new Scanner(file);
			  input = in.nextLine();
			  readErrorLabel.setVisible(false);
			  Raport.makeRaport(disks, fileName, input);
			  System.out.println(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			readErrorLabel.setVisible(true);
			e.printStackTrace();
		}
		
		inputDataLabel.setText(input);
	}

	@FXML
	public void findFile() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("TXT", "*.txt"));
		File file = fileChooser.showOpenDialog(null);
		if(file == null) System.out.println("NULL!");
		readFileTextField.setText(file.getAbsolutePath());
	}

	@FXML
	public void putDataOnDisks(){
		int index = 0;
		List<String> dividedData1 = new LinkedList<>();
		List<String> dividedData2 = new LinkedList<>();
		List<String> dividedData3 = new LinkedList<>();
		String data = inputDataLabel.getText();
		int nrDisk = 1;
		while (index < data.length()) {
			if(nrDisk % 3 == 1){
				dividedData1.add(data.substring(index, Math.min(index + 4,data.length())));
			} else if(nrDisk % 3 == 2) {
				dividedData2.add(data.substring(index, Math.min(index + 4,data.length())));
			} else if(nrDisk % 3 == 0) {
				dividedData3.add(data.substring(index, Math.min(index + 4,data.length())));
			}
			nrDisk++;
			index += 4;
		}

		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(dividedData1);
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(dividedData2);
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(dividedData3);
		disk1.setItems(obsDisk1);
		disk2.setItems(obsDisk2);
		disk3.setItems(obsDisk3);
	}

	public void setScreen(AnchorPane anchorPane){

		mainPane.getChildren().add(anchorPane);
	}

}
