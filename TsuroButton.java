import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Dimension2D;

/**
 * A JavaFX Button that has a Tsuro game tile painted on it.
 * The game tile contains 4 paths connecting 4 pairs of endpoints on the icon.
 * Each endpoint is represented by a number
 * <pre>
 *         0   1
 *       +-------+
 *     6 |       | 2
 *       |       |
 *     7 |       | 3
 *       +-------+
 *         4   5
 * </pre>
 *
 * The four paths on the tile are represented by an array of int where
 * <tt>array[0] = 5</tt> means we have a path connecting endpoint 0 and endpoint 5.
 * 
 * @author Harold Connamacher
 */
public class TsuroButton extends Button {
  /** An array that stores the endpoint to endpoint connections of the 4 paths on the icon */
  private int[] paths = null;
  
  /** Stores whether there is a piece on one of the icon endpoints */
  private Color[] pieces = new Color[8];
  
  /** The background color for the icon */
  private Color background = Color.WHITE;
  
  /** The color used to draw the paths */
  private Color foreground = Color.BLACK;
  
  /** The width used to draw the paths on the icon */
  private double pathWidth = 3.0;
  
  /**
   * Create a Tsuro game button
   * @param width the desired with of the button
   * @param height the desired height of the button
   */
  public TsuroButton(int width, int height) {
    super();
    setMinWidth(width);
    setMinHeight(height);
    setGraphic(new GameTile(width, height));
  }
  
  /**
   * Redisplays the tile image
   */
  private void repaint() {
    ((GameTile)getGraphic()).paintTile();
  }
  
  /**
   * Change the connections for the paths of the game tile.  The paths are drawn on the button icon.  A null input produces a blank tile.
   * @param connections  an array of 8 ints.  connections[i] = j means there is a path from endpoint i to endpoint j.
   */
  public void setConnections(int[] connections) {
    this.paths = connections;
    repaint();
  }
  
  /**
   * Return an array showing the connections for the game tile that are drawn on the icon
   * @return  the array giving the endpoint connections for the paths that are drawn on the icon
   */
  public int[] getConnections() {
    return paths;
  }
  
  /**
   * Remove a stone from a specific endpoint of a path on the game tile.
   * @param endPoint  the path endpoint that we are removing a game piece from
   */
  public void removeStone(int endPoint) {
    pieces[endPoint] = null;
    repaint();
  }
  
  /**
   * Add a game stone to a specific path endpoint on the game tile.
   * @param stoneColor  the color of the stone to be added
   * @param endPoint  the path endpoint for the piece
   */
  public void addStone(Color stoneColor, int endPoint) {
    pieces[endPoint] = stoneColor;
    repaint();
  }
  
  /**
   * Retrieve the color used for the background of the game tile button.
   * @return the background color of the button
   */
  public Color getBackgroundColor() {
    return background;
  }
  
  /**
   * Change the color used for the background of the game tile button.
   * @param background  the background color of the button
   */
  public void setBackgroundColor(Color background) {
    this.background = background;
    repaint();
  }
  
  /**
   * Retrieve the color used to draw the paths on the icon.
   * @return the color used to draw the paths
   */
  public Color getForegroundColor() {
    return foreground;
  }
  
  /**
   * The color used to draw the paths on the game tile.
   * @param foreground  the color used to draw the paths
   */
  public void setForegroundColor(Color foreground) {
    this.foreground = foreground;
    repaint();
  }
  
  /**
   * Return the pen width used to draw the paths on the game tile.
   * @return the pen width
   */
  public double getPathWidth() {
    return pathWidth;
  }
  
  /**
   * Change the pen width used to draw the paths on the game tile.
   * @param pathWidth  the width used to draw the paths
   */
  public void setPathWidth(double pathWidth) {
    this.pathWidth = pathWidth;
    repaint();
  }
  
  /** Add a pair to a random symmetric permutation.
    * @param connections  the array stores what each endpoint connects to
    * @param filled       the array indicates whether a an endpoint has a match
    * @param numLeft      the number of not-filled entries of the connections array
    */
  private static void makeRandomConnection(int[] connections, boolean[] filled, int numLeft) {
    int next = 0;
    while (filled[next])                                 // get the next spot not yet connected
      next++;
    
    int skip = (int)(Math.random() * (numLeft - 1));     // randomly choose from the remaining spots
    int connectTo = next + 1;
    while (skip > 0 || filled[connectTo]) {              // go to that randomly chosen spot
      if (!filled[connectTo])
        skip--;
      connectTo++;
    }
    connections[next] = connectTo;                       // make the connection
    connections[connectTo] = next;
    filled[next] = true;
    filled[connectTo] = true;
  }
  
  /**
   * Create a random symmetric permutation.  The permutation represents the connections between
   * two endpoints.  If index i stores value j, then the icon will have a path from endpoint i to
   * endpoint j.
   * @return a new array storing the random path connections
   */
  public static int[] makeRandomConnectionArray() {
    int[] connections = new int[8];
    boolean[] filled = new boolean[8];
    makeRandomConnection(connections, filled, 8);
    makeRandomConnection(connections, filled, 6);
    makeRandomConnection(connections, filled, 4);
    makeRandomConnection(connections, filled, 2);
    return connections;
  }
  
