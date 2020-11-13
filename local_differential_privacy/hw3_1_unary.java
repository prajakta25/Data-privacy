import java.io.*;
import java.util.*;

public class hw3_1_unary {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	Map<Integer, Integer> ageFreq = new HashMap<>();
	private ArrayList<Integer> dataset = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> serverData = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> actualData = new ArrayList<>();
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

	public void writeData(Map<Integer, Float> a) throws IOException {

		FileWriter writer = new FileWriter("hw3_1_unaryestFreq.txt");
		Set<Integer> k = a.keySet();
		for (int key : k) {
			writer.write(a.get(key) + System.lineSeparator());
		}
		writer.close();

	}

	public boolean estFreqOfEachBit(float p) {
		Random r = new Random();
		float ran = r.nextFloat();
		return (ran < p);
	}

	public void server() throws IOException {
		int dataSize = serverData.size();
		boolean visited[] = new boolean[dataSize - 1];
		int actualFreq[] = new int[dataset.size()];
		int estFreq[] = new int[dataset.size()];
		float unibiasedEstFreq[] = new float[dataset.size()];
		Map<Integer, Float> unbiasedV = new HashMap<>();

		Arrays.fill(visited, false);
		Arrays.fill(actualFreq, 0);
		Arrays.fill(estFreq, 0);

		// calculate actual frequency
		for (int i = 0; i < actualData.size(); i++) {
			for (int j = 0; j < actualData.get(0).size(); j++) {
				int temp = actualFreq[j];
				actualFreq[j] = temp + actualData.get(i).get(j);
			}
		}

		System.out.println("\nActual frquency");
		for (int j = 0; j < actualFreq.length; j++) {
			System.out.print(actualFreq[j] + " ");
		}
		System.out.println();

		// calculate server Data collected frequency
		for (int i = 0; i < serverData.size(); i++) {
			for (int j = 0; j < serverData.get(0).size(); j++) {
				int temp = estFreq[j];
				estFreq[j] = temp + serverData.get(i).get(j);
			}
		}

		// Frequency estimation using fi* = (fi−q)/(p−q)
		System.out.println("\nEstimated frquency");
		for (int i = 0; i < estFreq.length; i++) {
			// num is (fi−q)
			float num = estFreq[i] - q;
			// den is (p−q)
			float den = p - q;
			// fi* = (fi−q)/(p−q)
			unibiasedEstFreq[i] = num / den;
		}

		for (int j = 0; j < unibiasedEstFreq.length; j++) {
			System.out.print(unibiasedEstFreq[j] + " ");
			unbiasedV.put(dataset.get(j), unibiasedEstFreq[j]);
		}
		System.out.println();
		writeData(unbiasedV);
		if (k > 0) {
			// find the top-k frequent ages
			double d = (double) k / 100.00;
			int kVal = (int) (d * dataset.size());

			List<Map.Entry<Integer, Float>> freqList = new ArrayList<Map.Entry<Integer, Float>>(unbiasedV.entrySet());

			// Sort the list
			Collections.sort(freqList, new Comparator<Map.Entry<Integer, Float>>() {
				public int compare(Map.Entry<Integer, Float> obj1, Map.Entry<Integer, Float> obj2) {
					if (obj1.getValue() == obj2.getValue())
						return obj2.getKey() - obj1.getKey();
					else
						return (int) (obj2.getValue() - obj1.getValue());
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
		for (int i = 0; i < actualFreq.length; i++) {
			L1 += Math.abs(actualFreq[i] - unibiasedEstFreq[i]);
		}
		System.out.println("\nL1 distance = " + L1);
	}

	// server aggregate data sent by user
	public void aggData(ArrayList<Integer> x) {
		serverData.add(x);
	}

	public void user() {
		double div = (double) nVal / 100.00;
		int dataSize = (int) (div * (modData.size() - 1));
		for (int i = 0; i < dataSize; i++) {
			int age = Integer.parseInt(modData.get(i).get(0));
			// Encode the input value 𝑣 into a d-bit string
			ArrayList<Integer> x = new ArrayList<>();
			ArrayList<Integer> xOrg = new ArrayList<>();
			// Initialize all bits to 0
			for (int j = 0; j < dataset.size(); j++) {
				x.add(0);
				xOrg.add(0);
			}
			// set x[v]:=1
			int index = dataset.indexOf(age);
			x.set(index, 1);
			xOrg.set(index, 1);
			actualData.add(xOrg);
			// estimate frequency of each bit and perturb each bit, preserving it with
			// probability p
			for (int j = 0; j < x.size(); j++) {
				if (!estFreqOfEachBit(p)) {
					if (x.get(j) == 0) {
						x.set(j, 1);
					} else if (x.get(j) == 1) {
						x.set(j, 0);
					}
				}
			}
			aggData(x);
		}

	}

	public void calPnQ() {
		// find probability p and q

		float epislon = (float) ep / 2;
		// num is e^(epsilon/2)
		float num = (float) Math.exp(epislon);
		// den is e^(epsilon/2) + 1
		float den = num + 1;
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
		System.out.println("\nD = " + dataset.toString());

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		hw3_1_unary obj = new hw3_1_unary();
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
