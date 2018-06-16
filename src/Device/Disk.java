package Device;

public class Disk {
	private static int numberOfDisks=0;
	private int id;
	private String name;
	private String data;
	
	public Disk(String name, String data) {
		super();
		this.id = ++numberOfDisks;
		this.name = name;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Disk [id=" + id + ", name=" + name + ", data=" + data + "]";
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
