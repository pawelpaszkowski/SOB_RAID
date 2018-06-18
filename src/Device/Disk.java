package Device;

import java.util.List;

public class Disk {
	private static int numberOfDisks=0;
	private int id;
	private String name;
	private List<String> data;
	
	public Disk(String name, List<String> data) {
		super();
		this.id = ++numberOfDisks;
		this.name = name;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Disk [id=" + id + ", name=" + name + ", data=" + data + "]";
	}

    public static int getNumberOfDisks() {
        return numberOfDisks;
    }

    public static void setNumberOfDisks(int numberOfDisks) {
        Disk.numberOfDisks = numberOfDisks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
