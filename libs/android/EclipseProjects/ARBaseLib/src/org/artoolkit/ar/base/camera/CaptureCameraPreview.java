/*
 *  CaptureCameraPreview.java
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

package org.artoolkit.ar.base.camera;

import java.io.IOException;
import java.lang.RuntimeException;

import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.R;
//import java.util.List;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class CaptureCameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

	/**
	 * Android logging tag for this class.
	 */
	private static final String TAG = "CameraPreview";
	
	/**
	 * The Camera doing the capturing.
	 */
    private Camera camera = null;
    private CameraWrapper cameraWrapper = null;

    /**
     * The camera capture width in pixels.
     */
    private int captureWidth;
    
    /**
     * The camera capture height in pixels.
     */
    private int captureHeight;
    
    /**
     * The camera capture rate in frames per second.
     */
    private int captureRate;
    
    /**
     * Counter to monitor the actual rate at which frames are captured from the camera.
     */
    private FPSCounter fpsCounter = new FPSCounter();
    
    /**
     * Listener to inform of camera related events: start, frame, and stop.
     */
    private CameraEventListener listener;
    
    /**
     * Constructor takes a {@link CameraEventListener} which will be called on 
     * to handle camera related events.
     * @param cel CameraEventListener to use. Can be null.
     */
    @SuppressWarnings("deprecation")
	public CaptureCameraPreview(Context context, CameraEventListener cel) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);      
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Deprecated in API level 11. Still required for API levels <= 10.

        setCameraEventListener(cel);
        
    }
    
    /**
     * Sets the {@link CameraEventListener} which will be called on to handle camera 
     * related events.
     * @param cel CameraEventListener to use. Can be null.
     */
    public void setCameraEventListener(CameraEventListener cel) {
    	 listener = cel;    
    }

    
    @SuppressLint("NewApi")
	@Override
    public void surfaceCreated(SurfaceHolder holder) {
        
		int cameraIndex = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("pref_cameraIndex", "0"));
    	Log.i(TAG, "Opening camera " + (cameraIndex + 1));
    	try {
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) camera = Camera.open(cameraIndex);
    		else camera = Camera.open();
            
    	} catch (RuntimeException exception) {
    		Log.e(TAG, "Cannot open camera. It may be in use by another process.");
    		return;
    	}
    	
        Log.i(TAG, "Camera open");
            
        try {

        	camera.setPreviewDisplay(holder);
               
        } catch (IOException exception) {
            Log.e(TAG, "IOException setting display holder");
            camera.release();
            camera = null;   
            Log.i(TAG, "Released camera");
            return;
    	}
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        
    	if (camera != null) {
    	
    		camera.setPreviewCallback(null);    	
    		camera.stopPreview();
    	
    		camera.release();
    		camera = null;
    	}
    	
    	if (listener != null) listener.cameraPreviewStopped();
    	
    }

  
    @SuppressWarnings("deprecation") // setPreviewFrameRate, getPreviewFrameRate
	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	
    	if (camera == null) {    		
    		// Camera wasn't opened successfully?
    		Log.e(TAG, "No camera in surfaceChanged");
    		return;    		
    	}
    	
    	Log.i(TAG, "Surfaced changed, setting up camera and starting preview");
    	
        String camResolution = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("pref_cameraResolution", getResources().getString(R.string.pref_defaultValue_cameraResolution));
        String[] dims = camResolution.split("x", 2);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]));
        parameters.setPreviewFrameRate(30);
        camera.setParameters(parameters);

        parameters = camera.getParameters();
        captureWidth = parameters.getPreviewSize().width;
        captureHeight = parameters.getPreviewSize().height;
        captureRate = parameters.getPreviewFrameRate();
        int pixelformat = parameters.getPreviewFormat(); // android.graphics.imageformat
        PixelFormat pixelinfo = new PixelFormat();
        PixelFormat.getPixelFormatInfo(pixelformat, pixelinfo);
        int cameraIndex = 0;
        boolean cameraIsFrontFacing = false;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraIndex = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("pref_cameraIndex", "0"));
			Camera.getCameraInfo(cameraIndex, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) cameraIsFrontFacing = true;
		}
        
		int bufSize = captureWidth * captureHeight * pixelinfo.bitsPerPixel / 8; // For the default NV21 format, bitsPerPixel = 12.
		Log.i(TAG, "Camera buffers will be " + captureWidth + "x" + captureHeight + "@" + pixelinfo.bitsPerPixel + "bpp, " + bufSize + "bytes.");
		cameraWrapper = new CameraWrapper(camera);       
        cameraWrapper.configureCallback(this, true, 10, bufSize); // For the default NV21 format, bitsPerPixel = 12.
        
        camera.startPreview();
        
        if (listener != null) listener.cameraPreviewStarted(captureWidth, captureHeight, captureRate, cameraIndex, cameraIsFrontFacing);

    }

    @Override
	public void onPreviewFrame (byte[] data, Camera camera) {
		
		if (listener != null) listener.cameraPreviewFrame(data);
		
		cameraWrapper.frameReceived(data);
		
		
		if (fpsCounter.frame()) {
			Log.i(TAG, "Camera capture FPS: " + fpsCounter.getFPS());			
		}

    	
	}
 
}
