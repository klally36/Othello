## Reversi Game (Hexagon) - Part 5

### Overview
This project is an extension of the classic board game Reversi, also known as Othello. The game, played on a hexagonal board with polygonal cells, has been further developed with additional features for enhanced gameplay and interaction.

#### Extensibilities
By abstracting the design of the board and the cells into shape of arbitrary regular polygons, the game could be easily adapted to cells with **different numbers of edges**, **different orientation of the cells**,  and **different initial game status**.

#### Rules
This version of the implementation is a PvP game - Black Player and White player take turns to place their disc on the board. The current turn of the player is displayed at the bottom, right corner of the UI. **To pass one's turn, press 'p'**. At each turn, the grids with cyan color indicates **all the legal move** for the current player. Play a move by **pressing the mouse on the desired grid**. When both sides have no legal move available on the board, the game ends, and the winner (or draw) is indicated by the text that pops up at the bottom of the interface.

### Quick start:
You may simply run the main method in the ./src/Reversi.java to start the game, or:
+ Here is a simple example of how to start a game of Reversi:
+ ```java
+ // Create a new Reversi model with a board size of 6 and a cell radius of 30.0, with the first vertex positioning at 90 degree:
+ ReversiModel model = new HexagonReversi(6, 30.0, Math.PI / 2);
+ // Create a new Reversi controller
+ ReversiController controller = new ReversiVisualController();
+ // Start the game with a window width of 800 and a height of 600
+ controller.playGame(model, 800, 600);
+ ```

### Invariant
#### Cell Status
Description: The cell can **Only be** played on if it is **EMPTY**; Once a cell is set to one color, it can **never** be set back to **EMPTY**.
Enforcement: This invariant is enforced by the setCellStatus method, as it is not allowed to set a cell back to EMPTY, and for any cell with non-EMPTY status, its status is only changed by the function flip(), which only changes white -> black or black -> white.

