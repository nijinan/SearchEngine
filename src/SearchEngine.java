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
    private static int NUM_OF_THREAD = 8;
    static Thread[] threads = new Thread[NUM_OF_THREAD];
	public SearchEngine() {
		// TODO Auto-generated constructor stub
	}

	public void LoadDoc(String path){
		File dir = new File(path);
		int no = 0;
		for (int time = 0; time < 1; time++)
		for (File file : dir.listFiles()){
			try {
				Document doc = new Document();
				doc.name = file.getName();
				doc.id = no;
				no++;
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null){
					Task task = new Task();
					task.settask(line);
					line = br.readLine();
					task.score = Double.valueOf(line);
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
		// get normal factor for each document
		for (Document doc : docs){
			for (Task task : doc.tasks){
				double score = 0;
				for (Task t : doc.tasks){
					score += task.getSim(t);
				}
				doc.factor+=score;
			}
		}
		// for each task calculate the idf
        for (int i = 0; i< NUM_OF_THREAD; i++) {
            threads[i] = new MyThread(i);
            threads[i].start();
        }
        for (int i = 0; i< NUM_OF_THREAD; i++) {
        	try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		for (Document doc : docs){
			for (Task task : doc.tasks){
				for (double relevance : task.relevances){
					task.tfidf.add(relevance * Math.log(docs.size()/task.idf));
				}
			}
		}
	}
	
	public List<Double> search(String q){
		Task query = new Task();
		query.settask(q);
		List<Double> A = new ArrayList<Double>();
		for (Task task : tasks){
			A.add(query.getSim(task));
		}
		List<Double> B = new ArrayList<Double>();
		for (int i = 0; i < docs.size(); i++){
			double tmp = 0;
			for (int j = 0; j < tasks.size(); j++){
				tmp += A.get(j) * tasks.get(j).tfidf.get(i);
			}
			B.add(tmp);
		}
		return B;
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
	public class MyThread extends Thread
	{
	    private int id;
	    public MyThread(int id)
	    {
	        this.id = id;
	    }
	    public void run()
	    {
	    	for (Document doc : docs){
    			if (doc.id % NUM_OF_THREAD != id)continue;
    			for (Task task : doc.tasks){
    				for (Document d : docs){
    					double tf = 0;
    					double relevance = 0;
    					for (Task t : d.tasks){
    						double similaity = task.getSim(t); 
    						tf = Math.max(tf, similaity);
    						relevance += similaity * t.score;
    					}
    					task.idf += tf;
    					task.tfs.add(tf);
    					task.relevances.add(relevance);
    				}
    			}			
    		}
	    }
	}
	public static void main(String args[]){
		SearchEngine se = new SearchEngine();
        long start = System.nanoTime();
        
		se.LoadDoc("D:\\ApiDocs");
//		System.out.println("");
//		for (String str : se.search1("want asdf. go")){
//			System.out.println(str);
//		}
		se.calcidf();
		List<Double> tmp = se.search("take/verb task/knn from/prop index/nn");
		for (double t : tmp){
			System.out.println(t);
		}
		long end = System.nanoTime();
		System.out.println((double)(end-start)/1000000000);
	}

}
