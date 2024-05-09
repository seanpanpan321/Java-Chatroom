import java.io.Serializable;

public class TestSerializableObject implements Serializable {

	public static final long serialVersionUID = -8557234467976812143L;
	private String name;
	private Integer id;
	public TestSerializableObject(String name, Integer id) {
		
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TestSerializableObject [name=" + name + ", id=" + id + "]";
	}
	

}
