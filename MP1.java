import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
		Integer[] needesIndexes = getIndexes();
		
		List<String> stopList = Arrays.asList(stopWordsArray);
				
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		
		List<String> lines = Files.readAllLines(Paths.get(this.inputFileName), Charset.defaultCharset());
		for(int i=0;i<needesIndexes.length;i++){
			
			String strLine = lines.get(needesIndexes[i]);
			
			StringTokenizer tokens = new StringTokenizer(strLine, this.delimiters);
			while(tokens.hasMoreTokens()){
				String word = tokens.nextToken().toLowerCase().trim();
				if(stopList.contains(word)){
					continue;
				}
				
				if(words.containsKey(word)){
					words.put(word, words.get(word) + 1);
				}
				else{
					words.put(word, 1);
				}
				
			}
		}
		
		words=sortByValues(words);
		
		int rectIndex = 0;
	    Iterator it = words.entrySet().iterator();
	    while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if(rectIndex<20){
				ret[rectIndex]=(String)pair.getKey();
				rectIndex++;	
			}else{
				continue;
			}
			it.remove();
		}
		
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
	
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());

	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
					Integer value1=(Integer)((Map.Entry) (o1)).getValue();
					Integer value2=(Integer)((Map.Entry) (o2)).getValue();
					String key1=(String)((Map.Entry) (o1)).getKey();
					String key2=(String)((Map.Entry) (o2)).getKey();
					if(value1 == value2){
						int res = String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
						return (res != 0) ? res : key1.compareTo(key2);
					}
					else{
						return value2.compareTo(value1);
					}
	            }
	       });

	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
}
