package com.ghgande.j2mod.modbus.slave;

import java.util.List;

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.net.ModbusListener;
import com.ghgande.j2mod.modbus.net.ModbusListenerFactory;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalIn;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalOut;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.slave.vo.AddressMap;

public class ModbusSlaveInitializer {
	
	public static final int UNIT_ID = 0;
	public static final int PORT = 1470;

	public static void main(String[] args) throws Exception {
		System.out.println("j2mod Modbus Slave (Server) v0.97");
		System.setProperty("com.ghgande.modbus.debug", "true");
		startSlave(PORT, "/csv/init_data.csv");
	}
	
	private static void startSlave(final int port, final String file) {
		new Thread() {
			public void run() {
				ModbusListener listener = null;
				SimpleProcessImage spi = null;
				
				String address = "tcp:127.0.0.1:" + port;

				try {
					List<AddressMap> addressMapList = CSVReader.read(CSVReader.class.getResource(file).toURI());
					
					/*
					 * Create the process image for this test.
					 */
					spi = new SimpleProcessImage();
					
					for (AddressMap am : addressMapList) {
						System.out.println(am);
						if (am.getType().equals("DigitalIn")) {
							spi.addDigitalIn(am.getAddress(), new SimpleDigitalIn(am.getValue() == 1));
						} else if (am.getType().equals("DigitalOut")) {
							spi.addDigitalOut(am.getAddress(), new SimpleDigitalOut(am.getValue() == 1));
						} else if (am.getType().equals("HoldingRegister")) {
							spi.addRegister(am.getAddress(), new SimpleRegister(am.getValue()));
						} else if (am.getType().equals("InputRegister")) {
							spi.addInputRegister(am.getAddress(), new SimpleInputRegister(am.getValue()));
						}
					}

					// 2. create the coupler holding the image
					ModbusCoupler.getReference().setProcessImage(spi);
					ModbusCoupler.getReference().setMaster(false);
					ModbusCoupler.getReference().setUnitID(UNIT_ID);

					// 3. create a listener with 3 threads in pool
					listener = ModbusListenerFactory.createModbusListener(address);
					
					while (listener.isListening()) {
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException x) {
							listener.stop();
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					if (listener != null) {
						listener.stop();
					}
				}
			}
		}.start();
	}
}