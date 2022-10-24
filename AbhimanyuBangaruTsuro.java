import javafx.application.Application;
import javafx.application.Platform; 
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane; 
import javafx.scene.paint.Color;
import java.util.NoSuchElementException;

public class AbhimanyuBangaruTsuro extends Application{
  
  
  //the number of columns 
  private static int numColumns; 
  
  //the number of rows 
  private static int numRows; 
  
  //the number of tiles each hand has
  private static int handsize; 
  
  //2d array storing the buttons for the grid pane 
  private TsuroButton[][] tsuroButtonsGP = new TsuroButton[numColumns][numRows]; 
  
  //1d array for storing the player 1 hands
  private TsuroButton[] player1Hand = new TsuroButton[handsize]; 
  
  //1d array for storing the player 2 hands
  private TsuroButton[] player2Hand = new TsuroButton[handsize]; 
  
  //Tracker of current player 
  private int currentPlayer = 1; 
  
  //array containing the highlighted button for each player 
  private TsuroButton[] highlightedButton = new TsuroButton[2]; 
  
  
  //array containing whether or not it is player's first move 
  private boolean[] firstMove = {true, true}; 
  
  //Array containing location on tile for stones
  private int[] locationOfStones = {6,2}; 
  
  //Array containing location on tile for stones
  private int[] prevLocationOfStones = {6,2};  
  
  //array of button with stones 
  private TsuroButton[] buttonWithStone = new TsuroButton[2]; 
  
  //the boolean flag to see if the game is done or not 
  private boolean continueGame = true; 
  
  //boolean array to see if any of the players have lost
  private boolean[] playerLost = {false, false}; 
  
  //Stage for Player one hand 
  private Stage player1Stage = new Stage(); 
  
  //Stage for Player two hand 
  private Stage player2Stage = new Stage(); 
  
  //scene to store the scene for the main board
  Scene scene;
  
  //scene to store the scene for player 1's hand
  Scene scenePlayer1; 
  
  //scene to store the scene for player 2's hand 
  Scene scenePlayer2; 
  
  /**
   * Set up the GUI
   * @param primaryStage the main window of the GUI
   */
  public void start(Stage primaryStage) {
    
    //the main layout for the GUI
    GridPane gridPaneBoard = new GridPane();
    
    //the gridPane for player 1
    GridPane gridPanePlayer1 = new GridPane();
    
    //the gridPane for player 2
    GridPane gridPanePlayer2 = new GridPane();
    
    //Button event handler 
    EventHandler<ActionEvent> playerButtonHandler = new PlayerButtonHandler();
    
    //set up the 2d array 
    setUpTsuroButtonsGP(tsuroButtonsGP, numColumns, numRows); 
    
    //setting up gridpane for board
    for(int i = 0; i < numColumns; i++){
      for(int j = 0; j < numRows; j++){
        gridPaneBoard.add(tsuroButtonsGP[i][j], i, j);
      }
    }
    
    
    /* set up the 1d array for player 1 */
    setUpPlayerHand(player1Hand, handsize, 1); 
    
    /* set up the 1d array for player 2 */
    setUpPlayerHand(player2Hand, handsize, 2); 
    
    /* setting up the gridpane for player 1 hand */
    for(int i = 0; i < handsize; i++){
      gridPanePlayer1.add(player1Hand[i],i,0); 
    }
    
    /* setting up the gridpane for player 2 hand */
    for(int i = 0; i < handsize; i++){
      gridPanePlayer2.add(player2Hand[i],i,0); 
    }
    
    //scene to store the scene for the main board
    scene = new Scene(gridPaneBoard);
    
    //scene to store the scene for player 1's hand
    scenePlayer1 = new Scene(gridPanePlayer1); 
    
    //scene to store the scene for player 2's hand 
    scenePlayer2 = new Scene(gridPanePlayer2);  
    
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setTitle("Fancy Tsuro"); 
    player1Stage.setScene(scenePlayer1);
    player1Stage.show();
    player1Stage.setTitle("Player 1"); 
    player2Stage.setScene(scenePlayer2);
    player2Stage.show();
    player2Stage.setTitle("Player 2"); 
    
    
  }
  
