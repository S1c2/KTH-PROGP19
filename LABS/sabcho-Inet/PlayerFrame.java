//Sabrina Chowdhury

/*

ok så det som ska göras
jag ska ta min array och kalla readInt flera gånger för att fylla den manuellt
använda serializable
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import javax.swing.JOptionPane;

public class PlayerFrame extends JFrame{

	private int width, height;
	private Container contentPane;
	private PlayerSprite me;
	private PlayerSprite enemy;

	private PlayerSprite reward1;
	private PlayerSprite reward2;
	/*
	private PlayerSprite reward3;
	private PlayerSprite reward4;
	private PlayerSprite reward5;
	private PlayerSprite reward6;
	private PlayerSprite reward7;

	*/

	private PlayerSprite hinder1;
	private PlayerSprite hinder2;
	private PlayerSprite hinder3;
	private PlayerSprite hinder4;

	private List<PlayerSprite> rewardList;

	int[] touchedList = new int[7];
	private List<PlayerSprite> hinderList;


	private DrawingComponent dc;
	private Timer animationTimer;
	private boolean up,down,left,right;
	private Socket socket;
	private int playerID;
	private ReadFromServer rfsRunnable;
	private WriteToServer wtsRunnable;
	private double spriteSize;

	private int touchedFlag = 0; // siffran är den rewarden man precis tagit
	private boolean touched1 = false;
	private boolean touched2 = false;
	private boolean touched3 = false;
	private boolean touched4 = false;
	private boolean touched5 = false;
	private boolean touched6 = false;
	private boolean touched7 = false;

	private int myPoints = 0;
	private int enemyPoints = 0;

	private HashSet<Integer> pointSet = new HashSet<Integer>();


	public PlayerFrame(int w, int h){
		width = w;
		height = h;
		up = false;
		down = false;
		left = false;
		right = false;
		spriteSize = 50;

		rewardList = new ArrayList<PlayerSprite>();
		rewardList.add(reward1);
		rewardList.add(reward2);
		/*
		rewardList.add(reward3);
		rewardList.add(reward4);
		rewardList.add(reward5);
		rewardList.add(reward6);
		rewardList.add(reward7);
		*/

		hinderList = new ArrayList<PlayerSprite>();
		hinderList.add(hinder1);
		hinderList.add(hinder2);
		hinderList.add(hinder3);
		hinderList.add(hinder4);

	}

	public void setUpGUI(){
		contentPane = this.getContentPane();
		this.setTitle("Player #" + playerID);
		contentPane.setPreferredSize(new Dimension(width,height));
		createSprites();
		createRewards();
		createHinders();
		dc = new DrawingComponent();
		contentPane.add(dc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

		setUpKeyListener();

		setUpAnimationTimer();
		
	}

	private void createSprites(){
		if(playerID == 1){
			me = new PlayerSprite(100,400,spriteSize,Color.BLUE);
			enemy = new PlayerSprite(490,400,spriteSize,Color.RED);
		}
		if(playerID == 2){
			enemy = new PlayerSprite(100,400,spriteSize,Color.BLUE);
			me = new PlayerSprite(490,400,spriteSize,Color.RED);
		}
		
	}

	private void createRewards(){
		reward1 = new PlayerSprite(100,100,spriteSize,Color.GREEN);
		reward2 = new PlayerSprite(370,400,spriteSize,Color.GREEN);
		/*
		reward3 = new PlayerSprite(200,300,spriteSize,Color.GREEN);
		reward4 = new PlayerSprite(300,300,spriteSize,Color.GREEN);
		reward5 = new PlayerSprite(350,200,spriteSize,Color.GREEN);
		reward6 = new PlayerSprite(100,200,spriteSize,Color.GREEN);
		reward7 = new PlayerSprite(400,150,spriteSize,Color.GREEN);
		*/
	}

	private void createHinders(){
		hinder1 = new PlayerSprite(400,70,spriteSize,Color.BLACK);
		hinder2 = new PlayerSprite(230,200,spriteSize,Color.BLACK);
		hinder3 = new PlayerSprite(380,270,spriteSize,Color.BLACK);
		hinder4 = new PlayerSprite(70,270,spriteSize,Color.BLACK);
	}

	private void setUpAnimationTimer(){
		int interval = 10;
		//myPoints = pointSet.size();
		//System.out.println(myPoints);
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				eraseTouchedReward();
				
				
				double speed = 5;
				if(!collisionDetected()){

				if(up){
					me.moveV(-speed);
				} else if(down){
					me.moveV(speed);
				}else if(left){
					me.moveH(-speed);
				}else if(right){
					me.moveH(speed);
				}
				dc.repaint();
			}if(collisionDetected()||touchedHinder()){

				if(up){
					me.moveV(2*speed);
				} else if(down){
					me.moveV(-2*speed);
				}else if(left){
					me.moveH(2*speed);
				}else if(right){
					me.moveH(-2*speed);
				}
				dc.repaint();

			}if(myPoints == 1 && enemyPoints ==1){
			
			JOptionPane.showMessageDialog(null, "Bra jobbat!");
			System.exit(0);
		
			}
		}

		};
		animationTimer = new Timer(interval,al);
		animationTimer.start();
	}

	public boolean collisionDetected(){
		return (me.getX() < enemy.getX() + spriteSize &&
   		me.getX() + spriteSize > enemy.getX() &&
   		me.getY() < enemy.getY() + spriteSize &&
   		me.getY() + spriteSize > enemy.getY()); 
	}

	public int touchedReward(){

		int touchedNr = 0;
		int result = 0;
		double rewardX = 0;
		double rewardY = 0;
		List<Double> rewardListX = new ArrayList<Double>();
		rewardListX.add(reward1.getX());
		rewardListX.add(reward2.getX());
		/*
		rewardListX.add(reward3.getX());
		rewardListX.add(reward4.getX());
		rewardListX.add(reward5.getX());
		rewardListX.add(reward6.getX());
		rewardListX.add(reward7.getX());
		*/
		List<Double> rewardListY = new ArrayList<Double>();

		rewardListY.add(reward1.getY());
		rewardListY.add(reward2.getY());

		/*
		rewardListY.add(reward3.getY());
		rewardListY.add(reward4.getY());
		rewardListY.add(reward5.getY());
		rewardListY.add(reward6.getY());
		rewardListY.add(reward7.getY());

		*/

		for(int i = 0; i<rewardListX.size();i++){
				
				rewardX = rewardListX.get(i);
				rewardY = rewardListY.get(i);
				if(me.getX() < rewardX + spriteSize &&
   				me.getX() + spriteSize > rewardX &&
   				me.getY() < rewardY + spriteSize &&
   				me.getY() + spriteSize > rewardY){

   					

   					result = touchedNr + 1;



				}
			touchedNr++;

			
		}

		return result;
	}

		public boolean touchedHinder(){

		boolean result = false;
		double hinderX = 0;
		double hinderY = 0;
		List<Double> hinderListX = new ArrayList<Double>();
		hinderListX.add(hinder1.getX());
		hinderListX.add(hinder2.getX());
		hinderListX.add(hinder3.getX());
		hinderListX.add(hinder4.getX());

		List<Double> hinderListY = new ArrayList<Double>();
		hinderListY.add(hinder1.getY());
		hinderListY.add(hinder2.getY());
		hinderListY.add(hinder3.getY());
		hinderListY.add(hinder4.getY());

		for(int i = 0; i<hinderListX.size();i++){
				
				hinderX = hinderListX.get(i);
				hinderY = hinderListY.get(i);
				if(me.getX() < hinderX + spriteSize &&
   				me.getX() + spriteSize > hinderX &&
   				me.getY() < hinderY + spriteSize &&
   				me.getY() + spriteSize > hinderY){

   					

   					result = true;


				}

		}

		return result;
	}

	public void eraseTouchedReward(){
		int touchedNr = touchedReward();
		PlayerSprite spriteTouched;
		//System.out.println(touchedNr);
		touchedFlag = touchedNr;
		checkTouchedFlag();

		if(touchedNr > 0){
			//&& touchedList[touchedNr-1]==0
		spriteTouched = rewardList.get(touchedNr-1);
		
		pointSet.add(touchedFlag);
		//System.out.println(pointSet);
		myPoints = pointSet.size();
	
	
		
	}


	}





	public void checkTouchedFlag(){
		if(touchedFlag == 1){
			touched1 = true;
			touchedList[0]=1;

		}
		else if(touchedFlag == 2){
			touched2 = true;
			touchedList[1]=1;
		}
		/*
		else if(touchedFlag == 3){
			touched3 = true;
			touchedList[2]=1;
		}
		else if(touchedFlag == 4){
			touched4 = true;
			touchedList[3]=1;
		}
		else if(touchedFlag == 5){
			touched5 = true;
			touchedList[4]=1;
		}
		else if(touchedFlag == 6){
			touched6 = true;
			touchedList[5]=1;
		}
		else if(touchedFlag == 7){
			touched7 = true;
			touchedList[6]=1;
		}
		*/
	}


	private void setUpKeyListener(){
		KeyListener kl = new KeyListener(){

			public void keyTyped(KeyEvent ke){
			}

			

			public void keyPressed(KeyEvent ke){
				int keyCode = ke.getKeyCode();


				
				switch(keyCode){
					case KeyEvent.VK_UP:
						
						up = true;
						System.out.println("up");
				
						break;
					
					case KeyEvent.VK_DOWN:
						
						down = true;
				
						break;
					case KeyEvent.VK_LEFT:
						
						left = true;
					
						break;
					case KeyEvent.VK_RIGHT:
					
						right = true;
				
						break;
					}

			}
			

			public void keyReleased(KeyEvent ke){

				int keyCode = ke.getKeyCode();

				switch(keyCode){
					case KeyEvent.VK_UP:
						up = false;
						break;
					case KeyEvent.VK_DOWN:
						down = false;
						break;
					case KeyEvent.VK_LEFT:
						left = false;
						break;
					case KeyEvent.VK_RIGHT:
						right = false;
						break;
				}

			}

		};

		contentPane.addKeyListener(kl);
		contentPane.setFocusable(true);
	}

	private void connectToServer(){

		try{
			socket = new Socket("localhost",45371);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			playerID = in.readInt();
			System.out.println("You are player #" + playerID);
			if(playerID == 1){
				System.out.println("Waiting for player #2 to connect");
			}

			rfsRunnable = new ReadFromServer(in);
			wtsRunnable = new WriteToServer(out);
			rfsRunnable.waitForStartMsg();

		}catch(IOException ex){
			System.out.println("IOException from connectToServer");
		}
	}

	private class DrawingComponent extends JComponent{
		protected void paintComponent(Graphics g){
			Graphics2D g2d = (Graphics2D) g;
			contentPane.requestFocus();
			enemy.drawSprite(g2d);
			me.drawSprite(g2d);

			checkTouchedFlag();
		
		if(touched1 != true){
			reward1.drawSprite(g2d);
		}
		if(touched2 != true){
			reward2.drawSprite(g2d);
		}
		/*
		if(touched3 != true){
			reward3.drawSprite(g2d);
		}
		if(touched4 != true){
			reward4.drawSprite(g2d);
		}
		if(touched5 != true){
			reward5.drawSprite(g2d);
		}
		if(touched6 != true){
			reward6.drawSprite(g2d);
		}
		if(touched7 != true){
			reward7.drawSprite(g2d);
		}

		*/

		hinder1.drawSprite(g2d);
		hinder2.drawSprite(g2d);
		hinder3.drawSprite(g2d);
		hinder4.drawSprite(g2d);

			

		}
	}

	private class ReadFromServer implements Runnable {

		private DataInputStream dataIn;

		public ReadFromServer(DataInputStream in){
			dataIn = in;
			System.out.println("RFS Runnable created");
		}

		public void run(){

			try{

				while(true){
					
					if(enemy != null){//avoiding a null pointer exception
					
					enemy.setX(dataIn.readDouble());
					enemy.setY(dataIn.readDouble());
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
				System.out.println("IOException from rfs run()");
			}

		}

		public void waitForStartMsg(){
			try{
				String startMsg = dataIn.readUTF();
				System.out.println("Message from server: " + startMsg);
				Thread readThread = new Thread(rfsRunnable);
				Thread writeThread = new Thread(wtsRunnable);
				readThread.start();
				writeThread.start();

			}catch(IOException ex){
				System.out.println("IOException from waitforstartmsg()");

			}

		}

	}

	private class WriteToServer implements Runnable {

		private DataOutputStream dataOut;


		public WriteToServer(DataOutputStream out){
			dataOut = out;
			System.out.println("WTS Runnable created");
		}

		public void run(){

			try{

				while(true){
					
					if(me !=null){ //we wait for the me object to be created before we execute
						
						dataOut.writeDouble(me.getX());
						dataOut.writeDouble(me.getY());
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

					try{
						Thread.sleep(10);

					}catch(InterruptedException ex){
						System.out.print("intterrupted exception from wts run()");

					}

				}

			}catch (IOException ex){
				System.out.println("IOException from WTS run()");
			}
			
		}

	}

	public static void main(String[] args){
		PlayerFrame pf = new PlayerFrame(640,480);
		pf.connectToServer();
		pf.setUpGUI();
		

	}

}



