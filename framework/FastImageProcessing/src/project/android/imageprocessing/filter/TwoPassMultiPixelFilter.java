package project.android.imageprocessing.filter;

import android.opengl.GLES20;

/**
 * An extension of TwoPassFilter.  This class acts the same as {@link TwoPassFilter} except it
 * passes the shaders information about texel width and height. On the first pass, the texel width
 * will be set but the height will be 0. On the second pass the texel height will be set but the width will be 0. 
 * This allows for a vertical and horizontal pass of the input. For more details about multi-pixel 
 * rendering, see {@link MultiPixelRenderer}.
 * @author Chris Batt
 */
public abstract class TwoPassMultiPixelFilter extends TwoPassFilter {
	protected static final String UNIFORM_TEXELWIDTH = "u_TexelWidth";
	protected static final String UNIFORM_TEXELHEIGHT = "u_TexelHeight";
	
	protected float texelWidth;
	protected float texelHeight;
	private int texelWidthHandle;
	private int texelHeightHandle;
	
	@Override
	protected void handleSizeChange() {
		super.handleSizeChange();
		texelWidth = 1.0f / (float)getWidth();
		texelHeight = 1.0f / (float)getHeight();
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		texelWidthHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELWIDTH);
		texelHeightHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELHEIGHT);
	}

	@Override
	protected void passShaderValues() {
		if(getCurrentPass() == 1) {
			texelWidth = 1.0f / (float)getWidth();
			texelHeight = 0f;
		} else {
			texelWidth = 0f;
			texelHeight = 1.0f / (float)getHeight();
		}
		super.passShaderValues();
		GLES20.glUniform1f(texelWidthHandle, texelWidth);
		GLES20.glUniform1f(texelHeightHandle, texelHeight);
	}
}
