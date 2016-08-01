import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
	List<Document> docs = new ArrayList<Document>();
	public SearchEngine() {
		// TODO Auto-generated constructor stub
	}

	public void LoadDoc(String path){
		File dir = new File(path);
		for (File file : dir.listFiles()){
			try {
				Document doc = new Document();
				doc.name = file.getName();
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null){
					Task task = new Task();
					task.settask(line);
					line = br.readLine();
					task.tf = Double.valueOf(line);
					doc.addtask(task);
				}
				docs.add(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String args[]){
		SearchEngine se = new SearchEngine();
		se.LoadDoc("D:\\ApiDocs");
		System.out.println("");
	}
}
class Document{
	String name = "";
	List<Task> tasks = new ArrayList<Task>();
	public void addtask(Task task){tasks.add(task);}
}

class Task{
	double tf;
	double idf;
	double score;
	List<String> words = new ArrayList<String>();
	List<String> pos = new ArrayList<String>();
	public void settask(String line){
		line = line.replaceAll(" +", " ");
		String [] words = line.split(" ");
		for (String word : words){
			this.words.add(word.split("/")[0]);
			this.pos.add(word.split("/")[1]);
		}
	}
}