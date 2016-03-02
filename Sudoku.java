import java.util.*;
import java.io.*;


class Sudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
     //Grid[row][col]
    int Grid[][];

    //Return true if value is valid in row
    public boolean isValidRow(int col, int n) {
      for(int i = 0; i<this.N; i++) {
        if(this.Grid[i][col] == n) {
          return false;
        }
      }
      return true;
    }

    //Return true if value is valid in column
    public boolean isValidCol(int row, int n) {
      for(int j = 0; j<this.N; j++) {
        if(this.Grid[row][j] == n) {
          return false;
        }
      }
      return true;
    }

    //Return true if value is valid in "box"
    public boolean isValidBox(int row, int col, int n) {
      //Operations on int values will round to the beginning and end points of boxes
      int row_i = row/this.SIZE*this.SIZE;
      int row_f = row_i+this.SIZE;

      int col_i = col/this.SIZE*this.SIZE;
      int col_f = col/this.SIZE*this.SIZE;

      for (int i=row_i; i<row_f; i++) {
        for(int j=col_i; j<col_f; j++) {
          if(Grid[i][j] == n) {
            return false;
          }
        }
      }
      return true;
    }

    //Returns true if all the 3 validation functions return true
    public boolean isValid(int row, int col, int n) {
      if(!isValidRow(col, n) || !isValidCol(row, n) || !isValidBox(row, col, n)) {
        return false;
      } else {
        return true;
      }
    }

    //Finds first incomplete square and returns its coordinates
    //If there are no incomplete squares, return null
    public int[] incomplete() {
      for(int i=0; i<this.N; i++) {
        for(int j=0; j<this.N; j++) {
          if(Grid[i][j] == 0) {
            int[] incomplete = {i, j};
            return incomplete;
          }
        }
      }
      return null;
    }

    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */
    public boolean solve()
    {
      //Get first incomplete square
      int[] emptySquare = incomplete();

      //If all squares are complete, return true
      //Puzzle solved!
      if(emptySquare == null) {
        return true;
      }

      int row = emptySquare[0];
      int col = emptySquare[1];

      //Check values 1-N
      for(int n=1; n<=this.N; n++) {
        //If valid, temporarily assign n
        if(isValid(row, col, n)) {
          this.Grid[row][col] = n;

          //recursive call that will end when all squares are complete
          if(solve()){
            return true;
          }

          //unnassigns the square if value is impossible
          this.Grid[row][col] = 0;
        }
      }
      //false when no value 1-N can satisfy the requirements
      return false;
    }


    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /*****************************************************************************/

    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku( int size )
    {
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                Grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
	String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width )
    {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print()
    {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the Grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( Grid[i][j] ), digits );
                // Print the vertical lines between boxes
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input,
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception
    {
        InputStream in;
        if( args.length > 0 )
            in = new FileInputStream( args[0] );
        else
            in = System.in;

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku s = new Sudoku( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );

        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.

        //long startTime = System.nanoTime();
        System.out.println("Solved: "+ s.solve());
        //long endTime = System.nanoTime();

        //System.out.println("Runtime: " + (endTime - startTime)/1000 + " us");

        // Print out the (hopefully completed!) puzzle
        s.print();
    }
}
