import java.awt.Frame;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.*;

public class KalicMarkoA1Q1 implements GLEventListener
{
	public static final boolean TRACE = true;

	public static final String WINDOW_TITLE = "A1Q1: Marko Kalic";
	
	public static final int INITIAL_WIDTH = 640;
	public static final int INITIAL_HEIGHT = 640;
	
	public static final float PHI = 1.59f;
	public static final float N = 3.05f;
	public static final float A = 0.0025f;
	public static final float B = 0.306349f;

	public static void main( String[] args )
	{
		final Frame frame = new Frame( WINDOW_TITLE );

		frame.addWindowListener( new WindowAdapter( )
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );

		final GLProfile profile = GLProfile.get( GLProfile.GL2 );
		final GLCapabilities capabilities = new GLCapabilities( profile );
		final GLCanvas canvas = new GLCanvas( capabilities );
		try
		{
			Object self = self( ).getConstructor( ).newInstance( );
			self.getClass( )
					.getMethod( "setup", new Class[] { GLCanvas.class } )
					.invoke( self, canvas );
			canvas.addGLEventListener( (GLEventListener)self );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			System.exit( 1 );
		}
		canvas.setSize( INITIAL_WIDTH, INITIAL_HEIGHT );
		canvas.setAutoSwapBufferMode( true );

		frame.add( canvas );
		frame.pack( );
		frame.setVisible( true );

		System.out.println( "\nEnd of processing." );
	}

	private static Class<?> self( )
	{
		// This ugly hack gives us the containing class of a static method
		return new Object( )
		{
		}.getClass( ).getEnclosingClass( );
	}

	public void setup( final GLCanvas canvas )
	{
		// Called for one-time setup
		if ( TRACE )
			System.out.println( "-> executing setup()" );
	}

	@Override
	public void init( GLAutoDrawable drawable )
	{
		// Called when the canvas is (re-)created - use it for initial GL setup
		if ( TRACE )
			System.out.println( "-> executing init()" );

		final GL2 gl = drawable.getGL( ).getGL2( );

		// TODO: choose your own background colour, and uncomment the lines
		// below to turn on line antialiasing
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
		// gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
		// gl.glEnable(GL2.GL_LINE_SMOOTH);
		// gl.glEnable(GL2.GL_BLEND);
		// gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void display( GLAutoDrawable drawable )
	{
		// Draws the display
		if ( TRACE )
			System.out.println( "-> executing display()" );

		final GL2 gl = drawable.getGL( ).getGL2( );
		gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );

		// TODO: your drawing code goes here
		
		drawSquares( gl );
		
		drawSpiral( gl );
	}

	private void drawSquares( GL2 gl )
	{
		float x = 0.0f;
		float y = 0.0f;
		float x2 = 0.0f;
		float y2 = 0.0f;
		float r;
		float redLevel = 0.0f;
		float greenLevel = 0.0f;
		
		boolean swap = true;
		
		for ( float theta = 0.0f; theta <= N * 2.0f * Math.PI; theta += PHI )
		{
			r = (float)( A * Math.exp( B * theta ) );
			
			if ( !swap )
			{
				x = (float)( r * Math.cos( theta ) );
				y = (float)( r * Math.sin( theta ) );
				
				gl.glColor3f( redLevel, greenLevel, 0.0f );
				gl.glBegin( GL2.GL_QUADS );
				
					gl.glVertex2f( x, y );
					gl.glVertex2f( x2, y );
					gl.glVertex2f( x2, y2 );
					gl.glVertex2f( x, y2 );
				
				gl.glEnd( );
				gl.glFlush( );
				
				greenLevel += 0.15f;
				swap = !swap;
			}
			else
			{
				x2 = (float)( r * Math.cos( theta ) );
				y2 = (float)( r * Math.sin( theta ) );
				
				gl.glColor3f( redLevel, greenLevel, 0.0f );
				gl.glBegin( GL2.GL_QUADS );
				
					gl.glVertex2f( x, y );
					gl.glVertex2f( x2, y );
					gl.glVertex2f( x2, y2 );
					gl.glVertex2f( x, y2 );
				
				gl.glEnd( );
				gl.glFlush( );
				
				redLevel += 0.15f;
				swap = !swap;
			}
		}
	}
	
	private void drawSpiral( GL2 gl )
	{
		float x = 0.0f;
		float y = 0.0f;
		float r = 0.0f;
		
		gl.glColor3f( 0.0f, 0.0f, 1.0f );
		gl.glLineWidth( 2.0f );
		gl.glBegin( GL2.GL_LINE_STRIP );
		
		for ( float theta = 0.0f; theta < N * 2.0f * Math.PI; theta += 0.109f )
		{
			r = (float)( A * Math.exp( B * theta ) );
			
			x = (float)( r * Math.cos( theta ) );
			y = (float)( r * Math.sin( theta ) );
			
			gl.glVertex2f( x, y );
		}
		
		gl.glEnd( );
		gl.glFlush( );
	}

	@Override
	public void dispose( GLAutoDrawable drawable )
	{
		// Called when the canvas is destroyed (reverse anything from init)
		if ( TRACE )
			System.out.println( "-> executing dispose()" );
	}

	@Override
	public void reshape( GLAutoDrawable drawable, int x, int y, int width,
			int height )
	{
		// Called when the canvas has been resized
		// Note: glViewport(x, y, width, height) has already been called so
		// don't bother if that's what you want
		if ( TRACE )
			System.out.println( "-> executing reshape(" + x + ", " + y + ", "
					+ width + ", " + height + ")" );

		final GL2 gl = drawable.getGL( ).getGL2( );

		final float ar = (float)width / ( height == 0 ? 1 : height );

		gl.glViewport( x, y, width, height );

		gl.glMatrixMode( GL2.GL_PROJECTION );
		gl.glLoadIdentity( );
		gl.glOrthof( ar < 1 ? -1.0f : -ar, ar < 1 ? 1.0f : ar, ar > 1 ? -1.0f
				: -1 / ar, ar > 1 ? 1.0f : 1 / ar, 0.0f, 1.0f );
		gl.glMatrixMode( GL2.GL_MODELVIEW );
		gl.glLoadIdentity( );
	}
}
