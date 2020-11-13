import java.io.*;
import java.util.*;

public class hw1_1_anonymity {
	private ArrayList<ArrayList<String>> originalData = new ArrayList<>();
	private ArrayList<ArrayList<String>> modData = new ArrayList<>();
	private int k1anon, k2anon;

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

	public void writeData() throws IOException {
		FileWriter writer = new FileWriter("hw1_1_outputData.txt");
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
		// age
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

	public void generalizeData(int aVal, int eVal, int mVal, int rVal) {

		ArrayList<ArrayList<String>> freqSeqList;
		ArrayList<ArrayList<ArrayList<String>>> attrFreqList;
		int countk1 = 0;
		int countk2 = 0;
		int alen, elen, mlen, rlen;
		freqSeqList = getFreqList(modData);
		int recordSize = freqSeqList.size();

		int thresholdCount = 0;
		// find the no. of tuples to be generalized
		for (int i = 0; i < recordSize; i++) {
			// no. of tuples to be generalized for k1=10
			if ((freqSeqList.get(i).get(14).trim().equals("<=50K")
					&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k1anon)) {
				countk1 += Integer.parseInt(freqSeqList.get(i).get(15).trim());
			} // no. of tuples to be generalized for k2=5
			else if ((freqSeqList.get(i).get(14).trim().equals(">50K")
					&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k2anon)) {
				countk2 += Integer.parseInt(freqSeqList.get(i).get(15).trim());
			}
		}

		// find the count of distinct values in each attribute
		attrFreqList = getFreqListofAttr(freqSeqList);
		alen = attrFreqList.get(0).size();
		elen = attrFreqList.get(1).size();
		mlen = attrFreqList.get(2).size();
		rlen = attrFreqList.get(3).size();

		// while no. of tuples to be generalized is more than k1anon or k2anon and the
		// no. of distinct values of attributes is greater than
		// k2anon generalize data

		while ((countk1 > k1anon || countk2 > k2anon) && thresholdCount < 4) {
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

			countk1 = 0;
			countk2 = 0;
			freqSeqList = getFreqList(modData);
			recordSize = freqSeqList.size();

			// recalculate no. of tuples to be generalized
			for (int i = 0; i < recordSize; i++) {
				if ((freqSeqList.get(i).get(14).trim().equals("<=50K")
						&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k1anon)) {
					countk1 += Integer.parseInt(freqSeqList.get(i).get(15).trim());
				} else if ((freqSeqList.get(i).get(14).trim().equals(">50K")
						&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k2anon)) {
					countk2 += Integer.parseInt(freqSeqList.get(i).get(15).trim());
				}
			}

			// recalculate count of distinct values in each attribute
			attrFreqList = getFreqListofAttr(freqSeqList);
			alen = attrFreqList.get(0).size();
			elen = attrFreqList.get(1).size();
			mlen = attrFreqList.get(2).size();
			rlen = attrFreqList.get(3).size();
		}

		// Suppressing sequences in frequency occurring less than k1 or k2 times
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
					if ((freqSeqList.get(i).get(14).trim().equals("<=50K")
							&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k1anon)
							|| (freqSeqList.get(i).get(14).trim().equals(">50K")
									&& Integer.parseInt(freqSeqList.get(i).get(15).trim()) < k2anon)) {
						modData.remove(j);
					}
				}
			}
		}

		calculateDistortion(aVal, eVal, mVal, rVal);
		calculatePrecision(aVal, eVal, mVal, rVal);
	}

	public int[][] setGenLevel(int aVal, int eVal, int mVal, int rVal) {

		int level[][] = new int[4][3];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (j == 0) {
					if (i == 0) {
						level[i][j] = aVal;
					} else if (i == 1) {
						level[i][j] = eVal;
					} else if (i == 2) {
						level[i][j] = mVal;
					} else if (i == 3) {
						level[i][j] = rVal;
					}
				} else if (j == 1) {
					if (i == 0) {
						level[i][j] = 3; // age
					} else if (i == 1) {
						level[i][j] = 2; // education
					} else if (i == 2) {
						level[i][j] = 2; // marital status
					} else if (i == 3) {
						level[i][j] = 3; // race
					}
				}
			}
		}
		return level;

	}

	public void calculateDistortion(int aVal, int eVal, int mVal, int rVal) {
		int[][] levlAttr = setGenLevel(aVal, eVal, mVal, rVal);
		float sum = 0f;
		for (int i = 0; i < 4; i++) {
			sum += (float) levlAttr[i][0] / levlAttr[i][1];
		}
		float distortion = (float) sum / 4;
		System.out.println("Distortion = " + distortion);
	}

	public void calculatePrecision(int aVal, int eVal, int mVal, int rVal) {
		int levlAttr[][] = setGenLevel(aVal, eVal, mVal, rVal);
		int na = 4;
		int ptSize = modData.size();
		float sum = 0f;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < ptSize; j++) {
				sum += (float) levlAttr[i][0] / levlAttr[i][1];
			}
		}

		float precision = 1 - ((float) (sum / (na * ptSize)));
		System.out.println("Precision " + precision);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		hw1_1_anonymity obj = new hw1_1_anonymity();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter full path for the dataset file");
		String fName = buf.readLine();
		System.out.println("Enter value of k1");
		obj.k1anon = Integer.parseInt(buf.readLine());
		System.out.println("Enter value of k2");
		obj.k2anon = Integer.parseInt(buf.readLine());
		obj.readData(fName);
		obj.generalizeData(1, 1, 1, 0);
		obj.suppressData();
		obj.writeData();

	}

}
