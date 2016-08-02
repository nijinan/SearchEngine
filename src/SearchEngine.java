import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchEngine {
	List<Document> docs = new ArrayList<Document>();
	List<Task> tasks = new ArrayList<Task>();
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
					task.doc = doc;
					doc.addtask(task);
					tasks.add(task);
				}
				docs.add(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void calcidf(){
		for (Task task1 : tasks){
			for (Task task2 : tasks)if (task1 != task2){
				double tmp = 0;
				
			}
		}
	}
	
	public List<String> search(String query){
		List<String> ret = new ArrayList<String>();
		
		return ret;
	}
	
	public List<String> search1(String query){
		List<String> ret = new ArrayList<String>();
		String[] words = query.replaceAll(" +", " ").trim().split(" ");
		for (Document doc : docs){
			for (Task task : doc.tasks){
				boolean flag = true;
				for (String word : words){
					if (!task.map.containsKey(word)) {
						flag = false;
						break;
					}
				}
				if (flag){
					ret.add(task.toString() + " " + doc.name);
				}
			}
		}
		Collections.sort(ret,String.CASE_INSENSITIVE_ORDER);
		return ret;
	}
	
	public static void main(String args[]){
		SearchEngine se = new SearchEngine();
		se.LoadDoc("D:\\ApiDocs");
		System.out.println("");
		for (String str : se.search1("want asdf. go")){
			System.out.println(str);
		}
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
	String text;
	Document doc;
	List<String> words = new ArrayList<String>();
	List<String> pos = new ArrayList<String>();
	Map<String,Integer> map = new HashMap<String,Integer>();
	public void settask(String line){
		this.text = line;
		line = line.replaceAll(" +", " ");
		String [] words = line.split(" ");
		for (String word : words){
			String key = word.split("/")[0];
			String value = word.split("/")[1];
			this.words.add(key);
			this.pos.add(value);
			
			if (!map.containsKey(key)) map.put(key, 0);
			map.put(key, map.get(key)+1);
		}
	}
	public String toString(){
		String ret = "";
		for (String word : words){
			ret = ret + word + " ";
		}
		return ret;
	} 
}