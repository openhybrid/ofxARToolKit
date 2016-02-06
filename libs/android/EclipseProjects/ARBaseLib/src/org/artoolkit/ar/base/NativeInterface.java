/*
 *  NativeInterface.java
 *  ARToolKit5
 *
 *  This file is part of ARToolKit.
 *
 *  ARToolKit is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ARToolKit is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ARToolKit.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  As a special exception, the copyright holders of this library give you
 *  permission to link this library with independent modules to produce an
 *  executable, regardless of the license terms of these independent modules, and to
 *  copy and distribute the resulting executable under terms of your choice,
 *  provided that you also meet, for each linked independent module, the terms and
 *  conditions of the license of that module. An independent module is a module
 *  which is neither derived from nor based on this library. If you modify this
 *  library, you may extend this exception to your version of the library, but you
 *  are not obligated to do so. If you do not wish to do so, delete this exception
 *  statement from your version.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.base;

import android.util.Log;

/**
 * The NativeInterface class contains the JNI function signatures for 
 * native ARToolKit functions. These functions should be accessed via 
 * the {@link ARToolKit} class rather than called directly.
 */
public class NativeInterface {

	/**
	 * Android logging tag for this class.
	 */
	private static final String TAG = "NativeInterface";
	
	/**
	 * The name of the native ARToolKit library.
	 */
	private static final String LIBRARY_NAME = "ARWrapper";

	/**
	 * Attempts to load the native library so that native functions can be called.
	 * @return true if the library was successfully loaded, otherwise false.
	 */
    public static boolean loadNativeLibrary() {
    	
    	try {
    		
    		Log.i(TAG, "Attempting to load library: " + LIBRARY_NAME);
    		
    		System.loadLibrary("c++_shared");

       		System.loadLibrary(LIBRARY_NAME);

    	} catch (Exception e) { 
    		Log.e(TAG, "Exception loading native library: " + e.toString());
    		return false;
    	}
    
    	return true;
    }
    
    /**
     * Gets the version of the underlying ARToolKit library.
     * @return 				ARToolKit version
     */
    public static native String arwGetARToolKitVersion();
    
    /**
     * Initialises the basic ARToolKit functions. After this function has 
     * been successfully called, markers can be added and removed, but marker 
     * detection is not yet running.
     * @return 				true on success, false if an error occurred
     */
    public static native boolean arwInitialiseAR();
    
    /**
     * Initialises the the basic ARToolKit functions with non-default options
     * for size and number of square markers. After this function has 
     * been successfully called, markers can be added and removed, but marker 
     * detection is not yet running.
     * @param pattSize For any square template (pattern) markers, the number of rows
     *     and columns in the template. May not be less than 16 or more than AR_PATT_SIZE1_MAX.
     *     
     *      Pass AR_PATT_SIZE1 for the same behaviour as arwInitialiseAR().
     * @param pattCountMax For any square template (pattern) markers, the maximum number
     *     of markers that may be loaded for a single matching pass. Must be > 0.
     *     
     *      Pass AR_PATT_NUM_MAX for the same behaviour as arwInitialiseAR().
     * @return			true if successful, false if an error occurred
     * @see				arwShutdownAR()
     */
    public static native boolean arwInitialiseARWithOptions(int pattSize, int pattCountMax);

	/**
	 * Changes the working directory to the resources directory used by ARToolKit.
     * Normally, this would be called immediately after arwInitialiseAR()
	 * @return			true if successful, false if an error occurred
	 * @see				arwInitialiseAR()
	 */
    public static native boolean arwChangeToResourcesDir(String resourcesDirectoryPath);
    
    /**
     * Initialises video capture. The native library will start to expect video
     * frames.
     * @param vconf			The video configuration string. Can be left empty.
     * @param cparaName		The camera parameter file to load.
     * @param nearPlane		The value to use for the near OpenGL clipping plane.
     * @param farPlane		The value to use for the far OpenGL clipping plane.
     * @return				true on success, false if an error occurred.
     */
    public static native boolean arwStartRunning(String vconf, String cparaName, float nearPlane, float farPlane);
    
    /**
     * Initialises stereo video capture. The native library will start to expect video
     * frames.
     * @param vconfL		The video configuration string for the left camera. Can be left empty.
     * @param cparaNameL	The camera parameter file to load for the left camera.
     * @param vconfR		The video configuration string for the right camera. Can be left empty.
     * @param cparaNameR	The camera parameter file to load for the right camera.
     * @param transL2RName	The stereo calibration file to load.
     * @param nearPlane		The value to use for the near OpenGL clipping plane.
     * @param farPlane		The value to use for the far OpenGL clipping plane.
     * @return				true on success, false if an error occurred
     */
    public static native boolean arwStartRunningStereo(String vconfL, String cparaNameL, String vconfR, String cparaNameR, String transL2RName, float nearPlane, float farPlane);
    
