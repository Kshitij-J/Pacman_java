import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;


public class PacMan extends JPanel
{
    class Block
    { // each block (basically a tile) in the game
        int x, y;
        int width, height;
        Image image;

        int startX, startY; // storing start positions

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
    }

    private int rowcount = 21;
    private int columncount = 19;
    private int tileSize = 32;
    private int boardWidth = columncount * tileSize;
    private int boardHeight = rowcount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // making Map
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };
    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    Block pacman;

    PacMan()
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // same size as the frame
        setBackground(Color.BLACK); // background color
        // load images
        wallImage = new ImageIcon(getClass().getResource("resources/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("resources/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("resources/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("resources/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("resources/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("resources/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("resources/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("resources/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("resources/pacmanRight.png")).getImage();

        loadMap(); // load the map


    }

    public void loadMap()
    {
        //initialziton of  the hashsets
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        //loop through the map and create blocks for each tile
        for(int r = 0; r < rowcount; r++)
        {
            for(int c = 0; c < columncount; c++)
            {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                // left corner of the tile
                int x = c * tileSize; // x position of the tile
                int y = r * tileSize; // y position of the tile

                if(tileMapChar == 'X')
                {
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall); // add wall to the set
                }
                else if (tileMapChar == 'b')
                {
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost); // ablue ghost
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { //food, 14 because center of the tile
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null); // draw the pacman

        for(Block ghost : ghosts) // draw the ghosts
        {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for(Block wall : walls) // draw the walls
        {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        for(Block food : foods) // draw the food
        {
            g.setColor(Color.WHITE); // color of the food
            g.fillOval(food.x, food.y, food.width, food.height); // draw the food
        }
    }
}
