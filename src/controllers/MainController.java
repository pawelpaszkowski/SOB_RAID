package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import Device.Disk;
import Device.Raid;
import io.Raport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainController {
	private Disk [] disks;
	private int spoiledDisk=0;
	private int lastSpoiledDisk=0;
	private LinkedList <Integer> indexesOfErrorBits;
	
	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField readFileTextField, numberOfErrors1, numberOfErrors2, numberOfErrors3;
	
	@FXML
	private Label inputDataLabel, readErrorLabel, labelError;

	@FXML
	private ListView<String> disk1, disk2, disk3;

	@FXML
    private Button bWriteData, bCalculateParity, bRecoverDisk, bChangeDisk1, bChangeDisk2, bChangeDisk3, bInjectErrors1, bInjectErrors2, bInjectErrors3;

    @FXML
    private ImageView imageView;

	@FXML
	public void initialize() {
		readErrorLabel.setVisible(false);
		labelError.setVisible(false);
		bCalculateParity.setDisable(true);
		bRecoverDisk.setDisable(true);
        bWriteData.setDisable(true);
        bChangeDisk1.setDisable(true);
        bChangeDisk2.setDisable(true);
        bChangeDisk3.setDisable(true);
        bInjectErrors1.setDisable(true);
        bInjectErrors2.setDisable(true);
        bInjectErrors3.setDisable(true);
        numberOfErrors1.setDisable(true);
        numberOfErrors2.setDisable(true);
        numberOfErrors3.setDisable(true);
        indexesOfErrorBits=new LinkedList<Integer>();
		
		//all will be move to controller 
		disks=new Disk[3];

        Image image = new Image("file:src/background.jpg");
        imageView.setImage(image);

		/*disks[0]= new Disk("C:","0111");
		disks[1]= new Disk("D:","1111");
		disks[2]= new Disk("E:","0000");*/
		
		/*System.out.println("Starting data:");
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
		*/
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
			  //Raport.makeRaport(disks, fileName, input);
              if (spoiledDisk<=1)
                 bWriteData.setDisable(false);
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
        if (lastSpoiledDisk!=1)
            spoiledDisk++;
        lastSpoiledDisk=1;
        boolean AreTwoDiskSpoiled=false;
        if (spoiledDisk<=1)
                bRecoverDisk.setDisable(false);
        else {
            bRecoverDisk.setDisable(true);
            bWriteData.setDisable(true);
            labelError.setVisible(true);
            if (spoiledDisk==2)
                AreTwoDiskSpoiled=true;
        }
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        for(int i = 0; i<obsDisk1.size(); i++){
            obsDisk1.set(i, "XXXX");
        }
        disk1.setItems(obsDisk1);
        writeToRaport(obsDisk1, obsDisk2, obsDisk3, "DISK STATE AFTER CHANGING DISK 1");
        if (AreTwoDiskSpoiled)
            Raport.writeSeriousError();
    }

    @FXML
    public void changeDisk2(){
        if (lastSpoiledDisk!=2)
            spoiledDisk++;
        lastSpoiledDisk=2;
        boolean AreTwoDiskSpoiled=false;
        if (spoiledDisk<=1)
            bRecoverDisk.setDisable(false);
        else {
            bRecoverDisk.setDisable(true);
            bWriteData.setDisable(true);
            labelError.setVisible(true);
            if (spoiledDisk==2)
                AreTwoDiskSpoiled=true;
        }
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        for(int i = 0; i<obsDisk2.size(); i++){
            obsDisk2.set(i, "XXXX");
        }
        disk2.setItems(obsDisk2);

        writeToRaport(obsDisk1, obsDisk2, obsDisk3, "DISK STATE AFTER CHANGING DISK 2");
        if (AreTwoDiskSpoiled)
            Raport.writeSeriousError();
    }

    @FXML
    public void changeDisk3(){
        if (lastSpoiledDisk!=3)
            spoiledDisk++;
        lastSpoiledDisk=3;
        boolean AreTwoDiskSpoiled=false;
        if (spoiledDisk<=1)
            bRecoverDisk.setDisable(false);
        else {
            bRecoverDisk.setDisable(true);
            bWriteData.setDisable(true);
            labelError.setVisible(true);
            if (spoiledDisk==2)
                AreTwoDiskSpoiled=true;
        }
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        for(int i = 0; i<obsDisk3.size(); i++){
            obsDisk3.set(i, "XXXX");
        }
        disk3.setItems(obsDisk3);

        writeToRaport(obsDisk1, obsDisk2, obsDisk3, "DISK STATE AFTER CHANGING DISK 1");
        if (AreTwoDiskSpoiled)
            Raport.writeSeriousError();
    }

    @FXML
    public void injectErrors1(){
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        disk1.setItems(generateErrorsBits(obsDisk1, numberOfErrors1));
    }

    @FXML
    public void injectErrors2(){
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        disk2.setItems(generateErrorsBits(obsDisk2, numberOfErrors2));
    }

    @FXML
    public void injectErrors3(){
        ObservableList<String> obsDisk1 = disk1.getItems();
        ObservableList<String> obsDisk2 = disk2.getItems();
        ObservableList<String> obsDisk3 = disk3.getItems();
        disk3.setItems(generateErrorsBits(obsDisk3, numberOfErrors3));
    }

    public ObservableList<String> generateErrorsBits(ObservableList<String> obsDisk, TextField numberOfErrors){
        Random random=new Random();
        //one number is excluded to not spoiled all bits (there is existing another mechanism for that)
        int topIndex=obsDisk.size()*4;
//        int numberOfBits=random.nextInt(topIdnex)+1;
        int numberOfBits=Integer.parseInt(numberOfErrors.getText())-1;

        int numberOfFreeBits=topIndex-indexesOfErrorBits.size();

        if ((0<=numberOfBits) && (numberOfBits<=topIndex-1) && (numberOfFreeBits > numberOfBits)) {

            int randomIndex;

            for (int i = 0; i <= numberOfBits; ) {
                randomIndex = random.nextInt(topIndex);
                if (!indexesOfErrorBits.contains(randomIndex)) {
                    indexesOfErrorBits.add(randomIndex);
                    i++;
                }
            }

            for (int i = 0; i < indexesOfErrorBits.size(); i++)
                System.out.println(indexesOfErrorBits.get(i) + ", ");

            for (int i = 0; i < obsDisk.size(); i++) {
                String portionOfBits = "";
                for (int j = i * 4; j < i * 4 + 4; j++)
                    if (indexesOfErrorBits.contains(j))
                        portionOfBits += "X";
                    else
                        portionOfBits += obsDisk.get(i).substring(j - (i * 4), j - (i * 4) + 1);

                obsDisk.set(i, portionOfBits + "  (" + obsDisk.get(i) + ")");
            }

            return obsDisk;
        }
        return obsDisk;
    }

    @FXML
    public void recoverDisk(){
        spoiledDisk--;
        lastSpoiledDisk=0;
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

        writeToRaport(obsDisk1, obsDisk2, obsDisk3, "DISK STATE AFTER RECOVERY");
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

        disks[0] = new Disk("A", dividedData1);
        disks[1] = new Disk("B", dividedData2);
        disks[2] = new Disk("C", dividedData3);
        Raport.makeRaport(disks, readFileTextField.getText(), inputDataLabel.getText());
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(dividedData1);
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(dividedData2);
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(dividedData3);
		disk1.setItems(obsDisk1);
		disk2.setItems(obsDisk2);
		disk3.setItems(obsDisk3);
        bChangeDisk1.setDisable(true);
        bChangeDisk2.setDisable(true);
        bChangeDisk3.setDisable(true);
        bInjectErrors1.setDisable(true);
        bInjectErrors2.setDisable(true);
        bInjectErrors3.setDisable(true);
        numberOfErrors1.setDisable(true);
        numberOfErrors2.setDisable(true);
        numberOfErrors3.setDisable(true);
        bRecoverDisk.setDisable(true);
        spoiledDisk=0;
        lastSpoiledDisk=0;
        indexesOfErrorBits.clear();
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

        writeToRaport(obsDisk1, obsDisk2, obsDisk3, "DISK STATE AFTER PARITY");
        disk1.setItems(obsDisk1);
        disk2.setItems(obsDisk2);
        disk3.setItems(obsDisk3);
        bChangeDisk1.setDisable(false);
        bChangeDisk2.setDisable(false);
        bChangeDisk3.setDisable(false);
        bInjectErrors1.setDisable(false);
        bInjectErrors2.setDisable(false);
        bInjectErrors3.setDisable(false);
        numberOfErrors1.setDisable(false);
        numberOfErrors2.setDisable(false);
        numberOfErrors3.setDisable(false);
    }


    private void writeToRaport(ObservableList<String> obsDisk1, ObservableList<String> obsDisk2, ObservableList<String> obsDisk3, String s) {
        disks[0].setData(obsDisk1.subList(0, obsDisk1.size()));
        disks[1].setData(obsDisk2.subList(0, obsDisk2.size()));
        disks[2].setData(obsDisk3.subList(0, obsDisk3.size()));
        Raport.writeDisks(disks, s);
    }
	public void setScreen(AnchorPane anchorPane){

		mainPane.getChildren().add(anchorPane);
	}

}