    /**
     * Queries whether marker detection is up and running. This will be true 
     * after a call to arwStartRunning, and frames are being sent through. At 
     * this point, marker visibility and transformations can be queried.
     * @return				true if marker detection is running, false if not
     */
    public static native boolean arwIsRunning();
    
    /**
     * Stops marker detection and closes the video source.
     * @return				true on success, false if an error occurred
     */
    public static native boolean arwStopRunning();
    
    /**
     * Shuts down the basic ARToolKit functions.
     * @return				true on success, false if an error occurred
     */
    public static native boolean arwShutdownAR();
    
    /**
     * Retrieves the ARToolKit projection matrix.
     * @return				A float array containing the OpenGL compatible projection matrix, or null if an error occurred.
     */
    public static native float[] arwGetProjectionMatrix();
    
    /**
     * Retrieves the ARToolKit projection matrix for the right camera of a stereo camera pair.
     * @return				A float array containing the OpenGL compatible projection matrix, or null if an error occurred.
     */
    public static native boolean arwGetProjectionMatrixStereo(float[] projL, float[] projR);
    
	/**
	 * Returns the parameters of the video source frame.
	 * 
	 * Usage example:
	 * int[] width = new int[1];
	 * int[] height = new int[1];
     * int[] pixelSize = new int[1];
     * String[] pixelFormatString = new String[1];
     * boolean ok = NativeInterface.arwGetVideoParams(width, height, pixelSize, pixelFormatString);
     *
     * @width An int array, the first element of which will be filled with the width (in pixels) of the video frame, or null if this information is not required.
     * @height An int array, the first element of which will be filled with the height (in pixels) of the video frame, or null if this information is not required.
     * @pixelSize An int array, the first element of which will be filled with the numbers of bytes per pixel of the source frame, or null if this information is not required.
     * @pixelFormatString A String array, the first element of which will be filled with the symbolic name of the pixel format of the video frame, or null if this information is not required. The name will be of the form "AR_PIXEL_FORMAT_xxx".
	 * @return			True if the values were returned OK, false if there is currently no video source or an error int[] .
	 * @see				arwGetVideoParamsStereo
	 */
    public static native boolean arwGetVideoParams(int[] width, int[] height, int[] pixelSize, String[] pixelFormatStringBuffer);
    
	/**
	 * Returns the parameters of the video source frames.
	 * 
	 * Usage example:
	 * int[] widthL = new int[1];
	 * int[] heightL = new int[1];
     * int[] pixelSizeL = new int[1];
     * String[] pixelFormatStringL = new String[1];
	 * int[] widthR = new int[1];
	 * int[] heightR = new int[1];
     * int[] pixelSizeR = new int[1];
     * String[] pixelFormatStringR = new String[1];
     * boolean ok = NativeInterface.arwGetVideoParams(widthL, heightL, pixelSizeL, pixelFormatStringL, widthR, heightR, pixelSizeR, pixelFormatStringR);
	 *
     * @widthL An int array, the first element of which will be filled with the width (in pixels) of the video frame, or null if this information is not required.
     * @widthR An int array, the first element of which will be filled with the width (in pixels) of the video frame, or null if this information is not required.
     * @heightL An int array, the first element of which will be filled with the height (in pixels) of the video frame, or null if this information is not required.
     * @heightR An int array, the first element of which will be filled with the height (in pixels) of the video frame, or null if this information is not required.
     * @pixelSizeL An int array, the first element of which will be filled with the numbers of bytes per pixel of the source frame, or null if this information is not required.
     * @pixelSizeR An int array, the first element of which will be filled with the numbers of bytes per pixel of the source frame, or null if this information is not required.
     * @pixelFormatStringL A String array, the first element of which will be filled with the symbolic name of the pixel format of the video frame, or null if this information is not required. The name will be of the form "AR_PIXEL_FORMAT_xxx".
     * @pixelFormatStringR A String array, the first element of which will be filled with the symbolic name of the pixel format of the video frame, or null if this information is not required. The name will be of the form "AR_PIXEL_FORMAT_xxx".
	 * @return			True if the values were returned OK, false if there is currently no stereo video source or an error occurred.
	 * @see				arwGetVideoParams
	 */
	public static native boolean arwGetVideoParamsStereo(int[] widthL, int[] heightL, int[] pixelSizeL, String[] pixelFormatStringL, int[] widthR, int[] heightR, int[] pixelSizeR, String[] pixelFormatString);

