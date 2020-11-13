import java.io.*;
import java.util.*;

public class hw3_1_generalizedrr {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	private Map<Integer, Integer> ageFreq = new HashMap<>();
	private ArrayList<Integer> dataset = new ArrayList<>();
	private ArrayList<Integer> serverData = new ArrayList<>();
	private int k, nVal;
	private float ep, p, q;

	public void readData(String filename) throws IOException {
		File adultData = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(adultData));

		String st;
		while ((st = br.readLine()) != null) {
			ArrayList<String> data = new ArrayList<String>();
			String arr[] = st.split(",");
			Collections.addAll(data, arr);
			originalData.add(data);
		}
		modData = originalData;
		br.close();
	}

	public void writeData(Map<Integer, Integer> a, Map<Integer, Integer> b, ArrayList<Integer> d) throws IOException {

		FileWriter writer = new FileWriter("hw3_1_estFreq.txt");
		Set<Integer> k = a.keySet();
		for (int key : k) {
			writer.write(a.get(key) + System.lineSeparator());
		}
		writer.close();

		FileWriter writer1 = new FileWriter("hw3_1_actualFreq.txt");
		Set<Integer> k1 = b.keySet();
		for (int key : k1) {
			writer1.write(b.get(key) + System.lineSeparator());
		}
		writer1.close();

		FileWriter writer2 = new FileWriter("hw3_1_datasize.txt");
		for (int key : d) {
			writer2.write(key + System.lineSeparator());

		}
		writer2.close();

	}

	public boolean biasedCoinToss(float p) {

		Random r = new Random();
		float ran = r.nextFloat();
		return (ran < p);
	}

	public void server() throws IOException {
		Map<Integer, Integer> serverFreqDist = new HashMap<>();
		Map<Integer, Integer> estimatedIv = new HashMap<>();
		Map<Integer, Integer> unbiasedV = new HashMap<>();
		List<Integer> originalFreq = new ArrayList<>();
		List<Integer> estimatedFreq = new ArrayList<>();
		int dataSize = serverData.size() - 1;
		boolean visited[] = new boolean[dataSize];
		Arrays.fill(visited, false);

		// calculate the sum for each item in the locally perturbed data
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				if (serverData.get(i) == serverData.get(j)) {
					visited[j] = true;
					count++;
				}
			}
			serverFreqDist.put(serverData.get(i), count);
		}
		// System.out.println(serverFreqDist.toString());
		// find the frequency estimation
		double div = (double) nVal / 100.00;
		int n = (int) (div * (modData.size() - 1));
		System.out.println("n = " + n);
		System.out.println("\nEstimated frequency: ");
		Set<Integer> orgKey = ageFreq.keySet();
		for (int org : orgKey) {
			int nv = ageFreq.get(org);
			int Iv;
			if (serverFreqDist.containsKey(org)) {
				Iv = serverFreqDist.get(org);
			} else {
				Iv = 0;
			}
			// E[Iv] = (nv * p) + ((n-nv) * q)
			int eIv = (int) ((nv * p) + (n - nv) * q);
			estimatedIv.put(org, eIv);
			// Unbiased Estimation c[v] = (Iv - (n*q))/(p-q)
			int cv = (int) ((Iv - (n * q)) / (p - q));
			if (cv < 0) {
				cv = 0;
			}
			unbiasedV.put(org, cv);
			// store original and calculate frequency
			originalFreq.add(ageFreq.get(org));
			estimatedFreq.add(cv);
		}
		
		
		System.out.println(unbiasedV.toString());
		writeData(unbiasedV, ageFreq, dataset);

		if (k > 0) {
			// find the top-k frequent ages
			double d = (double) k / 100.00;
			int kVal = (int) (d * dataset.size());

			List<Map.Entry<Integer, Integer>> freqList = new ArrayList<Map.Entry<Integer, Integer>>(
					unbiasedV.entrySet());

			// Sort the list
			Collections.sort(freqList, new Comparator<Map.Entry<Integer, Integer>>() {
				public int compare(Map.Entry<Integer, Integer> obj1, Map.Entry<Integer, Integer> obj2) {
					if (obj1.getValue() == obj2.getValue())
						return obj2.getKey() - obj1.getKey();
					else
						return obj2.getValue() - obj1.getValue();
				}
			});

			// actual top-k frequent ages
			List<Map.Entry<Integer, Integer>> orgfreqList = new ArrayList<Map.Entry<Integer, Integer>>(
					ageFreq.entrySet());

			// Sort the list
			Collections.sort(orgfreqList, new Comparator<Map.Entry<Integer, Integer>>() {
				public int compare(Map.Entry<Integer, Integer> obj1, Map.Entry<Integer, Integer> obj2) {
					if (obj1.getValue() == obj2.getValue())
						return obj2.getKey() - obj1.getKey();
					else
						return obj2.getValue() - obj1.getValue();
				}
			});

			int estK[] = new int[kVal];
			int orgK[] = new int[kVal];

			//System.out.println("\nActual Top-" + kVal + " frequent age");
			for (int i = 0; i < kVal; i++) {
				//System.out.println(orgfreqList.get(i).getKey());
				orgK[i] = orgfreqList.get(i).getKey();
			}

			System.out.println("\nEstimated Top-" + kVal + " frequent age");
			for (int i = 0; i < kVal; i++) {
				System.out.println(freqList.get(i).getKey());
				estK[i] = freqList.get(i).getKey();
			}

			// calculate L1 distance
			double kL1 = 0.00;
			for (int i = 0; i < orgK.length; i++) {
				kL1 += Math.abs(orgK[i] - estK[i]);
			}
			System.out.println("\nL1 distance for  top-" + kVal + " frequent age = " + kL1);
		}

		// calculate L1 distance
		double L1 = 0.00;
		for (int i = 0; i < originalFreq.size() - 1; i++) {
			L1 += Math.abs(originalFreq.get(i) - estimatedFreq.get(i));
		}
		System.out.println("\nL1 distance = " + L1);

	}

	// server aggregate data sent by user
	public void aggregateData(int age) {
		serverData.add(age);
	}

	public void user() {
		double div = (double) nVal / 100.00;
		int dataSize = (int) (div * (modData.size() - 1));
		for (int i = 0; i < dataSize; i++) {
			int age = Integer.parseInt(modData.get(i).get(0));
			// toss a coin with bias p
			if (biasedCoinToss(p)) { // for head return true value
				aggregateData(Integer.parseInt(modData.get(i).get(0)));
			} else { // report any other value with probability q
				Random r = new Random();
				int index = r.nextInt(dataset.size() - 1);
				int v = dataset.get(index);
				while (v == age) {
					index = r.nextInt(dataset.size() - 1);
					v = dataset.get(index);
				}
				// each user sends its locally perturbed data to server
				aggregateData(v);
			}
		}

	}

	public void calPnQ() {
		
		// find probability p and q
		// num is e^epsilon
		float num = (float) Math.exp(ep);
		// den is e^epsilon + d -1
		float den = num + dataset.size()- 1;
		// p = (e^epsilon)/(e^epsilon + d -1)
		p = num / den;
		// q = 1/(e^epsilon + d -1)
		q = 1 / den;
		System.out.println("p = " + p);
		System.out.println("q = " + q);
	}

	public void findDataset() {
		// Find the set of output D
		double div = (double) nVal / 100.00;
		int dataSize = (int) (div * (modData.size() - 1));
		boolean visited[] = new boolean[dataSize];
		Arrays.fill(visited, false);
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				if (modData.get(i).get(0).trim().equals(modData.get(j).get(0).trim())) {
					visited[j] = true;
					count++;
				}
			}
			ageFreq.put(Integer.parseInt(modData.get(i).get(0).trim()), count);
			dataset.add(Integer.parseInt(modData.get(i).get(0).trim()));

		}
		Collections.sort(dataset);
		System.out.println("\nActual Freq " + ageFreq.toString());
		System.out.println("\nD = " + dataset.toString());

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		hw3_1_generalizedrr obj = new hw3_1_generalizedrr();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		String fName = buf.readLine();
		System.out.println("Enter value of epsilon");
		obj.ep = Float.parseFloat(buf.readLine());
		System.out.println("Enter value of k");
		obj.k = Integer.parseInt(buf.readLine());
		System.out.println("Enter value of n");
		obj.nVal = Integer.parseInt(buf.readLine());
		System.out.println("\nResults:\n");
		obj.readData(fName);
		obj.findDataset();
		obj.calPnQ();
		obj.user();
		obj.server();

	}

}