  /** The image on a tile of the Tsuro game. Plus any stones that are on the tile. */
  private class GameTile extends Canvas {
    
    /**
     * Create a tile of the game
     * @param width the width of the game tile in pixels
     * @param height the height of the game time in pixels
     */
    public GameTile(int width, int height) {
      super(width, height);
      paintTile();
    }
    
    /**
     * Returns the end point location (in pixels) of a particular end point of a path on the tile
     * @param value a value between 0-7 to indicate one of the 8 path endpoints on this tile
     */
    private Dimension2D endPoint2Dimension(int value) {
      double x_value = 0, y_value = 0;
      switch (value) {
        case 0:
          x_value = getWidth() / 3;
          y_value = 0;
          break;
        case 1:
          x_value = 2 * getWidth() / 3;
          y_value = 0;
          break;
        case 2:
          x_value = getWidth() - 1;
          y_value = getHeight() / 3;
          break;
        case 3:
          x_value = getWidth() - 1;
          y_value = 2 * getHeight() / 3;
          break;
        case 5:
          x_value = 2 * getWidth() / 3;
          y_value = getHeight() - 1;
          break;
        case 4:
          x_value = getWidth() / 3;
          y_value = getHeight() - 1;
          break;
        case 7:
          x_value = 0;
          y_value = 2 * getHeight() / 3;
          break;
        default:
          x_value = 0;
          y_value = getHeight() / 3;
      }
      return new Dimension2D(x_value, y_value);
    }
    
    /**
     * Draw a path on the icon between two getConnections() locations.
     * @param g  the graphics context used for the drawing
     * @param start  one of the getConnections()s of the path
     * @param end    the other getConnections() of the path
     */
    private void drawPath(GraphicsContext gc, int start, int end) {
      Dimension2D start_dim = endPoint2Dimension(start);
      Dimension2D end_dim = endPoint2Dimension(end);
 
      if (start_dim.getWidth() == 0 && end_dim.getWidth() == 0)
        gc.strokeArc(-getWidth()/4, getHeight() / 3, getWidth() / 2, 2*getHeight()/3 - getHeight() / 3, 270, 180, javafx.scene.shape.ArcType.OPEN);
      else if (start_dim.getWidth() == getWidth() - 1 && end_dim.getWidth() == getWidth() - 1)
        gc.strokeArc(getWidth() - 1 - getWidth() / 4, getHeight() / 3, getWidth() / 2, 2*getHeight()/3 - getHeight() / 3, 90, 180, javafx.scene.shape.ArcType.OPEN);
      else if (start_dim.getHeight() == 0 && end_dim.getHeight() == 0)
        gc.strokeArc(getWidth() / 3, - getHeight() / 4, 2*getWidth() / 3 - getWidth() / 3, getHeight() / 2, 180, 180, javafx.scene.shape.ArcType.OPEN);
      else if (start_dim.getHeight() == getHeight() - 1 && end_dim.getHeight() == getHeight() - 1)
        gc.strokeArc(getWidth() / 3, getHeight() - 1 - getHeight() / 4, 2 * getWidth() / 3 - getWidth() / 3, getHeight() / 2, 0, 180, javafx.scene.shape.ArcType.OPEN);
      else
        gc.strokeLine(start_dim.getWidth(), start_dim.getHeight(), end_dim.getWidth(), end_dim.getHeight());
    }
    
    /**
     * Draw the tile image on the button
     */
    public void paintTile () {
      GraphicsContext gc = getGraphicsContext2D();
      
      gc.setFill(getBackgroundColor());
      gc.fillRect(0,0,getHeight(),getWidth());
      
      if (getConnections() != null) {
        gc.setStroke(getForegroundColor());
        gc.setLineWidth(getPathWidth());
        for (int i = 0; i < getConnections().length; i++) {
          drawPath(gc, i, getConnections()[i]);
        }
        
        for (int i = 0; i < pieces.length; i++) {
          if (pieces[i] != null) {
            gc.setFill(pieces[i]);
            Dimension2D piece_dim = endPoint2Dimension(i);
            double width, height, x, y;
            if (piece_dim.getWidth() == 0) {
              x = 0;
              y = piece_dim.getHeight() - getHeight() / 8;
              width = getWidth() / 8;
              height = getHeight() / 4;
            }
            else if (piece_dim.getWidth() == getWidth() - 1) {
              x = getWidth() - 1 - getWidth() / 8;
              y = piece_dim.getHeight() - getHeight() / 8;
              width = getWidth() / 8;
              height = getHeight() / 4;
            }
            else if (piece_dim.getHeight() == 0) {
              x = piece_dim.getWidth() - getWidth() / 8;
              y = 0;
              width = getWidth() / 4;
              height = getHeight() / 8;
            }
            else {
              x = piece_dim.getWidth() - getWidth() / 8;
              y = getHeight() - 1 - getHeight() / 8;
              width = getWidth() / 4;
              height = getHeight() / 8;
            }
            gc.fillOval(x, y, width, height);
          }
        }
      }
    }
  }
  
}