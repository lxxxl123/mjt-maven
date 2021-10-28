package com.chen.fast.jar;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class HostUtil {

	public static List<String> getAllIp() throws SocketException {
		List<String> ips = new ArrayList<>();
		Enumeration allNetInterfaces = null;
		allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
					.nextElement();
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					if (!ip.getHostAddress().equals("127.0.0.1"))
						ips.add(ip.getHostAddress());
				}
			}
		}
		return ips;
	}


}