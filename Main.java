import javax.swing.JFrame;
import game.PacMan; 
public class Main {
    public static void main(String[] args) {

        int rowcount = 21;
        int columncount = 19;
        int tilesize = 32;
        int boardWidth = columncount * tilesize;
        int boardHeight = rowcount * tilesize;


        JFrame frame = new JFrame("PacMan(real)"); // making frame
        frame.setVisible(true); // visible by default
        frame.setSize(boardWidth, boardHeight); // set size of the frame
        frame.setLocationRelativeTo(null); //center of screen
        frame.setResizable(false); // not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close on exit

        PacMan pacmanGame = new PacMan(); // create an instance of the PacMan class
        frame.add(pacmanGame); // add the PacMan instance to the frame
        frame.pack(); // pack the frame to fit the components
        pacmanGame.requestFocus(); // request focus for the PacMan instance to receive key events
        frame.setVisible(true); // make the frame visible again after packing
    }
}