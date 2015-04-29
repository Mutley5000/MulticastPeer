import java.net.*;
import java.io.*;

public class MulticastPeer {
	public static void main(String args[]) {
		// args give message contents and destination multicast group
		// (e.g."228.5.6.7")
		MulticastSocket s = null;
		try {
			InetAddress group = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);
			String peerID = args[2];
			s = new MulticastSocket(port);
			s.joinGroup(group);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			boolean inGroup = true;
			
			while (inGroup == true) {
				String input = br.readLine();
				String message;
				byte[] m;
				DatagramPacket messageOut;
				byte[] buffer;
				DatagramPacket messageIn;
				
				
				if (input.equals("")) {
					message = peerID + " has left.";
					m = message.getBytes();
					messageOut = new DatagramPacket(m, m.length, group, port);
					s.send(messageOut);
					buffer = new byte[1000];
					messageIn = new DatagramPacket(buffer, buffer.length);
					s.receive(messageIn);
					System.out.println(new String(messageIn.getData()));
					
					s.leaveGroup(group);
					inGroup = false;
				}
				
				else {
					message = "Message from " + peerID + ": " + input;
					m = message.getBytes();
					messageOut = new DatagramPacket(m, m.length, group, port);
					s.send(messageOut);
					buffer = new byte[1000];
					messageIn = new DatagramPacket(buffer, buffer.length);
					s.receive(messageIn);
					System.out.println(new String(messageIn.getData()));
				}
			}
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (s != null)
				s.close();
		}
	}
}