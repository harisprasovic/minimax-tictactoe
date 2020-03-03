import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GUI extends JFrame{
	private Container pane;
	private JButton [][] board;
	private boolean hasWinner;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem quit;
	private JMenuItem newGame;
	
	public GUI() {
		super();
		pane = getContentPane();
		pane.setLayout(new GridLayout(3,3));
		setSize(500,500);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		board=new JButton[3][3];
		
		initializeBoard();
		initializeMenuBar();
		
		hasWinner=false;
		
		bestMove();
		
	}
	

	private void initializeMenuBar() {
		menuBar= new JMenuBar();
		menu = new JMenu("File");
		newGame = new JMenuItem("New Game");
		newGame.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				resetBoard();				
			}
			
		});
		
		
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		menu.add(newGame);
		menu.add(quit);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
	}
	
	private void resetBoard() {
		hasWinner=false;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				board[i][j].setText("");			
				}
		}
		bestMove();
		
		
	}
	
	private void initializeBoard(){
		for(int i=0;i<3;i++) {
	    	for(int j=0;j<3;j++) {
	    		JButton btn= new JButton();
				btn.setFont(new Font(Font.SANS_SERIF,Font.BOLD,100));
				
				board[i][j]= btn;
				int temp1=i;
				int temp2=j;
				
				btn.addActionListener(e -> btnClick(e, temp1, temp2));
				pane.add(btn);
	    	}
	    }
	}
	
	public void result() {
		String temp=checkWinner();
		if(temp!=null) {
			
			if(temp.equals("TIE")) {
				JOptionPane.showMessageDialog(null, "It was a Tie!");
				System.exit(0);
			
			}else if(temp.equals("X")){
				JOptionPane.showMessageDialog(null, "The computer has won");
				System.exit(0);
		
			}
			else {
				JOptionPane.showMessageDialog(null, "The player wins");
				System.exit(0);
				
			}
		}
	}
	
	
	//have to add a checking that the player has played something and not clicked on an occupied space
	private void btnClick(ActionEvent e, int a, int b) {
		if(!(board[a][b].getText()).equals("") ) {
			return;
		}
		else if(hasWinner==false) {
			board[a][b].setText("O");
			result();
			bestMove();
		}
	}
	
	public void bestMove() {
		double bestScore=Double.NEGATIVE_INFINITY;
		int movei=-1;
		int movej=-1;
		result();
		
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(board[i][j].getText().equals("")) {
					board[i][j].setText("X");
					double score=minimax(board, 0, false);
					board[i][j].setText("");
					if(score>bestScore) {
						bestScore=score;
						movei=i;
						movej=j;
					}
				}
			}	
		}
		board[movei][movej].setText("X");
		result();
		
		//users turn
	}
	
	public static int scoring(String a) {
		int ret=0;
		if(a.equals("X")) {
			ret=1;
		}
		else if(a.equals("O")) {
			ret=-1;
		}
		else if(a.equals("TIE")) {
			ret=0;
		}
		return ret;
		
	}
	
	public double minimax(JButton[][] newbo, int depth, boolean isMaximizing) {
		String result= checkWinner();
		if(result!=null) {
			return scoring(result);
		}
		
		if(isMaximizing==true) {
			double bestScore=Double.NEGATIVE_INFINITY;
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++) {
					if(board[i][j].getText().equals("")) {
						board[i][j].setText("X");;
						//maybe take board out
						double score = minimax(board, depth+1, false);
						board[i][j].setText("");;
						if(score>bestScore) {
							bestScore=score;
						}
					}
				}
			}
			return bestScore;
		}
		else {
			double bestScore=Double.POSITIVE_INFINITY;
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++) {
					if(board[i][j].getText().equals("")) {
						board[i][j].setText("O");
						//maybe take board out
						double score = minimax(board, depth+1, true);
						board[i][j].setText("");;
						if(score<bestScore) {
							bestScore=score;
						}
					}
				}
			}
			return bestScore;
		}
		
	}
	
	public String checkWinner() {
		String winner=null;
		
		//horizontal
		for(int i=0;i<3;i++) {
			if(!(board[i][0].getText()).equals("") && board[i][0].getText().contentEquals(board[i][1].getText()) && board[i][1].getText().contentEquals(board[i][2].getText())) {
				
				winner=board[i][0].getText();
			}
		}
		
		//vertical
		for(int i=0;i<3;i++) {
			if(!(board[0][i].getText()).equals("") && board[0][i].getText().contentEquals(board[1][i].getText()) && board[1][i].getText().contentEquals(board[2][i].getText())) {		
				winner=board[0][i].getText();
			}
		}
		
		if(!(board[0][0].getText()).equals("") && board[0][0].getText().contentEquals(board[1][1].getText()) && board[1][1].getText().contentEquals(board[2][2].getText())) {
			
			winner=board[0][0].getText();
		}
		if(!(board[2][0].getText()).equals("") && board[2][0].getText().contentEquals(board[1][1].getText()) && board[1][1].getText().contentEquals(board[0][2].getText())) {
			winner=board[2][0].getText();
		}
		
		int openspo=0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(board[i][j].getText().equals("")) {
					openspo++;
				}
			}
		}
		
		if(winner==null && openspo==0) {
			return "TIE";
		}
		else {
			return winner;
		}
		
		
	}

	
	
	

}
