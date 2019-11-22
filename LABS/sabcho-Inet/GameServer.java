//Sabrina Chowdhury
import java.io.*;
import java.net.*;

public class GameServer{

	private ServerSocket ss;
	private int numPlayers;
	private int maxPlayers;

	private Socket p1Socket;
	private Socket p2Socket;
	private ReadFromClient p1ReadRunnable;
	private ReadFromClient p2ReadRunnable;
	private WriteToClient p1WriteRunnable;
	private WriteToClient p2WriteRunnable;

	private double p1x, p1y, p2x, p2y;
	private int touchedFlag;
	private int myPoints, enemyPoints;
	private int[] touchedList;
	private boolean touched1;
	private boolean touched2;
	/*
	private boolean touched3;
	private boolean touched4;
	private boolean touched5;
	private boolean touched6;
	private boolean touched7;
	*/


	public GameServer(){
		System.out.println("-----GAME SERVER-----");
		numPlayers = 0;
		maxPlayers = 2;

		p1x = 100;
		p1y = 400;
		p2x = 490;
		p2y = 400;

		try{
			ss = new ServerSocket(45371);

		}catch(IOException ex){
			System.out.println("IOException from constructor");
		}
	}

	public void acceptConnections(){
		try{
			System.out.println("Waiting for connections...");

			while(numPlayers < maxPlayers){
				Socket s = ss.accept();
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());

				numPlayers++;
				out.writeInt(numPlayers);
				System.out.println("Player #" + numPlayers + " has connected");

				ReadFromClient rfc = new ReadFromClient(numPlayers, in);
				WriteToClient wtc = new WriteToClient(numPlayers,out);

				if(numPlayers == 1){
					//player 1 want to send to 2
					
					p1Socket = s;
					p1ReadRunnable = rfc;
					p1WriteRunnable = wtc;

				} else{
					
					p2Socket = s;
					p2ReadRunnable = rfc;
					p2WriteRunnable = wtc;
					p1WriteRunnable.sendStartMsg();
					p2WriteRunnable.sendStartMsg();

					//when player 2 has connected we can start the threads for reading an writing to the client
					Thread readThread1 = new Thread(p1ReadRunnable);
					Thread readThread2 = new Thread(p2ReadRunnable);
					readThread1.start();
					readThread2.start();
					Thread writeThread1 = new Thread(p1WriteRunnable);
					Thread writeThread2 = new Thread(p2WriteRunnable);
					writeThread1.start();
					writeThread2.start();


				}

			}

			System.out.println("No longer acception connections");

		}catch(IOException ex){
			System.out.println("IOException from acceptConnections");
		}
	}

	private class ReadFromClient implements Runnable{
		private int playerID;
		private DataInputStream dataIn;

		public ReadFromClient(int pid, DataInputStream in){
			playerID = pid;
			dataIn = in;
			System.out.println("RFC" + playerID + "Runnable created");
		}

		public void run(){

			try{
				while(true){
					if(playerID ==1){
						
						p1x = dataIn.readDouble();
						p1y = dataIn.readDouble();
						touchedFlag = dataIn.readInt();
						myPoints = dataIn.readInt();
						//touched1 = dataIn.readBoolean();
						//touched2 = dataIn.readBoolean();
						/*
						touched3 = dataIn.readBoolean();
						touched4 = dataIn.readBoolean();
						touched5 = dataIn.readBoolean();
						touched6 = dataIn.readBoolean();
						touched7 = dataIn.readBoolean();
						*/
						//touchedList = dataIn.readObject();

					}else{
						
						p2x = dataIn.readDouble();
						p2y = dataIn.readDouble();
						touchedFlag = dataIn.readInt();
						enemyPoints = dataIn.readInt();

						//touched1 = dataIn.readBoolean();
						//touched2 = dataIn.readBoolean();
						/*
						touched3 = dataIn.readBoolean();
						touched4 = dataIn.readBoolean();
						touched5 = dataIn.readBoolean();
						touched6 = dataIn.readBoolean();
						touched7 = dataIn.readBoolean();
						*/
						//touchedList = dataIn.readObject();

					}
				}

			}catch(IOException ex){
				System.out.println("IOException from rfc run");
			}

		}

	}

	private class WriteToClient implements Runnable{
		private int playerID;
		private DataOutputStream dataOut;

		public WriteToClient(int pid, DataOutputStream out){
			playerID = pid;
			dataOut = out;
			System.out.println("WTC" + playerID + "Runnable created");
		}

		public void run(){

			try{
				while(true){
					if(playerID ==1){

						dataOut.writeDouble(p2x);
						dataOut.writeDouble(p2y);
						dataOut.writeInt(touchedFlag);
						dataOut.writeInt(enemyPoints);
						//dataOut.writeBoolean(touched1);
						//dataOut.writeBoolean(touched2);
						/*
						dataOut.writeBoolean(touched3);
						dataOut.writeBoolean(touched4);
						dataOut.writeBoolean(touched5);
						dataOut.writeBoolean(touched6);
						dataOut.writeBoolean(touched7);
						*/
						//dataOut.writeObject(touchedList);
						dataOut.flush();
					}else{

						dataOut.writeDouble(p1x);
						dataOut.writeDouble(p1y);
						dataOut.writeInt(touchedFlag);
						dataOut.writeInt(myPoints);
						//dataOut.writeBoolean(touched1);
						//dataOut.writeBoolean(touched2);
						/*
						dataOut.writeBoolean(touched3);
						dataOut.writeBoolean(touched4);
						dataOut.writeBoolean(touched5);
						dataOut.writeBoolean(touched6);
						dataOut.writeBoolean(touched7);
						*/
						//dataOut.writeObject(touchedList);
						dataOut.flush();

					}
					try {
						Thread.sleep(25);
					} catch(InterruptedException ex){
						System.out.println("interrupted exception from wtc run");
					}				
				}

			}catch(IOException ex){
				System.out.println("IOException from wtc run");
			}
			
		}

		public void sendStartMsg(){
			try{
				dataOut.writeUTF("We now have 2 players. Go!");

			}catch(IOException ex){
				System.out.println("IOException from sendstartmsg()");
			}
		}

	}

		public static void main(String[] args){
			GameServer gs = new GameServer();
			gs.acceptConnections();

		
	}

}

