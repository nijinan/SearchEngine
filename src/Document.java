import java.util.ArrayList;
import java.util.List;

public class Document{
	String name = "";
	int id;
	double factor = 0;
	List<Task> tasks = new ArrayList<Task>();
	public void addtask(Task task){
		tasks.add(task);
	}
}