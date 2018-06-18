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
import javafx.scene.control.Button;
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
    private Button bCalculateParity, bRecoverDisk;
	@FXML
	public void initialize() {
		readErrorLabel.setVisible(false);
		bCalculateParity.setDisable(true);
		bRecoverDisk.setDisable(true);
		
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
		if(input.length() % 8 != 0){
		      int nrOfZeros = Math.abs((input.length() % 8) - 8);
		      for(int i = 0; i < nrOfZeros; i++){
		          input = "0".concat(input);
              }
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
    public void changeDisk1(){
	    bRecoverDisk.setDisable(false);
        ObservableList<String> obsDisk1 = disk1.getItems();
        for(int i = 0; i<obsDisk1.size(); i++){
            obsDisk1.set(i, "XXXX");
        }
        disk1.setItems(obsDisk1);
    }

    @FXML
    public void changeDisk2(){
        bRecoverDisk.setDisable(false);
        ObservableList<String> obsDisk2 = disk2.getItems();
        for(int i = 0; i<obsDisk2.size(); i++){
            obsDisk2.set(i, "XXXX");
        }
        disk2.setItems(obsDisk2);
    }

    @FXML
    public void changeDisk3(){
        bRecoverDisk.setDisable(false);
        ObservableList<String> obsDisk3 = disk3.getItems();
        for(int i = 0; i<obsDisk3.size(); i++){
            obsDisk3.set(i, "XXXX");
        }
        disk3.setItems(obsDisk3);
    }

    @FXML
    public void recoverDisk(){
        bRecoverDisk.setDisable(true);
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        if(obsDisk1.contains("XXXX")){
            for(int i=0; i < obsDisk1.size(); i++){
                obsDisk1.set(i,Raid.xor(obsDisk2.get(i), obsDisk3.get(i)));
            }
        } else if(obsDisk2.contains("XXXX")){
            for(int i=0; i < obsDisk2.size(); i++){
                obsDisk2.set(i,Raid.xor(obsDisk1.get(i), obsDisk3.get(i)));
            }
        } else if(obsDisk3.contains("XXXX")){
            for(int i=0; i < obsDisk3.size(); i++){
                obsDisk3.set(i,Raid.xor(obsDisk1.get(i), obsDisk2.get(i)));
            }
        }
    }

	@FXML
	public void putDataOnDisks(){
	    bCalculateParity.setDisable(false);
		int index = 0;
		List<String> dividedData1 = new LinkedList<>();
		List<String> dividedData2 = new LinkedList<>();
		List<String> dividedData3 = new LinkedList<>();
		String data = inputDataLabel.getText();
		int nrDisk = 1;
		int unknownValue = 3;
		while (index < data.length()) {
			if(nrDisk % 3 == 1){
                if(nrDisk != unknownValue){
                    dividedData1.add(data.substring(index, Math.min(index + 4,data.length())));
                    index += 4;
                } else {
                    dividedData1.add("XXXX");
                    unknownValue += 5;
                }

			} else if(nrDisk % 3 == 2) {
                if(nrDisk != unknownValue){
                    dividedData2.add(data.substring(index, Math.min(index + 4,data.length())));
                    index += 4;
                } else {
                    dividedData2.add("XXXX");
                    unknownValue += 2;
                }
			} else if(nrDisk % 3 == 0) {
			    if(nrDisk != unknownValue){
                    dividedData3.add(data.substring(index, Math.min(index + 4,data.length())));
                    index += 4;
                } else {
			        dividedData3.add("XXXX");
                    unknownValue += 2;
                }

			}

			nrDisk++;
		}
        if(dividedData1.size() != dividedData3.size()){
            dividedData3.add("XXXX");
        }

		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(dividedData1);
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(dividedData2);
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(dividedData3);
		disk1.setItems(obsDisk1);
		disk2.setItems(obsDisk2);
		disk3.setItems(obsDisk3);
	}

	@FXML
	public void calculateParity(){
	    bCalculateParity.setDisable(true);
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        for(int i=0; i < obsDisk1.size(); i++){
            if(obsDisk1.get(i).equals("XXXX")){
                obsDisk1.set(i,Raid.xor(obsDisk2.get(i), obsDisk3.get(i)));
            } else if(obsDisk2.get(i).equals("XXXX")){
                obsDisk2.set(i,Raid.xor(obsDisk1.get(i), obsDisk3.get(i)));
            } else if(obsDisk3.get(i).equals("XXXX")){
                obsDisk3.set(i,Raid.xor(obsDisk1.get(i), obsDisk2.get(i)));
            }
        }

        disk1.setItems(obsDisk1);
        disk2.setItems(obsDisk2);
        disk3.setItems(obsDisk3);
    }



	public void setScreen(AnchorPane anchorPane){

		mainPane.getChildren().add(anchorPane);
	}

}