	/**
	 * Checks if a new video frame is available.
	 * @return				true if a new frame is available.
	 */
    public static native boolean arwCapture();
	
	/**
	 * Performs an update, runs marker detection if in the running state.
	 * @return				true if no error occurred, otherwise false
	 */
    public static native boolean arwUpdateAR();

    /**
	 * Adds a marker to be detected.
	 * @param cfg			Marker configuration string
	 * @return				A unique identifier (UID) of the new marker, or -1 if the marker was not added due to an error.
	 */
    public static native int arwAddMarker(String cfg);
	
	/**
	 * Removes the specified marker.
	 * @param markerUID		The unique identifier (UID) of the marker to remove
	 * @return				true if the marker was removed, otherwise false
	 */
    public static native boolean arwRemoveMarker(int markerUID);
	
	/**
	 * Removes all loaded markers.
	 * @return				The number of markers removed
	 */
    public static native int arwRemoveAllMarkers();
	
	/**
	 * Queries whether the specified marker is currently visible.
	 * @param markerUID		The unique identifier (UID) of the marker to check
	 * @return				true if the marker is currently visible, otherwise false
	 */
    public static native boolean arwQueryMarkerVisibility(int markerUID);
	
	/**
	 * Retrieves the transformation matrix for the specified marker
	 * @param markerUID		The unique identifier (UID) of the marker to check
	 * @return				A float array containing the OpenGL compatible transformation matrix, or null if the marker isn't visible or an error occurred.
	 */
    public static native float[] arwQueryMarkerTransformation(int markerUID);
	
	/**
	 * Retrieves the transformation matrix for the specified marker
	 * @param markerUID		The unique identifier (UID) of the marker to check
	 * @param matrixL		A float array containing the OpenGL compatible transformation matrix for the left camera.
	 * @param matrixR		A float array containing the OpenGL compatible transformation matrix for the right camera.
	 * @return				true if the marker is currently visible, otherwise false.
	 */
    public static native boolean arwQueryMarkerTransformationStereo(int markerUID, float[] matrixL, float[] matrixR);
	
    public static final int ARW_MARKER_OPTION_FILTERED = 1,
    						ARW_MARKER_OPTION_FILTER_SAMPLE_RATE = 2,
    						ARW_MARKER_OPTION_FILTER_CUTOFF_FREQ = 3,
    						ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION = 4,
    						ARW_MARKER_OPTION_SQUARE_CONFIDENCE = 5,
    						ARW_MARKER_OPTION_SQUARE_CONFIDENCE_CUTOFF = 6;
    public static native void arwSetMarkerOptionBool(int markerUID, int option, boolean value);
    public static native void arwSetMarkerOptionInt(int markerUID, int option, int value);
    public static native void arwSetMarkerOptionFloat(int markerUID, int option, float value);
    public static native boolean arwGetMarkerOptionBool(int markerUID, int option);
    public static native int arwGetMarkerOptionInt(int markerUID, int option);
    public static native float arwGetMarkerOptionFloat(int markerUID, int option);
    
	/**
	 * Sets whether to enable or disable debug mode.
	 * @param debug			true to enable, false to disable
	 */
    public static native void arwSetVideoDebugMode(boolean debug);
	
	/**
	 * Returns whether debug mode is enabled.
	 * @return				true if enabled, otherwise false
	 */
    public static native boolean arwGetVideoDebugMode();
	
	/**
	 * Sets the threshold value used during video image binarization.
	 * @param threshold		The new threshold value
	 */
    public static native void arwSetVideoThreshold(int threshold);
	
	/**
	 * Returns the current threshold value used during video image binarization.
	 * @return				The current threshold value
	 */
    public static native int arwGetVideoThreshold();    
    
    public static final int AR_LABELING_THRESH_MODE_MANUAL = 0,
    	    				AR_LABELING_THRESH_MODE_AUTO_MEDIAN = 1,
    	    				AR_LABELING_THRESH_MODE_AUTO_OTSU = 2,
    	    				AR_LABELING_THRESH_MODE_AUTO_ADAPTIVE = 3,
							AR_LABELING_THRESH_MODE_AUTO_BRACKETING = 4;

	/**
	 * Sets the threshold mode used during video image binarization.
	 * @param threshold		The new threshold mode.
	 */
    public static native void arwSetVideoThresholdMode(int mode);
	
