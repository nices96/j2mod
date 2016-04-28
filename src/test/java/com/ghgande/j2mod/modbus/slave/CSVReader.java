package com.ghgande.j2mod.modbus.slave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.ghgande.j2mod.modbus.slave.vo.AddressMap;

public class CSVReader {

	public static List<AddressMap> read(URI uri) {
		BufferedReader br = null;
		String line = "";
		String delimeter = ",";
		
		List<AddressMap> modbusList = new ArrayList<AddressMap>();
		AddressMap modbus = null;
		String[] strArr = null;
		
		try {
			br = new BufferedReader(new FileReader(new File(uri)));
			
			// read first line as a column title
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				strArr = line.split(delimeter);
	
				modbus = new AddressMap();
				modbus.setType(strArr[0]);
				modbus.setAddress(Integer.parseInt(strArr[1]));
				modbus.setValue(Integer.parseInt(strArr[2]));
				
				if (strArr[3].startsWith("\"")) {
					String desc = "";
					for (int i = 3; i < strArr.length; i++) {
						if (!desc.equals("")) {
							desc += ",";
						}
						desc += strArr[i];
					}
					modbus.setDescription(desc.replaceAll("\"", ""));
				} else {
					modbus.setDescription(strArr[3]);
				}
				
				modbusList.add(modbus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return modbusList;
	}
	
	public static void main(String[] args) throws URISyntaxException {
		List<AddressMap> modbusList = CSVReader.read(CSVReader.class.getResource("/csv/pcs.csv").toURI());
		
		for (AddressMap m : modbusList) {
			System.out.println(m);
		}
	}
}
