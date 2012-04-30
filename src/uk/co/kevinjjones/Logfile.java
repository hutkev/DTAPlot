/**
 Copyright 2011 Kevin J. Jones (http://www.kevinjjones.co.uk)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.co.kevinjjones;

import au.com.bytecode.opencsv.CSVReader;
import java.io.*;

public class Logfile {

	private static Logfile instance = null;
	private String status = "Waiting to load file";
	private double lambSumData[][] = null;
	private int lambCountData[][] = null;

	public static final int MapUnits=5;
	public static final int MapTotalUnits=60;
	public static final int MapMax=MapUnits*MapTotalUnits;
	public static final int RpmUnits=500;
	public static final int RpmTotalUnits=23;
	public static final int RpmMax=RpmUnits*RpmTotalUnits;
    
    public static void main(String[] args) throws Exception {
        byte[] data=readFile(new FileInputStream(new File(args[0])));
        Logfile l=Logfile.getInstance();
        l.loadFile(args[0], data);
        l.printAFRMap(System.out);
    }
    
    static byte[] readFile(FileInputStream fis) throws Exception {
        InputStream in = null;
        byte[] out = new byte[0];

        try {
            in = new BufferedInputStream(fis);

            // the length of a buffer can vary
            int bufLen = 20000 * 1024;
            byte[] buf = new byte[bufLen];
            byte[] tmp = null;
            int len = 0;
            while ((len = in.read(buf, 0, bufLen)) > 0) {
                // extend array
                tmp = new byte[out.length + len];

                // copy data
                System.arraycopy(out, 0, tmp, 0, out.length);
                System.arraycopy(buf, 0, tmp, out.length, len);

                out = tmp;
                tmp = null;
            }

        } finally {
            // always close the stream
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return out;
    }
    

	protected Logfile() {
		// Exists only to defeat instantiation.
	}

	public static Logfile getInstance() {
		if (instance == null) {
			instance = new Logfile();
		}
		return instance;
	}

	public String getStatus() {
		return status;
	}

	public void loadFile(String name, byte[] data) {
		status = "Loaded from " + name + ", " + data.length + " bytes.";

		oldt=100;
		CSVReader parser = new CSVReader(new StringReader(new String(data)),
				';');
		String[] nextLine;
		try {
			// Read first line to find RPM, MAP, LAMBDA & THROTTLE index
			int NUMindex = 0;
			int RPMindex = 0;
			int MAPindex = 0;
			int LAMBindex = 0;
			int THROTindex = 0;
			nextLine = parser.readNext();
			if (nextLine != null) {
				NUMindex = nextLine.length;
				for (int i = 0; i < NUMindex; i++) {
					if (nextLine[i].equals("RPM"))
						RPMindex = i;
					if (nextLine[i].equals("THROT"))
						THROTindex = i;
					if (nextLine[i].equals("MAP"))
						MAPindex = i;
					if (nextLine[i].equals("LAMB"))
						LAMBindex = i;
				}
				if (RPMindex == 0 || MAPindex == 0 || LAMBindex == 0
						|| THROTindex == 0) {
					status = "Missing RPM, MAP, LAMBDA or THROT column in data";
					return;
				}
			} else {
				status = "Missing header row, perhaps empty file";
			}

			int lines = 0;
			while ((nextLine = parser.readNext()) != null) {
				if (nextLine.length > 1) {

					if (nextLine.length > 1 && nextLine.length != NUMindex) {
						status = "Missing data on line " + lines;
						return;
					}

					recordData(nextLine[RPMindex], nextLine[THROTindex],
							nextLine[MAPindex], nextLine[LAMBindex]);

				}
				lines++;
			}
			status = "Loaded from " + name + ", " + lines + " lines.";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int oldt;

	private void recordData(String rpm, String throt, String map, String lamb) {
		if (lambSumData==null) {
			lambSumData = new double [23][60]; //rpm x map
			lambCountData = new int [23][60];
		}

		int t=Integer.parseInt(throt.trim());

		// Ignore trailing throttle
		if (t<5 || t>oldt) {
			oldt=t;
			return;
		}
		oldt=t;

		int r=Integer.parseInt(rpm.trim());
		int m=Integer.parseInt(map.trim());
		double l=Double.parseDouble(lamb.trim());

		int mapslot=(int)((((double)m)+2.5)/5);

		try {
			lambSumData[(r+250)/500][mapslot] +=l;
			lambCountData[(r+250)/500][mapslot] +=1;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Bug");
		}
	}

	public int getLambda(int rpm, int map) {
		if (lambSumData!=null && lambCountData[rpm][map]>5) {
			return (int)(100*lambSumData[rpm][map]/lambCountData[rpm][map]);
		} else {
			return 0;
		}
	}

	public boolean isLambdaData(int map) {
		for (int r=0; r<23; r++) {
			if (lambCountData[r][map]>5) {
				return true;
			}
		}
		return false;
	}


	public void printAFRMap(PrintStream ps) {
		ps.println("Logfile AFR vs. Target (0.88 <100kpa, 0.81 >= 150kpa");
		ps.println("====================================================");
		ps.print("       ");
		for (int r=0; r<23; r++) {
			ps.printf("%4d",r*5);
		}
		ps.println();
		for (int m=0; m<60; m++) {
			int target=88;
			if (m*5>150)
				target=81;
			else if (m*5>100)
				target=88-(int)(7*((float)(m*5)-100)/50);

			if (isLambdaData(m)) {
				ps.printf("%4d/%2d",m*5,target);
				for (int r=0; r<23; r++) {
					int l=getLambda(r,m);
					if (l>0)
						ps.printf("%4d",l);
					else
						ps.printf("    ");
				}
				ps.println();
			}
		}
		ps.println();
	}

}