	/**
	 * Returns the current threshold mode used during video image binarization.
	 * @return				The current threshold mode.
	 */
    public static native int arwGetVideoThresholdMode();    
    
	/**
	 * Passes a video frame to the native library for processing.
	 * @param image			Buffer containing the video frame
	 * @param width			Width of the video frame in pixels
	 * @param height		Height of the video frame in pixels
	 * @param cameraIndex	Zero-based index of the camera in use. If only one camera is present, will be 0.
	 * @param cameraIsFrontFacing false if camera is rear-facing (the default) or true if camera is facing toward the user.
	 * @return				true if no error occurred, otherwise false
	 */
    public static native boolean arwAcceptVideoImage(byte[] image, int width, int height, int cameraIndex, boolean cameraIsFrontFacing);
    
	/**
	 * Passes a video frame for the right camera of a stereo camera pair to the native library for processing.
	 * @param imageL		Buffer containing the video frame
	 * @param widthL		Width of the video frame in pixels
	 * @param heightL		Height of the video frame in pixels
	 * @param cameraIndexL	Zero-based index of the camera in use. If only one camera is present, will be 0.
	 * @param cameraIsFrontFacingL false if camera is rear-facing (the default) or true if camera is facing toward the user.
	 * @param imageR		Buffer containing the video frame
	 * @param widthR		Width of the video frame in pixels
	 * @param heightR		Height of the video frame in pixels
	 * @param cameraIndexR	Zero-based index of the camera in use. If only one camera is present, will be 0.
	 * @param cameraIsFrontFacingR false if camera is rear-facing (the default) or true if camera is facing toward the user.
	 * @return				true if no error occurred, otherwise false
	 */
    public static native boolean arwAcceptVideoImageStereo(byte[] imageL, int widthL, int heightL, int cameraIndexL, boolean cameraIsFrontFacingL, byte[] imageR, int widthR, int heightR, int cameraIndexR, boolean cameraIsFrontFacingR);
    
    
    public static native boolean arwUpdateDebugTexture32(byte[] image);
  
  
	public static final int AR_LABELING_WHITE_REGION = 0,
    						AR_LABELING_BLACK_REGION = 1;

	public static native void arwSetLabelingMode(int mode);
  	
	public static native int arwGetLabelingMode();
	
	
	public static final int AR_TEMPLATE_MATCHING_COLOR               = 0,
    						AR_TEMPLATE_MATCHING_MONO                = 1,
    						AR_MATRIX_CODE_DETECTION                 = 2,
    						AR_TEMPLATE_MATCHING_COLOR_AND_MATRIX    = 3,
    						AR_TEMPLATE_MATCHING_MONO_AND_MATRIX     = 4;

	public static native void arwSetPatternDetectionMode(int mode);
	
	public static native int arwGetPatternDetectionMode();
	
	
	public static native void arwSetBorderSize(float size);
	
	public static native float arwGetBorderSize();
	
	
	public static final int AR_MATRIX_CODE_3x3 = 0x03,                                                  // Matrix code in range 0-63.
    						AR_MATRIX_CODE_3x3_PARITY65 = 0x103,                                        // Matrix code in range 0-31.
    						AR_MATRIX_CODE_3x3_HAMMING63 = 0x203,                                       // Matrix code in range 0-7.
    						AR_MATRIX_CODE_4x4 = 0x04,                                                  // Matrix code in range 0-8191.
    						AR_MATRIX_CODE_4x4_BCH_13_9_3 = 0x304,                                      // Matrix code in range 0-511.
    						AR_MATRIX_CODE_4x4_BCH_13_5_5 = 0x404,                                      // Matrix code in range 0-31.
    						AR_MATRIX_CODE_5x5_BCH_22_12_5 = 0x405,                                     // Matrix code in range 0-4095.
    						AR_MATRIX_CODE_5x5_BCH_22_7_7 = 0x505,                                      // Matrix code in range 0-127.
    						AR_MATRIX_CODE_5x5 = 0x05,                                                  // Matrix code in range 0-4194303.
    						AR_MATRIX_CODE_6x6 = 0x06,                                                  // Matrix code in range 0-8589934591.
    						AR_MATRIX_CODE_GLOBAL_ID = 0xb0e;

	public static native void arwSetMatrixCodeType(int type);
	
	public static native int arwGetMatrixCodeType();
	
	
	public static final int AR_IMAGE_PROC_FRAME_IMAGE = 0,
    						AR_IMAGE_PROC_FIELD_IMAGE = 1;

	public static native void arwSetImageProcMode(int mode);
	
    public static native int arwGetImageProcMode();

}
