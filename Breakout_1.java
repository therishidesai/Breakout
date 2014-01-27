import acm.graphics.*;
import acm.program.*;
import acm.util.*;
//import acm.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Frame;
import javax.swing.JDialog;


public class Breakout_1 extends GraphicsProgram{
	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 1280;
	public static final int APPLICATION_HEIGHT = 800;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 50;

/** Number of rows of bricks */
	
	private static final int NBRICK_ROWS = 10;
	private static int nbricks=500;
	private static final int brickOffset = 4;
/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private static int NTURNS = 3;
	private static int turns = 0;
	private static int row= 0;
	private static double y = 50;
	 private static final int DIAM_BALL = 15; 
	 /** Amount Y velocity is increased each cycle as a
	 * result of gravity */ 
	 private static final double GRAVITY = 3; 
	 /** Animation delay or pause time between ball moves */ 
	 private static final int DELAY = 50; 
	 /** Initial X and Y location of ball */ 
	 private static final double X_START = DIAM_BALL / 2; 
	 private static final double Y_START = 200; 
	 /** X Velocity */ 
	 //private static final double X_VEL = 5; 
	 /** Amount Y Velocity is reduced when it bounces */ 
	 private static final double BOUNCE_REDUCE = 1;
	 private static final double BOUNCE_INCREASE= 1.5;
	 /** Starting X and Y Velocties */ 
	 private double xVel = 10; 
	 private double yVel = 10; 
	 private static int score = 0;
	 private static int life = 3;
	 private static final double paddleY = HEIGHT/2+200;
	 private static double ballTop;				//y coordinate of top of ball
		private static double ballBottom;			//y coordinate of top of ball
		private static double ballLeft;				//x coordinate of leftmost of ball
		private static double ballRight;
	 /* private instance variable */ 
	 private GOval ball;
	 private GRect paddle;
	 private GLabel lives;
	 private GLabel scorelabel;
	
public void run() {
		
		/* You fill this in, along with any subsidiary methods */
		setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
		
		//pack();
		//setResizable(false);
		//setResizable(false);
		for(row = 1; row<=NBRICK_ROWS; row++){
			bricker();
			y=y+BRICK_HEIGHT+brickOffset;
		
		}
		lives = new GLabel("Lives: " + life, 1000, 25);
		add(lives);
		paddle();
		addMouseListeners();
		setup();
		scorelabel = new GLabel("Score: "+ score, 1000, 40);
		add(scorelabel);
		GLabel wait = new GLabel("Click to start the game", 300, 300);
		add(wait);
		waitForClick();
		remove(wait);
		 // Simulation ends when ball goes off right hand 
		 // end of screen 
		  while (turns<NTURNS || nbricks==0) { 
			  moveBall(); 
			  checkForCollision(); 
			  pause(DELAY);
		 }
		  GLabel gameover = new GLabel("Game Over", 300, 300);
		  
		  add(gameover);
		  
}
private void setup() { 
	 ball = new GOval(rgen.nextDouble(10, getWidth()), Y_START, DIAM_BALL, DIAM_BALL); 
	 ball.setFilled(true); 
	 add(ball); 
	 } 
	 /** Update and move ball */

