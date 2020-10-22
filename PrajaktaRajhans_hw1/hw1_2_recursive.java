import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class hw1_2_recursive {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	private int kanon, lVal;
	private float cVal;
	private String fName;
	private int genVals[][] = new int[4][2];
	private float[] cl = new float[3];

	public void readData(String filename) throws IOException {
		File adultData = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(adultData));
		ArrayList<ArrayList<String>> copyData = new ArrayList<>();
		String st;
		while ((st = br.readLine()) != null) {
			ArrayList<String> data = new ArrayList<String>();
			String arr[] = st.split(",");
			Collections.addAll(data, arr);
			copyData.add(data);
		}
		modData = copyData;
		originalData = copyData;
		br.close();

		cl[0] = 0.5f;
		cl[1] = 1;
		cl[2] = 2;
	}

	public void setGenVals(int a, int e, int m, int r) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					if (i == 0) {
						genVals[i][j] = a;
					} else if (i == 1) {
						genVals[i][j] = e;
					} else if (i == 2) {
						genVals[i][j] = m;
					} else if (i == 3) {
						genVals[i][j] = r;
					}
				} else {
					if (i == 0) {
						genVals[i][j] = 3;
					} else if (i == 1) {
						genVals[i][j] = 2;
					} else if (i == 2) {
						genVals[i][j] = 2;
					} else if (i == 3) {
						genVals[i][j] = 2;
					}
				}
			}
		}
	}

	public void writeData() throws IOException {

		FileWriter writer = new FileWriter("hw1_2_recOpData_" + cVal + "_.txt");
		for (ArrayList<String> str : modData) {
			writer.write(str.toString().replace("[", "").replace("]", "") + System.lineSeparator());
		}
		writer.close();

	}

	public void suppressData() {
		for (int i = 0; i < originalData.size() - 1; i++) {
			modData.get(i).set(1, "*");
			modData.get(i).set(2, "*");
			modData.get(i).set(4, "*");
			modData.get(i).set(7, "*");
			modData.get(i).set(9, "*");
			modData.get(i).set(10, "*");
			modData.get(i).set(11, "*");
			modData.get(i).set(12, "*");
			modData.get(i).set(13, "*");
			modData.get(i).set(14, "*");
		}
	}

	public void genAge(int val) {
		if (val == 1) {
			for (int i = 0; i < modData.size() - 1; i++) {
				if (Integer.parseInt(modData.get(i).get(0).trim()) <= 25) {
					modData.get(i).set(0, "0-25");
				} else if (Integer.parseInt(modData.get(i).get(0).trim()) > 25
						&& Integer.parseInt(modData.get(i).get(0).trim()) <= 50) {
					modData.get(i).set(0, "26-50");
				} else if (Integer.parseInt(modData.get(i).get(0).trim()) > 50
						&& Integer.parseInt(modData.get(i).get(0).trim()) <= 75) {
					modData.get(i).set(0, "51-75");
				} else if (Integer.parseInt(modData.get(i).get(0).trim()) > 75) {
					modData.get(i).set(0, ">75");
				}
			}
		} else if (val == 2) {
			for (int i = 0; i < modData.size() - 1; i++) {
				if (Integer.parseInt(modData.get(i).get(0).trim()) <= 50 || modData.get(i).get(0).trim().equals("0-25")
						|| modData.get(i).get(0).trim().equals("26-50")) {
					modData.get(i).set(0, "<=50");
				} else if (Integer.parseInt(modData.get(i).get(0).trim()) > 50
						|| modData.get(i).get(0).trim().equals("51-75") || modData.get(i).get(0).trim().equals(">75")) {
					modData.get(i).set(0, ">50");
				}

			}
		} else if (val == 3) {
			for (int i = 0; i < modData.size() - 1; i++) {
				modData.get(i).set(0, "*");
			}
		} else if (val == 0) {
			return;
		}

	}

	public void genEdu(int val) {
		if (val == 1) {
			for (int i = 0; i < modData.size() - 1; i++) {
				if (modData.get(i).get(3).trim().equals("Bachelors")
						|| modData.get(i).get(3).trim().equals("Some-college")
						|| modData.get(i).get(3).trim().equals("HS-grad")
						|| modData.get(i).get(3).trim().equals("Prof-school")
						|| modData.get(i).get(3).trim().equals("Assoc-acdm")
						|| modData.get(i).get(3).trim().equals("Assoc-voc")
						|| modData.get(i).get(3).trim().equals("Masters")
						|| modData.get(i).get(3).trim().equals("Doctorate")) {
					modData.get(i).set(3, "Higher Education");
				} else {
					modData.get(i).set(3, "K-12 Education");
				}
			}
		} else if (val == 2) {
			for (int i = 0; i < modData.size() - 1; i++) {
				modData.get(i).set(3, "*");
			}
		} else if (val == 0) {
			return;
		}

	}

	public void genMs(int val) {
		if (val == 1) {
			for (int i = 0; i < modData.size() - 1; i++) {
				if (modData.get(i).get(5).trim().equals("Married-civ-spouse")
						|| modData.get(i).get(5).trim().equals("Married-spouse-absent")
						|| modData.get(i).get(5).trim().equals("Married-AF-spouse")
						|| modData.get(i).get(5).trim().equals("Separated")) {
					modData.get(i).set(5, "Married");
				} else {
					modData.get(i).set(5, "Not Married");
				}
			}
		} else if (val == 2) {
			for (int i = 0; i < modData.size() - 1; i++) {
				modData.get(i).set(5, "*");
			}
		} else if (val == 0) {
			return;
		}
	}

	public void genRace(int val) {
		if (val == 1) {
			for (int i = 0; i < modData.size() - 1; i++) {
				if (modData.get(i).get(8).trim().equals("White") || modData.get(i).get(8).trim().equals("Black")
						|| modData.get(i).get(8).trim().equals("Other")) {
					modData.get(i).set(8, "White/Black/Other");
				} else {
					modData.get(i).set(8, "Asian-Pac-Islander/Amer-Indian-Eskimo");
				}
			}
		} else if (val == 2) {
			for (int i = 0; i < modData.size() - 1; i++) {
				modData.get(i).set(8, "Person");
			}
		} else if (val == 3) {
			for (int i = 0; i < modData.size() - 1; i++) {
				modData.get(i).set(8, "*");
			}
		} else if (val == 0) {
			return;
		}
	}

	// A frequency list of distinct sequence of values of PT[QI]
	public ArrayList<ArrayList<String>> getFreqList(ArrayList<ArrayList<String>> dataset) {

		// array for attribute positions
		// 0 - Age
		// 3 - Education
		// 5 - Martial status
		// 8 - Race
		int attrPos[] = { 0, 3, 5, 8 };
		ArrayList<ArrayList<String>> freqList = new ArrayList<>();
		int dataSize = dataset.size() - 1;
		boolean visited[] = new boolean[dataSize];
		Arrays.fill(visited, false);

		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				boolean flag = true;
				for (int k = 0; k < attrPos.length; k++) {
					if (!(dataset.get(i).get(attrPos[k]).trim().equals(dataset.get(j).get(attrPos[k]).trim()))) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					visited[j] = true;
					count++;
				}
			}
			ArrayList<String> data = new ArrayList<>();
			for (int l = 0; l < 15; l++) {
				data.add(dataset.get(i).get(l));
			}
			data.add(Integer.toString(count));
			freqList.add(data);
		}

		return freqList;

	}

	// A frequency list of distinct values of each attributes
	public ArrayList<ArrayList<ArrayList<String>>> getFreqListofAttr(ArrayList<ArrayList<String>> freqList) {

		// array for attribute positions
		// 0 - Age
		// 3 - Education
		// 5 - Marital status
		// 8 - Race
		ArrayList<ArrayList<ArrayList<String>>> attrFreqList = new ArrayList<>();
		ArrayList<ArrayList<String>> ageFreq = new ArrayList<>();
		ArrayList<ArrayList<String>> eduFreq = new ArrayList<>();
		ArrayList<ArrayList<String>> msFreq = new ArrayList<>();
		ArrayList<ArrayList<String>> raceFreq = new ArrayList<>();
		int dataSize = originalData.size() - 1;
		boolean visited[] = new boolean[dataSize];
		Arrays.fill(visited, false);
		// age
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
			ArrayList<String> data = new ArrayList<>();
			data.add(modData.get(i).get(0));
			data.add(Integer.toString(count));
			ageFreq.add(data);

		}

		// education
		Arrays.fill(visited, false);
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				if (modData.get(i).get(3).trim().equals(modData.get(j).get(3).trim())) {
					visited[j] = true;
					count++;
				}
			}
			ArrayList<String> data = new ArrayList<>();
			data.add(modData.get(i).get(3));
			data.add(Integer.toString(count));
			eduFreq.add(data);

		}

		// marital status
		Arrays.fill(visited, false);
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				if (modData.get(i).get(5).trim().equals(modData.get(j).get(5).trim())) {
					visited[j] = true;
					count++;
				}
			}
			ArrayList<String> data = new ArrayList<>();
			data.add(modData.get(i).get(5));
			data.add(Integer.toString(count));
			msFreq.add(data);

		}

		// race status
		Arrays.fill(visited, false);
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			int count = 1;
			for (int j = i + 1; j < dataSize; j++) {
				if (modData.get(i).get(8).trim().equals(modData.get(j).get(8).trim())) {
					visited[j] = true;
					count++;
				}
			}
			ArrayList<String> data = new ArrayList<>();
			data.add(modData.get(i).get(8));
			data.add(Integer.toString(count));
			raceFreq.add(data);

		}

		attrFreqList.add(ageFreq);
		attrFreqList.add(eduFreq);
		attrFreqList.add(msFreq);
		attrFreqList.add(raceFreq);

		return attrFreqList;

	}

	public void generalizeData(int aVal, int eVal, int mVal, int rVal) throws IOException {

		ArrayList<ArrayList<String>> freqSeqList;
		ArrayList<ArrayList<ArrayList<String>>> attrFreqList;
		int countk = 0;
		int alen, elen, mlen, rlen;
		freqSeqList = getFreqList(modData);
		int recordSize = freqSeqList.size();
		int thresholdCount = 0;
		// find the no. of tuples to be generalized
		for (int i = 0; i < recordSize; i++) {
			if (Integer.parseInt(freqSeqList.get(i).get(15).trim()) < kanon) {
				countk += Integer.parseInt(freqSeqList.get(i).get(15).trim());
			}
		}

		// find the count of distinct values in each attribute
		attrFreqList = getFreqListofAttr(freqSeqList);
		alen = attrFreqList.get(0).size();
		elen = attrFreqList.get(1).size();
		mlen = attrFreqList.get(2).size();
		rlen = attrFreqList.get(3).size();

		// while no. of tuples to be generalized is more than kanon and the
		// no. of distinct values of attributes is greater than
		// kanon generalize data

		while ((countk > kanon) && thresholdCount < 4) {
			// generalize attribute in freq list with most no of distinct values
			// generalize age
			if (alen > elen && alen > mlen && alen > rlen) {
				genAge(aVal);
				thresholdCount++;
			}
			// generalize marital status
			if (mlen > alen && mlen > elen && mlen > rlen) {
				genMs(mVal);
				thresholdCount++;

			}

			// generalize education
			if (elen > alen && elen > mlen && elen > rlen) {
				genEdu(eVal);
				thresholdCount++;

			}
			// generalize race
			if (rlen > alen && rlen > mlen && rlen > alen) {
				genRace(rVal);
				thresholdCount++;

			}

			countk = 0;
			freqSeqList = getFreqList(modData);
			recordSize = freqSeqList.size();

			// recalculate no. of tuples to be generalized
			for (int i = 0; i < recordSize; i++) {
				if (Integer.parseInt(freqSeqList.get(i).get(15).trim()) < kanon) {
					countk += Integer.parseInt(freqSeqList.get(i).get(15).trim());
				}
			}

			// recalculate count of distinct values in each attribute
			attrFreqList = getFreqListofAttr(freqSeqList);
			alen = attrFreqList.get(0).size();
			elen = attrFreqList.get(1).size();
			mlen = attrFreqList.get(2).size();
			rlen = attrFreqList.get(3).size();

			setGenVals(aVal, eVal, mVal, rVal);
		}

		// Suppressing sequences in frequency occurring less than k times
		int attrPos[] = { 0, 3, 5, 8 };
		for (int i = 0; i < freqSeqList.size() - 1; i++) {
			for (int j = 0; j < modData.size() - 1; j++) {
				boolean flag = true;
				for (int k = 0; k < attrPos.length; k++) {
					if (!(freqSeqList.get(i).get(attrPos[k]).trim().equals(modData.get(j).get(attrPos[k]).trim()))) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					if (Integer.parseInt(freqSeqList.get(i).get(15).trim()) < kanon) {
						modData.remove(j);
					}
				}
			}
		}

		checkRecursive(aVal, eVal, mVal, rVal);

	}

	public ArrayList<ArrayList<ArrayList<String>>> findEquiClass() throws IOException {
		int attrPos[] = { 0, 3, 5, 8 };
		ArrayList<ArrayList<ArrayList<String>>> equiClassList = new ArrayList<>();
		ArrayList<ArrayList<String>> freqList;
		int dataSize = modData.size() - 1;
		boolean visited[] = new boolean[dataSize];
		Arrays.fill(visited, false);

		// Find equivalence class in the generalized dataset
		for (int i = 0; i < dataSize; i++) {
			if (visited[i] == true)
				continue;
			ArrayList<String> data = new ArrayList<>();
			for (int l = 0; l < 15; l++) {
				data.add(modData.get(i).get(l));
			}
			freqList = new ArrayList<>();
			freqList.add(data);
			for (int j = i + 1; j < dataSize; j++) {
				boolean flag = true;
				for (int k = 0; k < attrPos.length; k++) {
					if (!(modData.get(i).get(attrPos[k]).trim().equals(modData.get(j).get(attrPos[k]).trim()))) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					visited[j] = true;
					ArrayList<String> data1 = new ArrayList<>();

					for (int l = 0; l < 15; l++) {
						data1.add(modData.get(j).get(l));
					}
					freqList.add(data1);

				}
			}
			equiClassList.add(freqList);

		}
		return equiClassList;
	}

	public void checkRecursive(int aVal, int eVal, int mVal, int rVal) throws IOException {

		ArrayList<ArrayList<ArrayList<String>>> equiClassList = findEquiClass();

		// get count of each SA in each equivalence table
		ArrayList<ArrayList<ArrayList<String>>> equiClassFrq = new ArrayList<>();
		ArrayList<ArrayList<String>> freqList;
		int dataSize = equiClassList.size() - 1;

		for (int i = 0; i < dataSize; i++) {
			// totalSum is summation(n(q*,s'))
			int totalSum = 0;
			boolean visited[] = new boolean[equiClassList.get(i).size()];
			Arrays.fill(visited, false);
			freqList = new ArrayList<>();
			for (int j = 0; j < equiClassList.get(i).size() - 1; j++) {

				if (visited[j] == true)
					continue;
				int count = 1;
				for (int k = j + 1; k < equiClassList.get(i).size() - 1; k++) {
					if (equiClassList.get(i).get(j).get(6).trim()
							.equals(equiClassList.get(i).get(k).get(6).trim()) == true) {
						count++;
						visited[k] = true;
					}
				}
				ArrayList<String> data = new ArrayList<>();
				for (int a = 0; a < 15; a++) {
					data.add(equiClassList.get(i).get(j).get(a));
				}
				data.add(Integer.toString(count));
				freqList.add(data);
				totalSum += count;
			}
			for (ArrayList<String> str : freqList) {
				str.add(Integer.toString(totalSum));
				break;
			}
			equiClassFrq.add(freqList);
		}

		// sort each equivalence class based on the count of SA

		for (int i = 0; i < equiClassFrq.size(); i++) {
			if (equiClassFrq.get(i).size() != 0) {
				for (int j = 0; j < equiClassFrq.get(i).size(); j++) {
					for (int k = 1; k < equiClassFrq.get(i).size() - j; k++) {
						if (Integer.parseInt(equiClassFrq.get(i).get(k - 1).get(15).trim()) < Integer
								.parseInt(equiClassFrq.get(i).get(k).get(15).trim())) {
							ArrayList<String> temp = equiClassFrq.get(i).get(k - 1);
							equiClassFrq.get(i).set(k - 1, equiClassFrq.get(i).get(k));
							equiClassFrq.get(i).set(k, temp);
						}
					}
				}

			}

		}

		boolean clRes[] = new boolean[equiClassFrq.size() - 1];
		ArrayList<Integer> arr = new ArrayList<>();

		// check if q* block satisfies recursive(c,l) - diversity
		// r1 < c(rl+....+rm)
		int count = 0;
		for (int i = 0; i < equiClassFrq.size() - 1; i++) {
			if (equiClassFrq.get(i).size() != 0) {
				int r1 = 0;
				int sumR = 0;
				for (int j = 0; j < equiClassFrq.get(i).size() - 1; j++) {
					if (j == 0) {
						r1 = Integer.parseInt(equiClassFrq.get(i).get(j).get(15).trim());
					} else if (j >= lVal - 1) {
						sumR += Integer.parseInt(equiClassFrq.get(i).get(j).get(15).trim());
						;
					}
				}
				if (r1 < cVal * sumR) {
					clRes[i] = true;
				} else {
					clRes[i] = false;
					arr.add(i);
					count++;
				}
			}
		}

		ArrayList<ArrayList<String>> notSatisfiedList = new ArrayList<>();
		if (!arr.isEmpty()) {
			for (int i = 0; i < arr.size(); i++) {
				for (int j = 0; j < equiClassFrq.get(i).size(); j++) {
					notSatisfiedList.add(equiClassFrq.get(i).get(j));
				}
			}
		}

		if (count >= kanon - 1) {
			modData.clear();
			readData(fName);
			if (cVal == cl[0]) {
				generalizeData(aVal, eVal, mVal, rVal + 2);
			} else if (cVal == cl[1]) {
				generalizeData(aVal + 1, eVal, mVal, rVal);
			} else {
				generalizeData(aVal, eVal, mVal, rVal + 1);
			}
		}

		suppressData();
		writeData();

	}

	public void calculateDistortion() {
		float sum = 0f;
		for (int i = 0; i < 4; i++) {
			sum += (float) genVals[i][0] / genVals[i][1];
		}
		float distortion = (float) sum / 4;
		System.out.println("Distortion = " + distortion);
	}

	public void calculatePrecision() {
		int na = 4;
		int ptSize = modData.size();
		float sum = 0f;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < ptSize; j++) {
				sum += (float) genVals[i][0] / genVals[i][1];
			}
		}
		float precision = 1 - (float) (sum / (na * ptSize));
		System.out.println("Precision = " + precision);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		hw1_2_recursive obj = new hw1_2_recursive();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		obj.fName = buf.readLine();
		System.out.println("Enter value of k");
		obj.kanon = Integer.parseInt(buf.readLine());
		System.out.println("Enter value of l");
		obj.lVal = Integer.parseInt(buf.readLine());
		System.out.println("Enter value of c");
		obj.cVal = Float.parseFloat(buf.readLine());
		obj.readData(obj.fName);
		obj.suppressData();
		obj.generalizeData(1, 1, 1, 0);
		obj.calculateDistortion();
		obj.calculatePrecision();
	}

}
