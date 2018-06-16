package Device;

import java.util.Random;

public class Raid {
	public static void addParityBit(Disk disk) {
		int numberOfOnes=0;
		String bits=disk.getData();
		for (int i=0; i<bits.length(); ++i)
			if (bits.charAt(i)=='1')
				++numberOfOnes;
		
		if (numberOfOnes%2 == 0)
			disk.setData(bits+"0");
		else
			disk.setData(bits+"1");
	}
	
	//changing random bit- simulating error
	public static void changeRandomBit(Disk disk) {
		String bits=disk.getData();
		String bitsWithError="";
		Random random=new Random();
		int numberOfBit=random.nextInt(bits.length());
		char bit=bits.charAt(numberOfBit);
		if (bit=='1')
			bit='0';
		else
			bit ='1';
		
		//genereting bit with error in random place
		for (int i=0; i<numberOfBit; ++i)
			bitsWithError+=bits.charAt(i);
		bitsWithError+=bit;
		for (int i=numberOfBit+1; i<bits.length(); ++i)
			bitsWithError+=bits.charAt(i);
		disk.setData(bitsWithError);
	}
	
}
