package io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;

import Device.Disk;

public class Raport {
	public static void makeRaport(Disk [] disk, String fileName, String inputData) {
		  PrintWriter zapis;
		  LocalDateTime localTime= LocalDateTime.from(LocalDateTime.now());
		try {
			zapis = new PrintWriter("raport_raid.txt");
			zapis.println("GENERAL RAPORT- RAID 5:");
			zapis.println("Date of generating: "+localTime+"\n");
			
			zapis.println("INPUT DATA:");
			zapis.println("Given name of input file: "+fileName);
			zapis.println("Input data: "+inputData+"\n");
			
			zapis.println("ALL DISKS:");
			zapis.println("Disk id: "+disk[0].getId());
			zapis.println("Disk name: "+disk[0].getName());
			zapis.println("Disk data: "+disk[0].getData());
			
			zapis.println("Disk id: "+disk[1].getId());
			zapis.println("Disk name: "+disk[1].getName());
			zapis.println("Disk data: "+disk[1].getData());
			
			zapis.println("Disk id: "+disk[2].getId());
			zapis.println("Disk name: "+disk[2].getName());
			zapis.println("Disk data: "+disk[2].getData()+"\n");
			
			zapis.println("INJECTED ERRORS:");
			
			zapis.close();
		} catch (FileNotFoundException e) {
			System.out.println("Writing failed.");
			e.printStackTrace();
		}
	}
}