  /**
   * Launch the GUI
   * @param args the command line arguments are ignored
   */
  public static void main(String[] args) {
    numRows = 6;
    numColumns = 6; 
    handsize = 3; 
    if(args.length >= 2){
      try{
        numRows = Integer.parseInt(args[0]);
        numColumns = Integer.parseInt(args[1]); 
      } catch(Exception ex){
        numRows = 6; 
        numColumns = 6; 
      }
    } 
    if(args.length == 3){
      try{
        handsize = Integer.parseInt(args[2]);  
      } catch(Exception ex){
        handsize = 3; 
      }
    }
    
    
    Application.launch(args);
  }
  
  /**
   * This method initializes all the indexes in the array storing all the buttons for the Tsuro board
   * @param array is the array that hold all the main board's buttons 
   * @param col is the number of columns in the array
   * @param row is the number of rows in the array 
   */ 
  public void setUpTsuroButtonsGP(TsuroButton[][] array, int col, int row){
    EventHandler<ActionEvent> boardButtonHandler = new BoardButtonHandler();
    for(int i = 0; i < col; i++){
      for(int j = 0; j < row; j++){
        array[i][j] = new TsuroButton(50,50);
        array[i][j].setOnAction(boardButtonHandler);
      }
    }
  }
  
  //sets up the player hands 
  /**
   * This method initializes all the indexes in the array storing all the buttons for a player's hand
   * @param array is the array that hold all the certain player's tiles
   * @param handSize is the size of the array 
   */ 
  public void setUpPlayerHand(TsuroButton[] array, int handSize, int player){
    EventHandler<ActionEvent> playerButtonHandler = new PlayerButtonHandler();
    for(int i = 0; i < handSize; i++){
      array[i] = new TsuroButton(50,50);
      array[i].setOnAction(playerButtonHandler);
      array[i].setConnections(TsuroButton.makeRandomConnectionArray());
      array[i].addStone(returnCorrectColor(player), locationOfStones[player - 1]);
    }
  }
  
  /*
   * This helper method decides whether or not the button should be highlighted
   * if the clicked button is already highlighted, it rotates the title
   * @param array the array of the current player
   * @param the button in the play hand clicked 
   */
  public void handlePlayerButton(TsuroButton[] array, TsuroButton button){
    if(button.getBackgroundColor() == Color.WHITE){
      highlightedButton[currentPlayer - 1] = button; 
      button.setBackgroundColor(Color.YELLOW); 
      for(TsuroButton but : array){
        if(but != button){
          but.setBackgroundColor(Color.WHITE);
        }
      }
    } else if(button.getBackgroundColor() == Color.YELLOW){
      int[] currentPaths = button.getConnections(); 
      int[] map = {2,3,5,4,6,7,1,0}; 
      int[] rotated = new int[8]; 
      for(int i = 0; i < 8; i++){
        rotated[map[i]] = map[currentPaths[i]];
      }
      button.setConnections(rotated);
    }
    
  }
  
  /** A event handler that takes care of button pressing for a player button */
  private class PlayerButtonHandler implements EventHandler<ActionEvent> {
    
    /** Override the handle method to indicate what to do if the event occurs
      * @param event the event data
      */
    public void handle(ActionEvent event) {
      if(continueGame()){
        TsuroButton b = (TsuroButton)event.getSource();
        for(TsuroButton but : player1Hand){
          if(but == b){
            if(currentPlayer == 1){
              handlePlayerButton(player1Hand, but); 
              
            }
          }
        }
        for(TsuroButton but : player2Hand){
          if(but == b){
            if(currentPlayer == 2){
              handlePlayerButton(player2Hand, but); 
            }
          }
        }
      }
    }
  }
  
  /**
   * A method that removes the stones from all the player hands
   */
  public void removeStonesFromAllPlayerHands(){
    for(TsuroButton but : player1Hand){
      but.removeStone(locationOfStones[0]); 
    }
    for(TsuroButton but : player2Hand){
      but.removeStone(locationOfStones[1]); 
    }
  }
  
  /**
   * A method that add a stone into all the player hands in the correct location
   */
  public void addStonesToAllPlayerHands(){
    for(TsuroButton but : player1Hand){
      but.addStone(Color.GREEN,locationOfStones[0]); 
    }
    for(TsuroButton but : player2Hand){
      but.addStone(Color.BLUE,locationOfStones[1]); 
    }
  }
  
