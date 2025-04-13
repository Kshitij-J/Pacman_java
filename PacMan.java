import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

// implementing so we have to use ActionListener and KeyListener
public class PacMan extends JPanel implements ActionListener, KeyListener
{

    class Block
    { // each block (basically a tile) in the game
        int x, y;
        int width, height;
        Image image;

        int startX, startY; // storing start positions

        char direction = 'U'; // U D L R 
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction)
        {
            char prevDirection = this.direction; // store the previous direction
            this.direction = direction; // update the direction of the block
            updateVelocity(); // update the velocity based on the direction
            this.x += this.velocityX; // update the x position based on the velocity
            this.y += this.velocityY; // update the y position based on the velocity
            for (Block wall : walls) // check for collision with walls
            {
                if (collision(this, wall)) {
                    this.x -= this.velocityX; // revert the position
                    this.y -= this.velocityY; // revert the position
                    this.direction = prevDirection; // revert the direction
                    updateVelocity(); // update the velocity based on the previous direction
                }
            }
            // for (Block ghost : ghosts) // check for collision with ghosts
            // {
            //     if (collision(this, ghost)) {
            //         this.x -= this.velocityX; // revert the position
            //         this.y -= this.velocityY; // revert the position
            //         this.direction = prevDirection; // revert the direction
            //         updateVelocity(); // update the velocity based on the previous direction
            //     }
            // }
        }

        void updateVelocity() // for movement
        {
            switch (this.direction) {
                case 'U':
                    this.velocityX = 0;
                    this.velocityY = -tileSize / 4;
                    break;
                case 'D':
                    this.velocityX = 0;
                    this.velocityY = tileSize / 4;
                    break;
                case 'L':
                    this.velocityX = -tileSize / 4;
                    this.velocityY = 0;
                    break;
                case 'R':
                    this.velocityX = tileSize / 4;
                    this.velocityY = 0;
                    break;
            }
        }

        void reset()
        {
            this.x = startX; // reset the x position to the start position
            this.y = startY; // reset the y position to the start position
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
    
    Timer gameLoop; // timer for the game loop
    char[] directions = {'U', 'D', 'L', 'R'}; // possible directions for the ghosts
    Random random = new Random(); // random number generator for ghost movement
    int score = 0;
    int lives = 3; // number of lives for pacman
    boolean gameOver = false; // game over flag

    PacMan()
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // same size as the frame
        setBackground(Color.BLACK); // background color
        addKeyListener(this); // add key listener to the panel
        setFocusable(true); // make the panel focusable 

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

        for(Block ghost : ghosts) // set random direction for each ghost
        {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection); // update the direction of the ghost
        }

        gameLoop = new Timer(50, this); // 20fps (1000/50)
        gameLoop.start(); // start the game loop
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

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18)); // set font for the score
        if (gameOver){
            g.drawString("Game Over " + String.valueOf(score), tileSize/2, tileSize/2); // draw game over message
        }
        else {
            g.drawString("x: " + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2); // draw the score
        }
    }

    // method to update the position of pacman and ghosts
    public void move()
    {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // check for collision with walls
        for (Block wall : walls) 
        {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX; // revert the position
                pacman.y -= pacman.velocityY; // revert the position
                break; // exit the loop if collision is detected
            }
        }

        for(Block ghost : ghosts) // move the ghosts
        {
            if(collision(ghost, pacman))
            {
                lives -= 1; // decrease the lives of pacman
                if(lives == 0) // if lives are 0, game over
                {
                    gameOver = true; // set game over flag
                    return;
                }
                resetPositions(); // reset the positions of pacman and ghosts

            }
            if(ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') // if the ghost is at the center of the map, set a random direction
            {
               ghost.updateDirection('U');
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            // check for collision with walls
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x < 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX; // revert the position
                    ghost.y -= ghost.velocityY; // revert the position
                    char newDirection = directions[random.nextInt(4)]; // set a new random direction
                    ghost.updateDirection(newDirection); // update the direction of the ghost
                }
            }
        }

        // check for collision with food
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food; // store the food that is eaten
                score += 10; // increase the score
                break; // exit the loop if collision is detected
            }
        }
        foods.remove(foodEaten); // remove the food from the set

        if (foods.isEmpty()) // if all food is eaten
        {
            loadMap();
            resetPositions(); // reset the positions of pacman and ghosts
        }
    }

    public boolean collision(Block a, Block b)
    {   
        return a.x < b.x + b.width && 
               a.x + a.width > b.x && 
               a.y < b.y + b.height && 
               a.y + a.height > b.y; // check for collision between two blocks
    }

    public void resetPositions()
    {
        pacman.reset(); // reset the position of pacman
        pacman.velocityX = 0; // reset the velocity of pacman
        pacman.velocityY = 0; // reset the velocity of pacman

        for(Block ghost : ghosts) // reset the position of ghosts
        {
            ghost.reset(); // reset the position of the ghost
            char newDirection = directions[random.nextInt(4)]; // set a new random direction
            ghost.updateDirection(newDirection); // update the direction of the ghost
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // move pacman
        repaint(); // repaint the panel to update the graphics
        if(gameOver) // if game is over, stop the game loop
        {
            gameLoop.stop(); // stop the game loop
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    // key pressed event for pacman movement
    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver)
        {
            loadMap(); // reload the map
            resetPositions(); // reset the positions of pacman and ghosts
            lives = 3;
            score = 0;
            gameOver = false; // reset the game over flag
            gameLoop.start(); // start the game loop again
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                pacman.updateDirection('U');
                pacman.image = pacmanUpImage;
                break;
            case KeyEvent.VK_DOWN:
                pacman.updateDirection('D');
                pacman.image = pacmanDownImage;
                break;
            case KeyEvent.VK_LEFT:
                pacman.updateDirection('L');
                pacman.image = pacmanLeftImage;
                break;
            case KeyEvent.VK_RIGHT:
                pacman.updateDirection('R');
                pacman.image = pacmanRightImage;
                break;
        }
    }

}
