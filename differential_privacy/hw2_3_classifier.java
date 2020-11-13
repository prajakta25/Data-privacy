import java.io.*;
import java.util.*;

public class hw2_3_classifier {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();

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

	// convert string to double
	public static double convToDouble(String s) {
		return Double.parseDouble(s);
	}

	// calculate accuracy percentage
	public static double accPer(ArrayList<String> actual, ArrayList<String> predicted) {
		double count = 0.0;
		for (int i = 0; i < actual.size(); i++) {
			if (actual.get(i).equals(predicted.get(i))) {
				count++;
			}
		}
		double ans = count / (double) actual.size();
		return ans * 100.0;

	}

	// get training data
	public static String[][] getTrainingData(ArrayList<ArrayList<String>> data) {
		int r = 120;
		int c = data.get(0).size();
		String dataset[][] = new String[r][c];
		int pos = 0;
		for (int i = 10; i < 50; i++) {
			for (int j = 0; j < c; j++) {
				dataset[pos][j] = data.get(i).get(j);
			}
			pos++;
		}
		for (int i = 60; i < 100; i++) {
			for (int j = 0; j < c; j++) {
				dataset[pos][j] = data.get(i).get(j);
			}
			pos++;
		}
		for (int i = 110; i < data.size() - 1; i++) {
			for (int j = 0; j < c; j++) {
				dataset[pos][j] = data.get(i).get(j);
			}
			pos++;
		}
		return dataset;

	}

	// get test data
	public static String[][] getTestData(ArrayList<ArrayList<String>> data, int m, int n) {
		int r = 10;
		int c = data.get(0).size();
		String dataset[][] = new String[r][c];
		int pos = 0;
		for (int i = m; i < n; i++) {
			for (int j = 0; j < c; j++) {
				dataset[pos][j] = data.get(i).get(j);
			}
			pos++;
		}

		return dataset;
	}

	// calculate gausian probability f(x) = (1 / sqrt(2 * PI) * sigma) *
	// exp(-((x-mean)^2 / (2 * stand_dev^2)))
	public static double calProb(double x, double mean, double dev) {
		double msq = Math.pow((x - mean), 2);
		double ssq = Math.pow((2 * dev), 2);
		double a = Math.sqrt(2 * Math.PI);
		double expo = Math.exp(-1 * (msq / ssq));
		return (1 / a * dev) * expo;
	}

	// calculate mean = sum(x)/n * count(x)
	public static double calMean(ArrayList<Double> values) {
		double sum = 0.00;
		for (double i : values) {
			sum += i;
		}
		double mean = sum / (double) values.size();
		return mean;
	}

	// calculate standard deviation = sqrt((sum i to N (x_i – mean(x))^2) / N-1)
	public static double calDev(ArrayList<Double> values) {
		double avg = calMean(values);
		double sum = 0.00;
		for (double i : values) {
			sum += Math.pow((i - avg), 2);
		}
		double variance = sum / (double) values.size();
		double dev = Math.sqrt(variance);
		return dev;

	}

	// Summarize data to get mean and deviation of each column
	public static ArrayList<ArrayList<Double>> summarizeData(double[][] dataset) {
		ArrayList<ArrayList<Double>> res = new ArrayList<>();
		for (int j = 0; j < dataset[0].length - 1; j++) {
			ArrayList<Double> arr = new ArrayList<>();
			ArrayList<Double> ans = new ArrayList<>();
			for (int i = 0; i < dataset.length; i++) {
				arr.add(dataset[i][j]);
			}
			ans.add(calMean(arr));
			ans.add(calDev(arr));
			ans.add((double) arr.size());
			res.add(ans);

		}
		return res;
	}

	// separate given data by class
	public static Map<String, ArrayList<ArrayList<Double>>> separateByClass(String[][] dataset) {

		Map<String, ArrayList<ArrayList<Double>>> separated = new HashMap<>();

		int flg = 0;
		for (int i = 0; i < dataset.length; i++) {
			ArrayList<ArrayList<Double>> arr = new ArrayList<>();
			String[] vector = dataset[i];
			String classVal = vector[dataset[i].length - 1].trim();
			if (!separated.containsKey(classVal)) {

				separated.put(classVal, arr);
				flg = 1;
			}
			ArrayList<Double> d = new ArrayList<>();
			for (int j = 0; j < vector.length - 1; j++) {
				d.add(convToDouble(vector[j]));
			}
			arr.add(d);
			if (flg == 1) {
				flg = 0;
			} else {
				separated.get(classVal).addAll(arr);
			}

		}
		return separated;

	}