  /**
   * This helper method allows for functionality if this isn't the first turn. Checks to make sure that the desired button 
   * is in the right location according to where the player's stone currently is. 
   * @param buttonPressed the TsuroButton trying to replace with the highlighted piece
   * @param locationOfStone is the connection to stone is on 
   * @param buttonWithThisPlayerStone is the button with one of the player's stone
   */
  public boolean canPlaceBoardButton(TsuroButton buttonPressed,int locationOfStone,TsuroButton buttonWithThisPlayerStone){
    //the x location of the button clicked 
    int locationXClicked = -1; 
    
    //the y location of the button clicked
    int locationYClicked = -1;
    
    //the x location of the button with the stone 
    int locationXAlready = -1;
    
    //the y location of the button with the stone 
    int locationYAlready = -1; 
    
    for(int i = 0; i < numColumns; i++){
      for(int j = 0; j < numRows; j++){
        if(tsuroButtonsGP[i][j] == buttonPressed){
          locationXClicked = i; 
          locationYClicked = j; 
        }
      }
    }
    
    for(int i = 0; i < numColumns; i++){
      for(int j = 0; j < numRows; j++){
        if(tsuroButtonsGP[i][j] == buttonWithThisPlayerStone){
          locationXAlready = i; 
          locationYAlready = j; 
        }
      }
    }
    if(locationOfStone == 0 || locationOfStone == 1){
      if((locationYClicked == locationYAlready - 1) && (locationXClicked == locationXAlready)){
        return true; 
      }  
    }
    else if(locationOfStone == 2 || locationOfStone == 3){
      if((locationXClicked == locationXAlready + 1) && (locationYClicked == locationYAlready)){
        return true; 
      } 
    }
    else if(locationOfStone == 4 || locationOfStone == 5){
      if((locationYClicked == locationYAlready + 1) && (locationXClicked == locationXAlready)){
        return true; 
      } 
    }
    else if(locationOfStone == 6 || locationOfStone == 7){
      if((locationXClicked == locationXAlready - 1) && (locationYClicked == locationYAlready)){
        return true; 
      }
    }
    
    return false; 
    
  }
  /* 
   * A helper method which returns the right color based on the player 
   * @param player is the player wanted
   * @return the color of the stone 
   */ 
  public Color returnCorrectColor(int player){
    if(player == 1){
      return Color.GREEN; 
    }else if(player == 2){
      return Color.BLUE; 
    } else{
      return Color.BLACK; 
    }
  }
  
  /**
   * A helper method that consolidates all the steps necessary to take if this is the player's first move after identifying
   * the validity of the button clicked
   * @param player the number of the current player 
   * @param buttonClicked is the button clicked
   */
  public void handleFirstMoveSetUp(int player, TsuroButton buttonClicked){
       
    //location of stone
    int locationOfStone = -100; 
    
    //TsuroButton to store the address of the highlighted player 
    TsuroButton highlightedPlayerButton; 
    
    //to store the input color into setStone 
    Color color = Color.BLACK;
    
    if(player == 1){
      locationOfStone = locationOfStones[0]; 
      highlightedPlayerButton = highlightedButton[0];
      color = Color.GREEN; 
    } else{
      locationOfStone = locationOfStones[1];
      highlightedPlayerButton = highlightedButton[1];
      color = Color.BLUE; 
    }
    //replace the highlighted piece 
    highlightedPlayerButton.setConnections(TsuroButton.makeRandomConnectionArray());
    //set the stone 
    buttonClicked.addStone(color, buttonClicked.getConnections()[locationOfStone]); 
    removeStonesFromAllPlayerHands();
    if(player == 1){
      locationOfStones[0] = buttonClicked.getConnections()[6];
    } else {
      locationOfStones[1] = buttonClicked.getConnections()[2];
    }
    updateLocationOfStone(player); //player number
    addStonesToAllPlayerHands(); 
  }
  
