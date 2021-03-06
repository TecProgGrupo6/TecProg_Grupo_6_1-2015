//import the packages for using the classes in them into the program

import javax.swing.*;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.sql.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrintingBooks extends JInternalFrame implements Printable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Log system from PrintingBooks class
	private final static Logger LOGGER = Logger.getLogger( PrintingBooks.class.getName() );

	/***************************************************************************
	 *** declaration of the private variables used in the program ***
	 ***************************************************************************/

	// Connection status
	private Connection connection = null;

	// Creating the statement
	private Statement statement = null;

	// Resultset from the statement which comes from the data base
	private ResultSet resultset = null;

	// Constant of the URL from the database
	private String URL = "jdbc:odbc:JLibrary";

	// For creating the text area
	private JTextArea textArea = new JTextArea();

	// For creating the vector to use it in the print
	private Vector<String> lines;

	// Tab size
	public static final int TAB_SIZE = 5;

	// Constructor of JLibrary
	public PrintingBooks ( String query ){

		super( "Printing Books" , false , true , false , true );
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Books were printed");

		// For getting the graphical user interface components display area
		Container cp = getContentPane();

		// For setting the font
		this.textArea.setFont( new Font( "Tahoma" , Font.PLAIN , 9 ) );

		// For adding the textarea to the container
		cp.add( this.textArea );

		try{

			Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );

		}catch ( ClassNotFoundException ea ){

			System.out.println( ea.toString() );

		}catch ( Exception e ){

			System.out.println( e.toString() );

		}

		/***************************************************************
		 * For making the connection,creating the statement and update * the
		 * table in the database. After that,closing the statmenet * and
		 * connection. There is catch block SQLException for error *
		 ***************************************************************/

		try{

			this.connection = DriverManager.getConnection( this.URL );
			this.statement = this.connection.createStatement();
			this.resultset = this.statement.executeQuery( query );
			this.textArea.append( "=============== Books Information ===============\n\n" );

			while ( this.resultset.next() ){

				this.textArea.append( "Subject: " + this.resultset.getString( "Subject" ) + "\n" + "Title: " + this.resultset.getString( "Title" ) + "\n"
						+ "Author(s): " + this.resultset.getString( "Author" ) + "\n" + "Copyright: " + this.resultset.getString( "Copyright" )
						+ "\n" + "Edition: " + this.resultset.getString( "Edition" ) + "\n" + "ISBN: " + this.resultset.getString( "ISBN" ) + "\n"
						+ "Library: " + this.resultset.getString( "Library" ) + "\n\n" );

			}

			this.textArea.append( "=============== Books Information ===============" );
			this.resultset.close();
			this.statement.close();
			this.connection.close();

		}catch ( SQLException SQLe ){
			System.out.println( SQLe.toString() );
		}
		// For setting the visible to true
		setVisible( true );

		// To show the frame
		pack();
	}

	// Printing books
	@Override
	public int print ( Graphics pg , PageFormat pageFormat , int pageIndex ) throws PrinterException{

		pg.translate( (int) pageFormat.getImageableX() , (int) pageFormat.getImageableY() );

		int wPage = (int) pageFormat.getImageableWidth();
		int hPage = (int) pageFormat.getImageableHeight();
		pg.setClip( 0 , 0 , wPage , hPage );

		pg.setColor( this.textArea.getBackground() );
		pg.fillRect( 0 , 0 , wPage , hPage );
		pg.setColor( this.textArea.getForeground() );

		Font font = this.textArea.getFont();
		pg.setFont( font );
		FontMetrics fm = pg.getFontMetrics();
		int hLine = fm.getHeight();

		if ( this.lines == null ){

			this.lines = getLines( fm , wPage );

		}else{
			// No action
		}

		int numLines = this.lines.size();
		int linesPerPage = Math.max( hPage / hLine , 1 );
		int numPages = (int) Math.ceil( (double) numLines / (double) linesPerPage );

		if ( pageIndex >= numPages ){
			this.lines = null;
			return NO_SUCH_PAGE;
		}else{
			// No action
		}

		int x = 0;
		int y = fm.getAscent();
		int lineIndex = linesPerPage * pageIndex;

		while ( lineIndex < this.lines.size() && y < hPage ){

			String str = this.lines.get( lineIndex );
			pg.drawString( str , x , y );
			y += hLine;
			lineIndex++;

		}
		return PAGE_EXISTS;
	}

	protected Vector<String> getLines ( FontMetrics fm , int wPage ){

		Vector<String> v = new Vector<>();

		String text = this.textArea.getText();
		String prevToken = "";
		StringTokenizer st = new StringTokenizer( text , "\n\r" , true );

		while ( st.hasMoreTokens() ){

			String line = st.nextToken();

			if ( line.equals( "\r" ) ){

				continue;
			}

			// StringTokenizer will ignore empty lines, so it's a bit tricky to
			// get them...
			if ( line.equals( "\n" ) && prevToken.equals( "\n" ) ){

				v.add( "" );

			}else{
				// No action
			}

			prevToken = line;

			if ( line.equals( "\n" ) ){

				continue;

			}else{
				// No action
			}

			StringTokenizer st2 = new StringTokenizer( line , " \t" , true );
			String line2 = "";

			while ( st2.hasMoreTokens() ){

				String token = st2.nextToken();

				if ( token.equals( "\t" ) ){
					int numSpaces = TAB_SIZE - line2.length() % TAB_SIZE;
					token = "";
					for ( int k = 0 ; k < numSpaces ; k++ )
						token += " ";
				}else{
					// No action
				}

				int lineLength = fm.stringWidth( line2 + token );

				if ( lineLength > wPage && line2.length() > 0 ){

					v.add( line2 );
					line2 = token.trim();
					continue;

				}else{
					// No action
				}
				line2 += token;
			}
			v.add( line2 );
		}
		return v;
	}
}