	// summarize data by class
	public static Map<String, ArrayList<ArrayList<Double>>> summarizeByClass(String[][] dataset) {
		Map<String, ArrayList<ArrayList<Double>>> res = new HashMap<>();
		Map<String, ArrayList<ArrayList<Double>>> separated = separateByClass(dataset);

		Set<String> key = separated.keySet();
		for (String k : key) {
			ArrayList<ArrayList<Double>> sumData = new ArrayList<>();
			int ilen = separated.get(k).size();
			int jlen = separated.get(k).get(0).size();
			double data[][] = new double[ilen][jlen];
			for (int i = 0; i < separated.get(k).size(); i++) {
				for (int j = 0; j < separated.get(k).get(i).size(); j++) {
					data[i][j] = separated.get(k).get(i).get(j);
				}
			}
			sumData = summarizeData(data);
			res.put(k, sumData);
		}

		return res;

	}

	// Calculate the probability of class P(class|data) = P(X|class) * P(class)
	public static Map<String, Double> calClassProb(Map<String, ArrayList<ArrayList<Double>>> summerized, String[] d1) {
		double total_row = 0.00;
		Set<String> key = summerized.keySet();
		for (String k : key) {
			total_row += summerized.get(k).get(0).get(2);
		}
		Map<String, Double> prob = new HashMap<>();
		for (String k : key) {
			double value = summerized.get(k).get(0).get(2) / (float) total_row;
			prob.put(k, value);
			int i = 0;
			for (ArrayList<Double> ar : summerized.get(k)) {
				double mean = ar.get(0);
				double dev = ar.get(1);
				double cp = calProb(convToDouble(d1[i]), mean, dev);
				double ans = prob.get(k) * cp;
				prob.remove(k);
				prob.put(k, ans);
				i++;
			}

		}
		return prob;

	}

	// predict class
	public static String predictClass(Map<String, ArrayList<ArrayList<Double>>> data, String[] row) {
		Map<String, Double> result = calClassProb(data, row);
		String classVal = null;
		double prob = -1;
		Set<String> key = result.keySet();
		for (String k : key) {
			if (classVal == null || result.get(k) > prob) {
				classVal = k;
				prob = result.get(k);
			}
		}
		return classVal;
	}

	public static ArrayList<Double> callAlgo(ArrayList<ArrayList<String>> dataset) {
		ArrayList<Double> results = new ArrayList<>();
		// get training data
		String trainData[][] = getTrainingData(dataset);
		ArrayList<String[][]> testData = new ArrayList<>();
		testData.add(getTestData(dataset, 0, 10));
		testData.add(getTestData(dataset, 50, 60));
		testData.add(getTestData(dataset, 100, 110));
		for (int i = 0; i < testData.size(); i++) {
			ArrayList<String> predicted = NaiveBayesAlgo(trainData, testData.get(i));
			ArrayList<String> actual = new ArrayList<>();
			for (int j = 0; j < testData.get(i).length; j++) {
				int len = testData.get(i)[j].length - 1;
				actual.add(testData.get(i)[j][len]);
			}
			results.add(accPer(actual, predicted));
		}

		return results;
	}

	public static ArrayList<String> NaiveBayesAlgo(String trainData[][], String testData[][]) {
		ArrayList<String> predictedData = new ArrayList<>();
		Map<String, ArrayList<ArrayList<Double>>> summerized = summarizeByClass(trainData);
		for (int i = 0; i < testData.length; i++) {
			String row[] = new String[testData[i].length];
			for (int j = 0; j < testData[i].length; j++) {
				row[j] = testData[i][j];
			}
			predictedData.add(predictClass(summerized, row));
		}
		return predictedData;

	}

	public static void main(String arg[]) throws IOException {

		hw2_3_classifier obj = new hw2_3_classifier();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		String fName = buf.readLine();
		obj.readData(fName);

		
		// Test Data
		System.out.println("\nTraining : ");
		ArrayList<Double> values = callAlgo(obj.modData);
		System.out.println("Scores : " + values.toString());
		double accuracy = 0.0;
		for (Double v : values) {
			accuracy += v;
		}
		System.out.println("Accuracy = " + (accuracy / (double) values.size()) + "%");

		System.out.println("\nPrediction : ");
		Map<String, ArrayList<ArrayList<Double>>> summerized = summarizeByClass(getTrainingData(obj.modData));

		ArrayList<String[][]> testData = new ArrayList<>();
		testData.add(getTestData(obj.modData, 0, 10));
		testData.add(getTestData(obj.modData, 50, 60));
		testData.add(getTestData(obj.modData, 100, 110));
		System.out.println();
		for (int i = 0; i < testData.size(); i++) {
			for (int j = 0; j < testData.get(i).length; j++) {
				int len = testData.get(i)[j].length - 1;
				String r[] = new String[len];
				for (int k = 0; k < len; k++) {
					r[k] = testData.get(i)[j][k];
				}
				List<String> list = new ArrayList<String>(r.length);
				for (String s : r) {
					list.add(s);
				}
				System.out.println("Data : " + list.toString() + "\t Prediction: " + predictClass(summerized, r));
			}
			System.out.println();
		}

	}
}