  /*
   * A helper method that resets the highlighted button after being placed on the board
   * @param highlightedButton the button to reset
   */
  public void resetHighlightedButtons(TsuroButton highlightedButton){
    highlightedButton.setConnections(TsuroButton.makeRandomConnectionArray());
    highlightedButton.setBackgroundColor(Color.WHITE); 
    highlightedButton = new TsuroButton(50,50); 
  }
  
  
  /** An event handler that handles the press of any button on the board **/ 
  private class BoardButtonHandler implements EventHandler<ActionEvent> {
    public void handle(ActionEvent event){
      //the button clicked
      TsuroButton button = (TsuroButton)event.getSource(); 
      
      //the column the row can be placed in
      int columnCanBePlace = -100; 
      
      if(currentPlayer == 1){
        columnCanBePlace = 0; 
      } else {
        columnCanBePlace = numColumns - 1; 
      }
      
      if(continueGame()){
        if(highlightedButton[currentPlayer - 1] != null){
          if(firstMove[currentPlayer - 1] == true){
            //for the first move, make sure that the button trying to be inputted is in the correct column based on the player number
            for(int i = 0; i < numRows; i++){
              if(tsuroButtonsGP[columnCanBePlace][i] == button){
                button.setConnections(highlightedButton[currentPlayer-1].getConnections()); 
                handleFirstMoveSetUp(currentPlayer,button); 
                firstMove[currentPlayer - 1] = false; 
                buttonWithStone[currentPlayer - 1]  = button; 
                resetHighlightedButtons(highlightedButton[currentPlayer - 1]);
                if(currentPlayer == 1){
                  currentPlayer = 2;
                } else{
                  currentPlayer = 1;
                }
              } 
            }
          } else{
            //only do the following if the board button pressed is a valid button 
            if(canPlaceBoardButton(button, prevLocationOfStones[currentPlayer - 1], buttonWithStone[currentPlayer - 1])){
              buttonWithStone[currentPlayer - 1].removeStone(prevLocationOfStones[currentPlayer - 1]);
              button.setConnections(highlightedButton[currentPlayer-1].getConnections());
              button.addStone(returnCorrectColor(currentPlayer), button.getConnections()[locationOfStones[currentPlayer - 1]]); 
              removeStonesFromAllPlayerHands();
              locationOfStones[currentPlayer - 1] = button.getConnections()[locationOfStones[currentPlayer - 1]];
              updateLocationOfStone(currentPlayer);
              addStonesToAllPlayerHands(); 
              buttonWithStone[currentPlayer - 1] = button; 
              resetHighlightedButtons(highlightedButton[currentPlayer - 1]); 
              nowMoveTheStonesUntilTheyCant();
              if(currentPlayer == 1){
                currentPlayer = 2;
              } else{
                currentPlayer = 1;
              }
            }
          }
        } 
      } 
      if(!continueGame()){
          if(playerLost[0] == true && playerLost[1] == true){
            System.out.println("DRAW"); 
          } else if(playerLost[0] == true){
            System.out.println("THE WINNER IS PLAYER 2"); 
          }
          else if(playerLost[1] == true){
            System.out.println("THE WINNER IS PLAYER 1"); 
          }
          
        }
    }
  }
  
