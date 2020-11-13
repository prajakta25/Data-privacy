import java.io.*;
import java.util.*;

public class hw2_2_exponential {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	private ArrayList<ArrayList<String>> resList = new ArrayList<>();
	float ep;
	private float gp1Prob = 0f;
	private ArrayList<Float> probGroup = new ArrayList<>();;
	// setting sensitivity to 1
	private int deltau = 1;

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

	public void writeData(ArrayList<ArrayList<String>> resList2) throws IOException {
		String fileNm = "hw2_2_datafile.txt";
		FileWriter writer = new FileWriter(fileNm);
		for (ArrayList<String> data : resList2) {
			for (String f : data) {
				writer.write(f + System.lineSeparator());
			}

		}
		writer.close();
	}

	// utility function returns the utility for each u(D,r)
	public Map<Integer, String> utility(List<String> edu) {
		Map<Integer, String> eduFreq = new HashMap<>();
		boolean visited[] = new boolean[edu.size()];
		Arrays.fill(visited, false);
		// find the frequency of each education
		for (int i = 0; i < edu.size(); i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < edu.size(); j++) {
				if (edu.get(j).trim().equals(edu.get(i).trim())) {
					visited[j] = true;
					count++;
				}
			}
			eduFreq.put(count, edu.get(i));
		}
		return eduFreq;

	}

	// Query the most frequent “Education” result
	public Map<Integer, String> queryEdu(int val) {
		String mostFreEdu = "";
		Map<Integer, String> eduFreq = new HashMap<>();
		List<String> edu = new ArrayList<>();
		for (ArrayList<String> row : modData) {
			if (row.size() > 3) {
				edu.add(row.get(3));
			}

		}
		eduFreq = utility(edu);
		Set<Integer> mapKey = eduFreq.keySet();
		List<Integer> eduKeys = new ArrayList<>();
		for (int i : mapKey) {
			eduKeys.add(i);
		}
		Collections.sort(eduKeys, Collections.reverseOrder());

		if (val == 0) {// Query the most frequent “Education” result

			mostFreEdu = eduFreq.get(eduKeys.get(0));
			System.out.println(mostFreEdu);

		} else if (val == 1) { // Query the most frequent “Education” result removing a record with the most
								// frequent “Education”
			mostFreEdu = eduFreq.get(eduKeys.get(0));
			if (edu.contains(mostFreEdu)) {
				edu.remove(edu.indexOf(mostFreEdu));
			}
			Map<Integer, String> eduFreq1 = utility(edu);
			Set<Integer> k = eduFreq1.keySet();
			List<Integer> ek = new ArrayList<>();
			for (int i : k) {
				ek.add(i);
			}
			Collections.sort(ek, Collections.reverseOrder());
			mostFreEdu = eduFreq1.get(ek.get(0));
			System.out.println(mostFreEdu);
			eduFreq = eduFreq1;

		} else if (val == 2) { // Query the most frequent “Education” result removing any record with the
								// second most frequent “Education”
			String secMost = eduFreq.get(eduKeys.get(1));
			for (int i = 0; i < edu.size(); i++) {
				if (edu.get(i).equals(secMost)) {
					edu.remove(i);
					break;
				}
			}
			Map<Integer, String> eduFreq1 = utility(edu);
			Set<Integer> k = eduFreq1.keySet();
			List<Integer> ek = new ArrayList<>();
			for (int j : k) {
				ek.add(j);
			}
			Collections.sort(ek, Collections.reverseOrder());
			mostFreEdu = eduFreq1.get(ek.get(0));
			System.out.println(mostFreEdu);
			eduFreq = eduFreq1;

		} else if (val == 3) { // Query the most frequent “Education” result removing any record with the least
								// frequent “Education”
			String leastFreq = eduFreq.get(eduKeys.get(eduKeys.size() - 1));
			for (int i = 0; i < edu.size(); i++) {
				if (edu.get(i).equals(leastFreq)) {
					edu.remove(i);
					break;
				}
			}
			Map<Integer, String> eduFreq1 = utility(edu);
			Set<Integer> k = eduFreq1.keySet();
			List<Integer> ek = new ArrayList<>();
			for (int l : k) {
				ek.add(l);
			}
			Collections.sort(ek, Collections.reverseOrder());
			mostFreEdu = eduFreq1.get(ek.get(0));
			eduFreq = eduFreq1;
			System.out.println(mostFreEdu);

		}
		return eduFreq;

	}

	// calculate probability of each education op using
	// inverse of e^(ep * u(D,r) / 2 deltau)
	// formula = (2 * delta u * ln u(d,r))/ep
	public Map<Float, String> expoMech(Map<Integer, String> utilOp) {
		Map<Float, String> prob = new HashMap<>();
		Set<Integer> k = utilOp.keySet();
		List<Integer> listUtilVal = new ArrayList<>();
		// convert set of keys to list
		for (int i : k) {
			listUtilVal.add(i);
		}

		// calculate probabilty
		for (int p : listUtilVal) {
			double lg = (double) Math.log(p);
			double mul = (double) lg * 2 * deltau;
			double exp = (double) mul / (float) ep;
			prob.put((float) exp, utilOp.get(p));
		}

		return prob;

	}

	public Map<Float, String> findProb(Map<Float, String> expOp) {
		Map<Float, String> prob = new HashMap<>();
		Set<Float> k = expOp.keySet();
		List<Float> expVal = new ArrayList<>();
		// convert set of keys to list
		for (Float i : k) {
			expVal.add(i);
		}
		float sum = 0.0f;
		for (int j = 0; j < expVal.size(); j++) {
			sum += expVal.get(j);
		}
		// find probability for each education val
		for (int i = 0; i < expVal.size(); i++) {
			float pr = (float) expVal.get(i) / sum;
			prob.put(pr, expOp.get(expVal.get(i)));
		}
		return prob;

	}

	public void generateRes() throws IOException {
		gp1Prob = 0f;
		probGroup.clear();
		for (int i = 0; i < 4; i++) {
			System.out.println("\nFor epsilon = " + ep + " query = " + (i + 1));
			// get utility
			Map<Integer, String> utility = queryEdu(i);
			System.out.println("\nUtility of each education result value");
			Set<Integer> k = utility.keySet();
			List<Integer> us = new ArrayList<>();
			// convert set of keys to list
			for (int l : k) {
				us.add(l);
			}
			Collections.sort(us, Collections.reverseOrder());
			for (Integer f : us) {
				System.out.println(String.format("%-15s= %s", utility.get(f), f));
			}

			// find probability using exponential mechnism
			Map<Float, String> exp = expoMech(utility);
			Map<Float, String> prob = findProb(exp);
			Set<Float> k1 = exp.keySet();
			List<Float> ps = new ArrayList<>();
			// convert set of keys to list
			for (float l : k1) {
				ps.add(l);
			}
			Collections.sort(ps, Collections.reverseOrder());
			System.out.println("\nProbability of each education result value");
			ArrayList<Float> randomSel = new ArrayList<>();
			for (float f : ps) {
				System.out.println(String.format("%-15s= %s", exp.get(f), f));
				randomSel.add(f);
			}
			Set<Float> k2 = prob.keySet();
			List<Float> p = new ArrayList<>();
			// convert set of keys to list
			for (float l : k2) {
				p.add(l);
			}
			if (i == 0) {
				gp1Prob = p.get(0);
			} else {
				probGroup.add(p.get(0));
			}

			ArrayList<String> queryRes = new ArrayList<>();
			// generate 1000 result for query by randomly selecting result
			Random rand = new Random();
			for (int r = 0; r < 1000; r++) {
				int pos = rand.nextInt(modData.size() - 1);
				queryRes.add(modData.get(pos).get(3));
			}
			addRs(queryRes);

		}
		validate(probGroup);
	}

	public void validate(ArrayList<Float> probGroup2) {
		// validate results using Pr[A(D)=r]/Pr[A(D’)=r] <= exp (ε)
		System.out.println("\nValidating that each of the last 3 groups of results and the first group are " + ep
				+ "-indistinguishable.");
		for (float f : probGroup2) {
			float expVal = (float) Math.exp(ep);
			float p = gp1Prob / f;
			System.out.println("\n" + p + " <= " + expVal);
		}
	}

	public void addRs(ArrayList<String> res) {
		ArrayList<String> rList = new ArrayList<>();
		for (String f : res) {
			rList.add(f);
		}
		resList.add(rList);

	}

	public static void main(String[] args) throws IOException {
		hw2_2_exponential obj = new hw2_2_exponential();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		String fName = buf.readLine();
		obj.readData(fName);
		float arr[] = { 0.5f, 1.0f };
		for (int i = 0; i < arr.length; i++) {
			obj.ep = (float) arr[i];
			obj.generateRes();
			System.out.println();
		}
		obj.writeData(obj.resList);
	}
}