	 private void moveBall() { 
	 // increase yVelocity due to gravity on each cycle
		 //yVel = 5;
	     //xVel = 5;
		 /*if(score == 100){
				life++;
				remove(lives);
				lives = new GLabel("Lives: "+ life, 1000, 25);
				add(lives);
				NTURNS++;
			 }*/
		 
		 	ball.move(xVel,yVel); 
	 } 
	 /** Determine if collision with floor, update velocities 
	 * and location as appropriate. */ 
	 private void checkForCollision() { 
		 
		 GObject collider = getCollidingObject();
			if (collider == paddle) {
				/* We need to make sure that the ball only bounces off the top part of the paddle  
				 * and also that it doesn't "stick" to it if different sides of the ball hit the paddle quickly and get the ball "stuck" on the paddle.
				 * I ran "println ("vx: " + vx + ", vy: " + vy + ", ballX: " + ball.getX() + ", ballY: " +ball.getY());"
				 * and found that the ball.getY() changes by 4 every time, instead of 1,
				 * so it never reaches exactly the the height at which the ball hits the paddle (paddle height + ball height), 
				 * therefore, I estimate the point to be greater or equal to the height at which the ball hits the paddle, 
				 * but less than the height where the ball hits the paddle minus 4. 
				 */

				if(ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - DIAM_BALL && ball.getY() < getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - DIAM_BALL + 4) {
					yVel = -yVel;	
				}
			}
			//since we lay down a row of bricks, the last brick in the brick wall is assigned the value brick.
			//so we narrow it down by saying that the collier does not equal to a paddle or null, 
			//so all that is left is the brick
			else if (collider != null) {
				remove(collider);
				remove(scorelabel);
				score= score+10;
				scorelabel = new GLabel("Score: " + score, 1000, 40);
				add(scorelabel);
				nbricks--;
				yVel=-yVel;
			}
		 if(ball.getX() > getWidth()- DIAM_BALL || ball.getX() < 0+ DIAM_BALL){
			  xVel = -xVel;
			 
		 }else if(ball.getY()< 1 - DIAM_BALL){
			 yVel = -yVel * BOUNCE_REDUCE;
		 }else if (ball.getY() > getHeight() - DIAM_BALL) { 
			 turns++;
			 remove(lives);
			 remove(ball);
			 
			 life--;
			lives = new GLabel("Lives: "+ life, 1000, 25);
			add(lives);
			 setup();
		
	 // change ball's Y velocity to now bounce upwards 
		 /*if(yVel >250){
			 yVel=3;
		 }else{
			 yVel = -yVel * BOUNCE_REDUCE; 
	 // assume bounce will move ball an amount above the 
	 // floor equal to the amount it would have dropped 
	 // below the floor. 
	 		double diff = ball.getY() - (getHeight() - DIAM_BALL); 
	 		ball.move(0, -2 * diff);*/
		 
	 } 
	 }
	 
	 
	 private GObject getCollidingObject(){
		 if((getElementAt(ball.getX(), ball.getY())) != null) {
	         return getElementAt(ball.getX(), ball.getY());
	      }
		else if (getElementAt( (ball.getX() + DIAM_BALL), ball.getY()) != null ){
	         return getElementAt(ball.getX() + DIAM_BALL, ball.getY());
	      }
		else if(getElementAt(ball.getX(), (ball.getY() + DIAM_BALL)) != null ){
	         return getElementAt(ball.getX(), ball.getY() + DIAM_BALL);
	      }
		else if(getElementAt((ball.getX() + DIAM_BALL), (ball.getY() + DIAM_BALL)) != null ){
	         return getElementAt(ball.getX() + DIAM_BALL, ball.getY() + DIAM_BALL);
	      }
		//need to return null if there are no objects present
		else{
	         return null;
	      }
	 }
	 
private void bricker(){
	int x = 0;
	double z=0.0;
	for(x=0; x<=NBRICKS_PER_ROW; x++){
		
		
		GRect brick= new GRect(z, y, BRICK_WIDTH,BRICK_HEIGHT );
	
		if(row <= 2){
			brick.setColor(Color.BLACK);
			brick.setFillColor(Color.RED);
			brick.setFilled(true);;
			add(brick);
		}else if(row > 2 && row <= 4){
			brick.setColor(Color.BLACK);
			brick.setFillColor(Color.ORANGE);
			brick.setFilled(true);;
			add(brick);
		}else if(row>4 && row <=6){
			brick.setColor(Color.BLACK);
			brick.setFillColor(Color.YELLOW);
			brick.setFilled(true);;
			add(brick);
		}else if(row>6 && row <=8){
			brick.setColor(Color.BLACK);
			brick.setFillColor(Color.GREEN);
			brick.setFilled(true);;
			add(brick);
		}else if(row>8 && row <= 10){
			brick.setColor(Color.BLACK);
			brick.setFillColor(Color.CYAN);
			brick.setFilled(true);
			add(brick);
		}else{
			System.out.println("error");
		}
		
		z= z+BRICK_WIDTH+brickOffset;
		
	}
	
}
private void paddle(){
		
	paddle =new GRect(getWidth()/2, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT );
	paddle.setFillColor(Color.BLACK);
	paddle.setFilled(true);
	add(paddle);
}
public void mouseMoved(MouseEvent e) {
	/* The mouse tracks the middle point of the paddle. 
	 * If the middle point of the paddle is between half paddle width of the screen
	 * and half a paddle width before the end of the screen, 
	 * the x location of the paddle is set at where the mouse is minus half a paddle's width, 
	 * and the height remains the same
	 */
	if ((e.getX() < getWidth() - PADDLE_WIDTH/2) && (e.getX() > PADDLE_WIDTH/2)) {
		paddle.setLocation(e.getX() - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}

}
}


/*public void mousePressed(MouseEvent e) {
	lastX = e.getX();
	lastY = e.getY();
	gobj = (GRect) getElementAt(lastX, lastY);
	}
	*//** Called on mouse drag to reposition the object *//*
	public void mouseDragged(MouseEvent e) {
	if (gobj!= null && e.getX()<=getWidth() && e.getX()>1) {
		
	gobj.move(e.getX() - lastX, 0);
	lastX = e.getX();
	lastY= e.getY();
	
	
	}
}
	*//** Called on mouse click to move this object to the front *//*
	public void mouseClicked(MouseEvent e) {
	if (gobj != null) gobj.sendToFront();
	}
	 Instance variables 
	private GObject gobj;  The object being dragged 
	private double lastX;  The last mouse X position 
	private double lastY;  The last mouse Y position 
	}*/
