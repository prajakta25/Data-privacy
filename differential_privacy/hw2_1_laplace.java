import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class hw2_1_laplace {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	private ArrayList<ArrayList<Float>> roundedList = new ArrayList<>();
	private ArrayList<ArrayList<Float>> resList = new ArrayList<>();
	private ArrayList<Float> overallRes = new ArrayList<>();
	float ep;
	int mew = 0;
	DecimalFormat df;

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

	public void writeData(ArrayList<ArrayList<Float>> queryRes) throws IOException {
		String fileNm = "hw2_1_datafile.txt";
		FileWriter writer = new FileWriter(fileNm);
		for (ArrayList<Float> data : queryRes) {
			for (Float f : data) {
				writer.write(f + System.lineSeparator());
			}

		}
		writer.close();
	}

	// find sensitivity for each dataset
	public float sensitivity(float size) {
		return (float) 1 / size;
	}

	// find b=(s/ep)
	public float findb(float s) {
		return (float) s / ep;
	}

	// query the average age of the records with age greater than 25 for different
	// combinations
	public ArrayList<Float> queryAvg(int val, ArrayList<ArrayList<String>> dataSet) {
		float avg = 0.0f;
		int sum = 0;
		ArrayList<Float> res = new ArrayList<Float>();
		res.clear();

		if (val == 0) { // query the average age of the records with age greater than 25
			ArrayList<Integer> age = new ArrayList<>();
			for (ArrayList<String> row : dataSet) {

				if (!(row.get(0).equals(""))) {
					int n = Integer.parseInt(row.get(0).trim());
					if (n > 25) {
						sum += n;
						age.add(n);
					}
				}

			}
			avg = (float) sum / age.size();
			res.add(avg);
			res.add((float) age.size());

		} else if (val == 1) { // query the average age of the records with age greater than 25 removing the
								// record with the oldest age
			ArrayList<Integer> age = new ArrayList<>();
			for (ArrayList<String> row : dataSet) {
				if (!(row.get(0).equals(""))) {
					age.add(Integer.parseInt(row.get(0).trim()));
				}
			}

			Collections.sort(age, Collections.reverseOrder());
			int oldestAge = age.get(0);
			for (int i = 0; i < age.size(); i++) {
				if (age.get(i) == oldestAge) {
					age.remove(i);
				} else {
					sum += age.get(i);
				}
			}
			avg = (float) sum / age.size();
			res.add(avg);
			res.add((float) age.size());

		} else if (val == 2) { // query the average age of the records with age greater than 25 removing any
								// record with age 26
			ArrayList<Integer> age = new ArrayList<>();
			for (ArrayList<String> row : dataSet) {
				if (!(row.get(0).equals(""))) {
					age.add(Integer.parseInt(row.get(0).trim()));
				}
			}
			for (int i = 0; i < age.size(); i++) {
				if (age.get(i) == 26) {
					age.remove(i);
				} else {
					sum += age.get(i);
				}
			}
			avg = (float) sum / age.size();
			res.add(avg);
			res.add((float) age.size());

		} else if (val == 3) { // query the average age of the records with age greater than 25 removing any
								// record with the youngest age
			ArrayList<Integer> age = new ArrayList<>();
			for (ArrayList<String> row : dataSet) {
				if (!(row.get(0).equals(""))) {
					age.add(Integer.parseInt(row.get(0).trim()));
				}
			}

			Collections.sort(age);
			int youngestAge = age.get(0);
			for (int i = 0; i < age.size(); i++) {
				if (age.get(i) == youngestAge) {
					age.remove(i);
				} else {
					sum += age.get(i);
				}
			}
			avg = (float) sum / age.size();
			res.add(avg);
			res.add((float) age.size());
		}

		return res;

	}

	public float sign(double d) {
		float ans = 0.00f;
		if (d < 0.0) {
			ans = -1.0f;
		} else {
			ans = 1.0f;
		}
		return ans;
	}

	// find global sensitivity
	public float findGS(ArrayList<Float> arr) {

		float gs = 0.0f;
		// GS=max|f(x)-f(x')|
		for (int i = 0; i < arr.size() - 1; i++) {
			float diff = Math.abs(arr.get(i) - arr.get(i + 1));
			if (gs < diff) {
				gs = diff;
			}
		}
		return gs;

	}

	// using the inverse of laplace distribution for easier calculations
	// F^{-1}(x) = mew - b*sign(x-0.5)*ln(1 - 2|x-0.5|).
	public float laplaceNoiseGenerator(float b, float oldVal) {
		Random r = new Random();
		int x = r.nextInt();
		// equation -> 1 - 2|x-0.5|
		float diff = (float) Math.abs((1 - 2 * (Math.abs(x - 0.5))));
		// calculate ln(diff)
		float logVal = (float) Math.log(diff);
		// find sign(x-0.5)
		float s = sign(x - 0.5);
		// finding the laplace random number
		float laplace = mew - b * s * logVal;
		if (oldVal == laplace) {
			laplaceNoiseGenerator(b, oldVal);
		}
		return laplace;

	}

	public void generateOP() throws IOException {
		// generate output results for different datasets
		float oldVal = 0.0f;
		ArrayList<Float> avgRes = new ArrayList<Float>();
		for (int v = 0; v < 4; v++) {
			ArrayList<Float> res = queryAvg(v, modData);
			float avg = res.get(0);
			avgRes.add(avg);
			overallRes.add(avg);
			float size = res.get(1);
			// find sensitivity
			float S = sensitivity(size);
			// find b
			float b = findb(S);
			ArrayList<Float> queryRes = new ArrayList<Float>();
			// generate 1,000 (random) results for the query
			int c = 0;
			while (c < 1000) {
				// add laplace noise
				float ans = (float) avg + laplaceNoiseGenerator(b, oldVal);
				oldVal = laplaceNoiseGenerator(b, oldVal);
				queryRes.add(ans);
				c++;

			}
			addRs(queryRes);
			System.out.println();
			System.out.println("For epsilon = " + ep + " and query = " + (v + 1) + " -> S = " + S + "\t, b = " + b
					+ "\t, Error = " + (2 * Math.pow(b, 2)));
			S = 0.0f;
			b = 0.0f;
			roundResult(queryRes);
			queryRes.clear();

		}
		System.out.println("\nGS = " + findGS(avgRes));
		validate(avgRes);

	}

	// round the result of each group
	public void roundResult(ArrayList<Float> res) {
		ArrayList<Float> rList = new ArrayList<Float>();
		for (float f : res) {
			rList.add(Float.parseFloat(df.format(f)));
		}
		roundedList.add(rList);

	}

	// combine 4 groups
	public void addRs(ArrayList<Float> res) {
		ArrayList<Float> rList = new ArrayList<Float>();
		for (float f : res) {
			rList.add(f);
		}
		resList.add(rList);

	}

	// Calculate probability Pr[A(D) belong to S]
	public float calProb(float a, int count) {
		return a / (float) count;
	}

	// Validate the rsult
	public void validate(ArrayList<Float> avgRes) {
		// find set of possible outputs from the given result
		Map<Float, Integer> freq = new HashMap<>();
		int orgCount = 0;
		ArrayList<Float> opSet = new ArrayList<>();
		int l = 0;
		float firstDataset = 0f;
		for (ArrayList<Float> f : roundedList) {
			boolean visited[] = new boolean[1000];
			Arrays.fill(visited, false);
			for (int i = 0; i < f.size(); i++) {
				if (visited[i] == true)
					continue;
				int count = 1;
				for (int j = i + 1; j < f.size(); j++) {
					if (Float.compare(f.get(i), f.get(j)) == 0) {
						visited[j] = true;
						count++;
					}
				}
				freq.put(f.get(i), count);
				opSet.add(f.get(i));
			}
			if (l == 0) {
				firstDataset = f.get(l);
				orgCount = freq.get(f.get(l));
			}
			l++;
		}
		Set<Float> k = freq.keySet();
		List<Integer> opS = new ArrayList<>();
		int sum = 0;
		for (float key : k) {
			sum += freq.get(key);
			if (!(Float.compare(key, firstDataset) == 0)) {
				opS.add(freq.get(key));
			}

		}

		float firstProb = calProb(orgCount, sum);
		// Compare the first result with the rest to validate epsilon differential
		// privacy
		System.out.println("\nValidating that each of the last 3 groups of results and the first group are " + ep
				+ "-indistinguishable.");
		for (int i = 0; i < opS.size(); i++) {
			float prob = calProb(opS.get(i), sum);
			float mul = (float) (Math.exp(ep) * prob);
			if (i < 2) {
				System.out.println("\n" + firstProb + " <= " + mul);
			} else {
				System.out.println("\n" + firstProb + " <= " + mul * 2);
				break;
			}
		}

	}

	public static void main(String[] args) throws IOException {
		hw2_1_laplace obj = new hw2_1_laplace();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		String fName = buf.readLine();
		obj.df = new DecimalFormat("#.##");
		obj.df.setRoundingMode(RoundingMode.CEILING);
		obj.readData(fName);
		float arr[] = { 0.5f, 1.0f };
		for (int i = 0; i < arr.length; i++) {
			obj.ep = arr[i];
			obj.generateOP();
			System.out.println();
		}
		obj.writeData(obj.resList);

	}
}