### Key Components:
#### Model:
The `ReversiModel` interface and its implementation `HexagonReversi` represent the game state, including the game board and the players. The game state includes the current turn, the board state, and the game status (whether the game is ongoing, or if a player has won or if it's a draw). The `ReversiModel` interface is designed for game with Polygon with any number of sides, and `HexagonReversi` is the detailed implementation in Hexagon. In general, the model takes in valid user inputs passed from the `ReversiVisualController`, and execute them by changing the status of board.

##### Model Subcomponent:
The `HexagonReversi` consists of a `Board` (representing all the cells and their status), a `GameStatus` (current status of the game), and two `Player`s that are either human or AI. the **react()** method in the model handles a sequence of reaction after a user has made a valid move:
- Check if the game ends (if so adjust everything and end the world accordingly)
- Check if it is a turn of AI player, if so, let the AI player makes the move


#### Board:
The `Board` interface and its implementation `PolygonGameBoard` represent the game board. The board is a collection of cells, each of which can be empty, hold a black piece, or hold a white piece. The board is responsible for maintaining the state of the game, including the status of each cell and the total number of black and white pieces.

##### Board Subcomponent:
First of all, a `PolygonGameBoard` consists of a 2D grid of cells with a **different indexing method**. Specifically, grid[x][y] represents the yth cell on the xth circle of cells counting from the center.
The board is then innitialized from the center cell(s) and spiraling out in a counter-clockwise direction, using the addition between `CartesianPosition` and `PolarPosition`.


#### Cell:
The `Cell` interface and its implementation `PolygonCell` represent a cell with n equal-length edges on the game board. Each cell has a status (empty, black, or white) and a position on the board. The cell is responsible for maintaining its own status and position.

##### Cell Subcomponent:
`PolygonCell` records all the virtices using a relative `PolarPosition`. A given `Position`  can be easily judged if it is within the cell by finding the **number of intersection points** between the ray starts from the given `Position` to the right with all the edge between the virticies.

#### Player:
The `Player` interface and its implementation `HumanPlayer` represent a player in the game. The player can be a human or an AI, and is responsible for making moves during their turn. This is handles by the `ReversiModel` model.

#### Position:
The `Position` interface and its implementations `AbstractPosition`, `CartesianPosition`, and `PolarPosition` represent the position of a cell on the game board. Positions can be calculated and updated in different ways depending on the implementation. The two 2D-positon system that we implemented - `CartesianPosition` and `PolarPosition` - can be used interchangably, converted, added, or subtracted. This provides great help to our implementation of arbitrary `PolygonGameBoard` and `PolygonCell`.

#### Status:
The `GameStatus` and `CellStatus` enumerations represent the state of the game and individual cells, respectively. The game status can be black's turn, white's turn, black wins, white wins, or a draw. The cell status can be empty, black, or white. Pretty straightforward.

+ ### Source Organization:
+ The source code for this project is organized into several packages, each of which provides a specific set of components for the game.
+
+ - `cs3500.reversi.model.implem`: This package contains the implementations of the key components of the game, including the game model (`HexagonReversi`), the game board (`PolygonGameBoard`), the cells (`PolygonCell`), and the players (`HumanPlayer`).
+ - `cs3500.reversi.model.interf`: This package contains the interfaces for the key components of the game, including the game model (`ReversiModel`), the game board (`Board`), the cells (`Cell`), and the players (`Player`).
+ - `cs3500.reversi.model.position`: This package contains the classes for representing positions on the game board, including `AbstractPosition`, `CartesianPosition`, `AbstractPosition`, and `PolarPosition`.
+ - `cs3500.reversi.model.status`: This package contains the enumerations for representing the status of the game (`GameStatus`) and individual cells (`CellStatus`).
+ - `cs3500.reversi.controller`: This package contains the controller for the game (`ReversiController` and `ReversiVisualController`), which handles user inputs from the interface and controls the flow of the game.
+ - `cs3500.reversi.view`: This package contains the graphical visual user interface for the game.
+
+ The test classes for the key components of the game are located in the `cs3500.reversi.test` package.


## Changes for part 2

### Overview
Since we have already implemented the interactive GUI with Graphics in Part I, and we did a relatively good job on abstracting our design, minimal changes are needed for this new assignment.


### Graphics2D: Use or Not
In the previous assignment, we have use Graphics as the main render of the GUI. To cope with the relatively simple functionalities it provides, we have designed two coordinate systems - `CartesianPosition` and `PolarPosition` - that can be used interchangably to help us with the transformation in space. For instance, we use a `CartesianPosition` to represent the center of a Hexagon cell, and use 6 relative `PolarPosition` to conviniently represent the six vertices. Therefore, we found it enough to stick to our existing designs.

### More Rules & More Features!

#### Select & Deselect
To fulfill the requirements of the assignment we change our original design or "click to play" into "click to select/deselect, press enter to confirm". Our view let the user deselect a selected cell by (1) clicking on it again, (2) clicking on another cell (in which case the view selects that new cell), or (3) clicking outside the boundary of the board. Clicking outside the boundary of the board **will not** cause the view to crash, throw an exception, or otherwise break.

Note: Since it is meaningless to select cell that you cannot play the move on, we forbid such behavior and you may only select cells that are your next legal moves.

### Highlighting Legal Moves
To make the game easier for beginners (like us!). At each turn, we use the color green to highlight those cells that one could play. This will refresh consistently with other component of the GUI, like after you press "P" to switch turns.

### Press "Enter" to Confirm
After selecting a cell, you may press "Enter" to confirm your selection and the model will update the game accordingly.

### Important Method: update()
To adjust the game model with the aforementioned changes, we have changed our move handling mechanism. We change react() into update(), which check the status of the game and let one player play the move according to their strategies: for AI, it depends on the specific strategies. For human player, it will always play the cell that is selected (If no cell is selected then nothing happens)

### ReadonlyReversiModel Interface
By splitting the ReversiModel Interface into 2:
- the interface for actual functions that may lead to modification of the game status
- and, an interface that provides method for the View to render the game correctly
  we forbid any possible modification from the view to the model. Hence it has to pass the information to the controller in order to interact with the model.

### Specify Player: Constructor of HexagonReversi:
Now, you may specify the player when creating a HexagonReversi instance. you may let two human play against each other, human v.s. AI, or simply AI v.s. AI

### New Features: 2 AIPlayers
We have implemented 2 AIPlayers (that implements the `Player` interface) with different levels:
- `CrazyAIPlayer`: A crazy AI that play the next move randomly from all the legal move options.
- `AdvancedAIPlayer`: A advanced AI that uses a combination of four different strategies controlled by parameters specified during construction.

### Strategies & Chain-of-Strategies
For `AdvancedAIPlayer` we have implemented a combination of 4 different strategies and the detailed behavior is controlled by 4 parameter, which is mathematically equivalent of chaining the strategies together.
- Corner Tendency: 0 for no such behavior; larger the parameter, the more likely the model will choose the move that is at the corner.
- Next-To-CornerPhobia: 0 for no such behavior; larger the parameter, the more likely the model will avoid making a move that is adjacent to corner.
- Greedy: 0 for no such behavior; larger the parameter, the more likely the model will choose the move that flips the most pices.
- Cautiousness (minimax): 0 for no such behavior; larger the parameter, the more likely the model will choose to avoid letting the oponent getting more scores in his/her/it next move.
## New Features and Components for Part 3
- **Controller Implementation**: Introduction of a new controller for managing game flow, acting as a bridge between the model and the view.
- **Listener Interfaces**: Design and integration of listener interfaces to respond accurately to both user inputs and game state changes.
- **Asynchronous GUI Controllers and Synchronous Gameplay**: Addressing the challenge of merging asynchronous GUI controllers with the synchronous rules of Reversi.
- **Player Notifications and Responses**: Enhanced model to notify players of their turn and await their move or pass decisions.
- **Handling Invalid Moves**: We have designed our game logic to prevent invalid moves systematically. As such, we opted not to include a pop-up menu for invalid moves, aligning with our approach to maintain game integrity and user experience.

### Key Updates
- **Enhanced Model and View**: Incorporation of feedback and completion of previously missing functionalities.
- **Comprehensive Player Design**: Implementation of both human and machine players using a unified interface.
- **Controller Functionality**: Enhanced controller design to manage player actions and model updates efficiently.

## Additional Design Considerations

### Forbidding Invalid Moves Without Notifications
- **Proactive Design Against Invalid Moves**: In our game design, we have taken proactive measures to prevent invalid moves from occurring. This approach is deeply embedded in the game logic and the way player interactions are handled.
- **No Notification System for Invalid Moves**: As a result of our design decision to prevent invalid moves at the system level, we have opted not to include a notification system or pop-up menus to indicate invalid moves. We believe this makes for a smoother and more intuitive gameplay experience.
### Emphasis on User Experience
- **Intuitive Interaction**: We have designed the game with the aim of creating an intuitive interaction model. Players can easily understand and follow the rules without the need for additional prompts or warnings for invalid actions.
- **Streamlined Gameplay**: By eliminating unnecessary interruptions, we have streamlined the gameplay, allowing players to focus on strategy and enjoy the challenge of Reversi.

### Console Output of Selected Cells
- **Coordinate Display in Console**: Each time a cell is selected in the game, its specific coordinate pair is displayed in the console. This feature provides immediate feedback to the player about their actions, enhancing the game's interactivity and making debugging easier.

### Mock Output for Strategy Transcript
- **MockModelStrategy1 Transcript**: We have implemented a `MockModelStrategy1` with a `StringBuilder` log to track and output the strategy's operations. This mock is designed to provide a transcript of the strategy's decisions and actions, aiding in the understanding and debugging of our game logic.
- This transcript demonstrates the sequence of method calls and interactions within the `MockModelStrategy1`, providing insight into the strategy's operational flow, confirming if 'getBoard, 'startGame', and other methods are properly called.

## New Features and Components for Part 4
We document the integration of the views and features provided by our provider group. This documentation aims to clearly outline which aspects were successfully implemented and which were not, providing clarity for grading and assessment.


## Integration and Functionality of PlayerAdapter

### Overview
We created a player `PlayerAdapter’ class that serves as a bridge between the provider's system and ours, particularly in handling the differences in game state representation and player moves.

### Key Functionalities of PlayerAdapter
- **Coordinate Conversion**: The most important role of `PlayerAdapter` is to convert coordinates between the provider's system and ours. Our system used a unique coordinate system, while the provider's used the standard axial system. `PlayerAdapter` contains logic to translate positions and moves from one system to the other, ensuring that the game state and player actions are correctly interpreted across both systems.
- **Game State Translation**: `PlayerAdapter` also translates the game state from the provider's model to our model's format. This includes interpreting the board layout, player turns, and game status, and converting them into a format that our system can process and display.
- **Move Execution**: When it comes to executing moves, `PlayerAdapter` takes the input from our system (like a player's move), converts it into the provider's format, and then executes the move in the provider's model. It also handles the reverse process, where it translates moves from the provider's model back into our system.
- **Handling Game Logic Discrepancies**: In instances where there were discrepancies in game logic or rules between the two systems, `PlayerAdapter` contained additional logic to reconcile these differences. This ensured that the gameplay remained consistent and fair, regardless of the underlying system handling the move.
### Successfully Integrated Features
- **Basic View Integration**: We successfully integrated the basic framework of the provider's views into our existing system. This includes the primary layout and structural components necessary for displaying the game board and pieces.
- **Coordinate System Mapping**: Despite differences in coordinate systems, we managed to map the provider's view coordinate system to our model. This required the development of a `PlayerAdapter` to facilitate the translation between the two systems.
- **User Interaction**: Key user interaction features such as selecting cells and visualizing legal moves were successfully incorporated. We adapted these features to align with our game's logic and user interface design.
- **Game State Display**: The provider's views were good at displaying various game states, and we were able to integrate these aspects without any trouble. This extends to updating the game board based on player moves and game progression.

### Features Not Integrated:
- **Command-line Argument Support**: We faced challenges integrating the provider's views that relied on command-line arguments due to our project's lack of support in this area. As a result, certain functionalities tied to command-line inputs could not be implemented.
- **Advanced Interactive Elements**: Some of the more advanced interactive elements and optional features in the provider's views were not integrated. This was primarily due to compatibility issues and our focus on ensuring the core gameplay mechanics were robust.
- **Customization Features**: Features that allowed customization of the game's appearance or settings were not integrated because they required extensive modifications to our existing codebase which were not feasible within the project's scope.

### Reflection on Integration Process
- **Challenges and Learning**: The process of integrating the provider's views highlighted the importance of flexible and adaptable design, especially in collaborative projects like this one. It re-emphasized the need for standardization in key areas such as coordinate systems and position to ease our integration challenges.
- **Future Improvements**: In future iterations, efforts will be focused on enhancing the adaptability of the views to different environments and expanding the range of supported features.

## Reversi Game (Hexagon) - Part 5

### FAQs and Design Choices
#### Some common FAQs
- **Q: Why 1 view instead of 2?**  
  **A:** We assign two controllers to one view for simpler presentation.

- **Q: Allow selecting cells that are not legal moves?**  
  **A:** No. Only green cells, which are legal moves, can be selected.

- **Q: Coordinate system?**  
  **A:** We use a "polar-like" coordinate system for move game boards, with (x, y) representing the "circle" to the center and angle in the "circle".

- **Q: Can we select a cell that is not legal for a move?**  
  **A:** No. This design choice simplifies gameplay by restricting selection to only legal moves.

- **Q: Redundant code?**  
  **A:** No redundant code. We utilize abstract classes for consistent model/board behavior across different numbers of cell edges.

### Major Changes
- Abstracted some Board/Model behavior for flexibility in handling polygons.
- Foreseen modifications from the start, leading to a design that allows cells and boards to be adaptable to different polygons.

### Arguments and Example Commands for Starters
1. **Basic Command:**  
   `java -jar Final.jar <GameType> <BoardSize> <CellRadius> <OpponentType>`
    - `GameType`: "hexa" or "square"
    - `BoardSize`: Size of the Board (int)
    - `CellRadius`: Radius of the cell (int)
    - `OpponentType`: "human" or "ai"

2. **Extended Command with Strategies:**  
   `java -jar Final.jar <GameType> <BoardSize> <CellRadius> <OpponentType> <CornerPlay> <AvoidNextToCorner> <CaptureMoreCells> <MinimizeMaxOpponentCapture>`
    - `CornerPlay`: Preference to Play at Corner (int)
    - `AvoidNextToCorner`: Preference to Avoid Play Next to Corner (int)
    - `CaptureMoreCells`: Preference to Capture more Cells (int)
    - `MinimizeMaxOpponentCapture`: Preference to Minimize the Max of Opponent's Captured Cells (int)

#### Examples:
- PvP Hexagon game, 6 circles of cells: `java -jar Final.jar hexa 6 30 human`
- PvAI Square game, 4 strategies, 5 circles of cells: `java -jar Final.jar square 5 50 ai`
- PvAI Square game, minimax strategy: `java -jar Final.jar square 5 50 ai 0 0 0 1`
- PvAI Hexagon game, minimax and greedy strategy: `java -jar Final.jar square 5 50 ai 0 0 10 1`