  /*
   * This method essentially keeps looping and changing the position of the stone on the board until the stone can no longer move
   * Then the players hand are updated if the stone moves
   */
  public void nowMoveTheStonesUntilTheyCant(){
    
    //array holding x coordinates of stones
    int[] stoneXs = {-1,-1}; 
    
    //array holding y coordinates of stones
    int[] stoneYs = {-1,-1}; 
    
    for(int i = 0; i < numColumns; i++){
      for(int j = 0; j < numRows; j++){
        if(tsuroButtonsGP[i][j] == buttonWithStone[0]){
          stoneXs[0] = i; 
          stoneYs[0] = j; 
        }
      }
    }
    
    for(int i = 0; i < numColumns; i++){
      for(int j = 0; j < numRows; j++){
        if(tsuroButtonsGP[i][j] == buttonWithStone[1]){
          stoneXs[1] = i; 
          stoneYs[1] = j; 
        }
      }
    }
    
    
    while(checkToKeepGoingPlayer(stoneXs[0],stoneYs[0],stoneXs[1], stoneYs[1], 1) && continueGame){
      //if both conditions are true, then based on the location of the stone move the stone to the new tile
      if(prevLocationOfStones[0] == 0 || prevLocationOfStones[0] == 1){
        moveTheStone(stoneXs[0], stoneYs[0]-1, Color.GREEN, buttonWithStone[0], 1);
        stoneYs[0] = stoneYs[0] - 1;
      }
      else if(prevLocationOfStones[0] == 2 || prevLocationOfStones[0] == 3){
        moveTheStone(stoneXs[0]+1, stoneYs[0], Color.GREEN, buttonWithStone[0], 1);
        stoneXs[0] = stoneXs[0] + 1;
      }
      else if(prevLocationOfStones[0] == 4 || prevLocationOfStones[0] == 5){
        moveTheStone(stoneXs[0], stoneYs[0] + 1, Color.GREEN, buttonWithStone[0], 1);
        stoneYs[0] = stoneYs[0] + 1;
      }
      else if(prevLocationOfStones[0] == 6 || prevLocationOfStones[0] == 7){
        moveTheStone(stoneXs[0]-1, stoneYs[0], Color.GREEN, buttonWithStone[0], 1);
        stoneXs[0] = stoneXs[0] - 1;
      }
    }
    if(!continueGame()){
      return; 
    }
    
    //boolean to check to see if can keep going
    while(checkToKeepGoingPlayer(stoneXs[1],stoneYs[1], stoneXs[0], stoneYs[0],2) && continueGame){
      if(prevLocationOfStones[1] == 0 || prevLocationOfStones[1] == 1){
        moveTheStone(stoneXs[1], stoneYs[1]-1, Color.BLUE, buttonWithStone[1], 2);
        stoneYs[1] = stoneYs[1] - 1;
      }
      else if(prevLocationOfStones[1] == 2 || prevLocationOfStones[1] == 3){
        moveTheStone(stoneXs[1]+1, stoneYs[1], Color.BLUE, buttonWithStone[1], 2);
        stoneXs[1] = stoneXs[1] + 1;
      }
      else if(prevLocationOfStones[1] == 4 || prevLocationOfStones[1] == 5){
        moveTheStone(stoneXs[1], stoneYs[1] + 1, Color.BLUE, buttonWithStone[1], 2);
        stoneYs[1] = stoneYs[1] + 1;
      }
      else if(prevLocationOfStones[1] == 6 || prevLocationOfStones[1] == 7){
        moveTheStone(stoneXs[1]-1, stoneYs[1], Color.BLUE, buttonWithStone[1], 2);
        stoneXs[1] = stoneXs[1] - 1;
      }
    }
  }
  
  /*
  /*
   * This helper method is supposed to determine whether or not it is possible for the stone to move tiles
   * @param stoneX the x coordinate of the stone to possibly move
   * @param stoneY the y coordinate of the stone to possibly move 
   * @param stoneX2 the x coordinate of the stone in place
   * @param stoneY2 the y coordinate of the stone in place
   * @param player the player's stone currently checking
   * @return boolean value if check can keep going 
   */ 
  public boolean checkToKeepGoingPlayer(int stoneX, int stoneY,int stoneX2, int stoneY2, int player){
    
    int prevLocationOfStone = prevLocationOfStones[player - 1];
    boolean returnValue = false; 
    
    if(prevLocationOfStone == 0 || prevLocationOfStone == 1){
      if((stoneY - 1 >= 0) && tsuroButtonsGP[stoneX][stoneY - 1].getConnections() != null){
        stoneY = stoneY -1; 
        returnValue = true; 
      } 
    }
    else if(prevLocationOfStone == 2 || prevLocationOfStone == 3){
      if((stoneX + 1 < numColumns) && tsuroButtonsGP[stoneX+1][stoneY].getConnections() != null){
        stoneX = stoneX + 1; 
        returnValue = true; 
      }
    }
    else if(prevLocationOfStone == 4 || prevLocationOfStone == 5){
      if((stoneY + 1 < numRows) && tsuroButtonsGP[stoneX][stoneY + 1].getConnections() != null){ 
        stoneY = stoneY + 1; 
        returnValue = true; 
      }
    }
    else if(prevLocationOfStone == 6 || prevLocationOfStone == 7){
      if((stoneX-1 >= 0) && tsuroButtonsGP[stoneX-1][stoneY].getConnections() != null){
        stoneX = stoneX - 1; 
        returnValue = true; 
      }
    }
    if(player == 1){
      if((stoneX2 == stoneX) && (stoneY2 == stoneY) && (locationOfStones[0] == prevLocationOfStones[1])){
        System.out.println("Drew when checking player 1's move"); 
        continueGame = false; 
        playerLost[0] = true;
        playerLost[1] = true;
        return false;
      }
    } else{
      if((stoneX2 == stoneX) && (stoneY2 == stoneY) && (locationOfStones[1] == prevLocationOfStones[0])){
        System.out.println("Drew when checking player 2's move"); 
        continueGame = false; 
        playerLost[0] = true;
        playerLost[1] = true;
        return false; 
      }
    }
    return returnValue;
  }
  
