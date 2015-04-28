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
			String input = "Message from " + peerID + ": " + br.readLine();
			byte[] m = input.getBytes();
			DatagramPacket messageOut = new DatagramPacket(m, m.length, group,
					port);
			s.send(messageOut);
			byte[] buffer = new byte[1000];
			for (int i = 0; i < 3; i++) {// get messages from others in group
				DatagramPacket messageIn = new DatagramPacket(buffer,
						buffer.length);
				s.receive(messageIn);
				System.out.println(new String(messageIn.getData()));
			}
			s.leaveGroup(group);
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