    /*
   * "Moves" the stone to the new location. This is done by removing the stone of the current tile, and placing 
   * the new stone into the right spot 
   * @param x the x coordinate of the new stone 
   * @param y the y coordinate of the new stone 
   * @param color is the color of the new stone 
   * @param buttonWithStone is the button currently with the stone. The address it points to will be updated
   * @param player is player currently checking
   */ 
  public void moveTheStone(int x, int y, Color color, TsuroButton bWithStone, int player){
    int prevLocationOfStone = prevLocationOfStones[player - 1]; 
    int locationOfStone = locationOfStones[player - 1] ; 
    removeStonesFromAllPlayerHands();
    bWithStone.removeStone(prevLocationOfStone); 
    bWithStone = tsuroButtonsGP[x][y]; 
    bWithStone.addStone(color,bWithStone.getConnections()[locationOfStones[player - 1]]); 
    locationOfStones[player - 1] = bWithStone.getConnections()[locationOfStone];
    updateLocationOfStone(player); 
    if(player == 1){
      buttonWithStone[0] = tsuroButtonsGP[x][y]; 
    } else if(player == 2){
      buttonWithStone[1] = tsuroButtonsGP[x][y]; 
    }
    removeStonesFromAllPlayerHands();
    addStonesToAllPlayerHands(); 
    
  }
  /*
   * Updates the location of the stone because the location of the stone on the new tile will start differently. 
   * @param player is stone of the player who we want to update location of 
   */ 
  public void updateLocationOfStone(int player){
    prevLocationOfStones[player - 1] = locationOfStones[player - 1];    
    if(locationOfStones[player - 1] >= 0 && locationOfStones[player - 1] <= 3){
      locationOfStones[player - 1] = locationOfStones[player - 1] + 4; 
    } else if(locationOfStones[player - 1] >= 4 && locationOfStones[player - 1] <= 7){
      locationOfStones[player - 1] = locationOfStones[player - 1] - 4; 
    }
    
  }
  
  
  /*
   * Checks the button tiles and stone location to see if any of them are at the edge. 
   * @returns returns false if the game is over 
   */
  public boolean continueGame(){
    int stoneX = 100; 
    int stoneY = 100; 
    TsuroButton buttonStone = new TsuroButton(50,50); 
    int prevLocationOfStone = -100; 
    //need to repeat this loop for both players
    for(int p = 0; p < 2; p++){
      prevLocationOfStone = prevLocationOfStones[p]; 
      buttonStone = buttonWithStone[p]; 
      for(int i = 0; i < numColumns; i++){
        for(int j = 0; j < numRows; j++){
          if(tsuroButtonsGP[i][j] == buttonStone){
            stoneX = i; 
            stoneY = j; 
          }
        }
      }
      if((prevLocationOfStone == 0 || prevLocationOfStone == 1) && stoneY == 0){
        playerLost[p] = true;  
      } else if((prevLocationOfStone == 4 || prevLocationOfStone == 5) && stoneY == numRows - 1){
        
        playerLost[p] = true;  
      } else if((prevLocationOfStone == 2 || prevLocationOfStone == 3) && stoneX == numColumns-1){
        playerLost[p] = true;  
      } else if((prevLocationOfStone == 6 || prevLocationOfStone == 7) && stoneX == 0){
        playerLost[p] = true;  
      }
    }
    if(playerLost[0] == true || playerLost[1] == true){
      return false; 
    }
    return true; 
  }
}