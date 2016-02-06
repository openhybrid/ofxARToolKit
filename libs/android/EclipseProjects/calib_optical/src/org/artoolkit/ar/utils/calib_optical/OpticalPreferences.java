/*
 *  OpticalPreferences.java
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
 *  Author(s): Philip Lamb
 *
 */

package org.artoolkit.ar.utils.calib_optical;

import java.util.List;
import org.artoolkit.ar.utils.calib_optical.R;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class OpticalPreferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String TAG = "OpticalPreferences";

	//private CheckBoxPreference forceLandscape;
	private CheckBoxPreference stereoDisplay;
	private ListPreference stereoDisplayMode;
	//
	private ListPreference eyeSelection;
	//
	private int cameraCount;
	private ListPreference cameraIndexPreference;
	private ListPreference cameraResolutionPreference;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Ensure we have a camera!
        PackageManager pm = this.getPackageManager();
    	if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
    		finish();
    		return;
    	}
        
        addPreferencesFromResource(R.xml.preferences);
        //forceLandscape = (CheckBoxPreference)findPreference("pref_forceLandscape");
        stereoDisplay = (CheckBoxPreference)findPreference("pref_stereoDisplay");
        stereoDisplayMode = (ListPreference)findPreference("pref_stereoDisplayMode");
        //
        eyeSelection = (ListPreference)findPreference("pref_eyeSelection");
        //
        cameraIndexPreference = (ListPreference)findPreference("pref_cameraIndex");
        cameraResolutionPreference = (ListPreference)findPreference("pref_cameraResolution");
        
		// One time only, we need to build a list of cameras. If more than
	    // one camera, enable the cameraIndexPreference. Otherwise, disable it.
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	        cameraCount = Camera.getNumberOfCameras();
	        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	        int cameraCountFront = 0;
	        int cameraCountRear = 0;
	        CharSequence[] entries = new CharSequence[cameraCount];
	        CharSequence[] entryValues = new CharSequence[cameraCount];
	    	for (int camIndex = 0; camIndex < cameraCount; camIndex++) {
	    		Camera.getCameraInfo(camIndex, cameraInfo);
	    		if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	    			cameraCountFront++;
	    			entries[camIndex] = "Front camera";
	    			if (cameraCountFront > 1) entries[camIndex] = entries[camIndex] + " " + cameraCountFront;
	    		} else {
	    			cameraCountRear++;
	    			entries[camIndex] = "Rear camera";
	    			if (cameraCountRear > 1) entries[camIndex] = entries[camIndex] + " " + cameraCountRear;
	    		}
    			entryValues[camIndex] = Integer.toString(camIndex);
	    	}
	    	cameraIndexPreference.setEnabled(true);
	    	cameraIndexPreference.setEntries(entries);
	    	cameraIndexPreference.setEntryValues(entryValues);
	    } else {
	    	cameraCount = 1;
	    	cameraIndexPreference.setEnabled(false);
	    }
	    
	    buildResolutionListForCameraIndex();
    }

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void buildResolutionListForCameraIndex() {
		int camIndex = Integer.parseInt(cameraIndexPreference.getValue());

		Camera cam = null;
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
				cam = Camera.open(camIndex);
			else
				cam = Camera.open();

			Camera.Parameters params = cam.getParameters();
			List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
			cam.release();

			// Build list of resolutions, checking whether the current
			// resolution is in the list too.
			// String camResolution =
			// sharedPreferences.getString("pref_cameraResolution",
			// getResources().getString(R.string.pref_defaultValue_cameraResolution);
			String camResolution = cameraResolutionPreference.getValue();
			boolean foundCurrentResolution = false;
			CharSequence[] entries = new CharSequence[previewSizes.size()];
			CharSequence[] entryValues = new CharSequence[previewSizes.size()];
			for (int i = 0; i < previewSizes.size(); i++) {
				int w = previewSizes.get(i).width;
				int h = previewSizes.get(i).height;
				entries[i] = w + "x" + h + "   (" + findAspectRatioName(w, h)
						+ ")";
				entryValues[i] = w + "x" + h;
				if (entryValues[i].equals(camResolution))
					foundCurrentResolution = true;
			}
			cameraResolutionPreference.setEntries(entries);
			cameraResolutionPreference.setEntryValues(entryValues);

			if (!foundCurrentResolution) {
				cameraResolutionPreference.setValue(entryValues[0].toString());
				cameraResolutionPreference
						.setSummary(cameraResolutionPreference.getEntry());
			}

		} catch (RuntimeException e) {
			Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
		}
	}
	
	private void adjustEyeSelectionNames() {
		CharSequence[] entriesMono = {"Left eye", "Right eye"};
		CharSequence[] entriesStereo = {"Left eye then right", "Right eye then left"};
		
		eyeSelection.setEntries(stereoDisplay.isChecked() ? entriesStereo : entriesMono);
		eyeSelection.setSummary(eyeSelection.getEntry());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// ListPreference preferences need their summary manually set.
		cameraIndexPreference.setSummary(cameraIndexPreference.getEntry());
		cameraResolutionPreference.setSummary(cameraResolutionPreference
				.getEntry());
		stereoDisplayMode.setSummary(stereoDisplayMode
				.getEntry());
		eyeSelection.setSummary(eyeSelection.getEntry());

		// Register for changes.
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("pref_cameraIndex")) {
			cameraIndexPreference.setSummary(cameraIndexPreference.getEntry());
			buildResolutionListForCameraIndex();
		} else if (key.equals("pref_cameraResolution")) {
			cameraResolutionPreference.setSummary(cameraResolutionPreference.getEntry());
		} else if (key.equals("pref_stereoDisplayMode")) {
			stereoDisplayMode.setSummary(stereoDisplayMode.getEntry());
		} else if (key.equals("pref_stereoDisplay")) {
			adjustEyeSelectionNames();
		} else if (key.equals("pref_eyeSelection")) {
			eyeSelection.setSummary(eyeSelection.getEntry());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public enum ASPECT_RATIO {
		_1_1, // 1.0
		_11_9, // 1.222
		_5_4, // 1.25
		_4_3, // 1.333
		_SQRROOT2_1, // 1.414
		_3_2, // 1.5
		_14_9, // 1.556
		_8_5, // 1.6
		_5_3, // 1.667
		_16_9, // 1.778
		_9_5, // 1.8
		_17_9, // 1.889
		_UNIQUE
	}

	private static final class PixelSizeToAspectRatio {
		int width;
		int height;
		ASPECT_RATIO aspectRatio;
		String name;

		PixelSizeToAspectRatio(int w, int h, ASPECT_RATIO ar, String name) {
			this.width = w;
			this.height = h;
			this.aspectRatio = ar;
			this.name = name;
		}
	}

	private static final PixelSizeToAspectRatio aspectRatios[] = new PixelSizeToAspectRatio[] {
			new PixelSizeToAspectRatio(1, 1, ASPECT_RATIO._1_1, "1:1"), // 1.0:
			new PixelSizeToAspectRatio(11, 9, ASPECT_RATIO._11_9, "11:9"), // 1.222:
																			// 176x144
																			// (QCIF),
																			// 352x288
																			// (CIF)
			new PixelSizeToAspectRatio(5, 4, ASPECT_RATIO._5_4, "5:4"), // 1.25:
																		// 1280x1024
																		// (SXGA),
																		// 2560x2048
			new PixelSizeToAspectRatio(4, 3, ASPECT_RATIO._4_3, "4:3"), // 1.333:
																		// 320x240
																		// (QVGA),
																		// 480x360,
																		// 640x480
																		// (VGA),
																		// 768x576
																		// (576p),
																		// 800x600
																		// (SVGA),
																		// 960x720,
																		// 1024x768
																		// (XGA),
																		// 1152x864,
																		// 1280x960,
																		// 1400x1050,
																		// 1600x1200,
																		// 2048x1536
			new PixelSizeToAspectRatio(3, 2, ASPECT_RATIO._3_2, "3:2"), // 1.5:
																		// 240x160,
																		// 480x320,
																		// 960x640,
																		// 720x480
																		// (480p),
																		// 1152x768,
																		// 1280x854,
																		// 1440x960
			new PixelSizeToAspectRatio(14, 9, ASPECT_RATIO._14_9, "14:9"), // 1.556:
			new PixelSizeToAspectRatio(8, 5, ASPECT_RATIO._8_5, "8:5"), // 1.6:
																		// 320x200,
																		// 1280x800,
																		// 1440x900,
																		// 1680x1050,
																		// 1920x1200,
																		// 2560x1600
			new PixelSizeToAspectRatio(5, 3, ASPECT_RATIO._5_3, "5:3"), // 1.667:
																		// 800x480,
																		// 1280x768,
																		// 1600x960
			new PixelSizeToAspectRatio(16, 9, ASPECT_RATIO._16_9, "16:9"), // 1.778:
																			// 1280x720
																			// (720p),
																			// 1920x1080
																			// (1080p)
			new PixelSizeToAspectRatio(9, 5, ASPECT_RATIO._9_5, "9:5"), // 1.8:
																		// 864x480
			new PixelSizeToAspectRatio(17, 9, ASPECT_RATIO._17_9, "17:9"), // 1.889:
																			// 2040x1080

			// Some values that are close to standard ratios.
			new PixelSizeToAspectRatio(683, 384, ASPECT_RATIO._16_9, "16:9"), // ~1.778:
																				// 1366x768
			new PixelSizeToAspectRatio(85, 48, ASPECT_RATIO._16_9, "16:9"), // ~1.778:
																			// 1360x768
			new PixelSizeToAspectRatio(256, 135, ASPECT_RATIO._17_9, "17:9"), // ~1.889:
																				// 2048x1080
																				// (2K)
			new PixelSizeToAspectRatio(512, 307, ASPECT_RATIO._5_3, "5:3"), // ~1.667:
																			// 1024x614
			new PixelSizeToAspectRatio(30, 23, ASPECT_RATIO._4_3, "4:3"), // ~1.333:
																			// 480x368
			new PixelSizeToAspectRatio(128, 69, ASPECT_RATIO._17_9, "17:9"), // ~1.889:
																				// 1024x552
			new PixelSizeToAspectRatio(30, 23, ASPECT_RATIO._11_9, "11:9"), // ~1.222:
																			// 592x480
	};

	/**
	 * A convenience method which makes it easy to determine the approximate
	 * aspect ratio of an image with the given resolution (expressed in pixel
	 * width and height). Returns a symbolic constant for the aspect ratio,
	 * which makes it easy to determine whether two different resolutions have
	 * the same aspect ratio.
	 * 
	 * @param w
	 *            Width in pixels
	 * @param h
	 *            Height in pixels
	 * @return If a matching commonly-used aspect ratio can be found, returns
	 *         symbolic constant for that aspect ratio.
	 */
	public ASPECT_RATIO findAspectRatio(int w, int h) {

		// Reduce.
		int primes[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
				47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 };
		int w_lcd = w, h_lcd = h;
		for (int i : primes) {
			while (w_lcd >= i && h_lcd >= i && w_lcd % i == 0 && h_lcd % i == 0) {
				w_lcd /= i;
				h_lcd /= i;
			}
		}

		// Find.
		for (PixelSizeToAspectRatio aspectRatio : aspectRatios) {
			if (w_lcd == aspectRatio.width && h_lcd == aspectRatio.height)
				return aspectRatio.aspectRatio;
		}
		return (ASPECT_RATIO._UNIQUE);
	}

	/**
	 * A convenience method which makes it easy to determine the approximate
	 * aspect ratio of an image with the given resolution (expressed in pixel
	 * width and height).
	 * 
	 * @param w
	 *            Width in pixels
	 * @param h
	 *            Height in pixels
	 * @return If a matching commonly-used aspect ratio can be found, returns
	 *         string name for that aspect ratio.
	 */
	public String findAspectRatioName(int w, int h) {

		// Reduce.
		int primes[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
				47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 };
		int w_lcd = w, h_lcd = h;
		for (int i : primes) {
			while (w_lcd >= i && h_lcd >= i && w_lcd % i == 0 && h_lcd % i == 0) {
				w_lcd /= i;
				h_lcd /= i;
			}
		}

		// Find.
		for (PixelSizeToAspectRatio aspectRatio : aspectRatios) {
			if (w_lcd == aspectRatio.width && h_lcd == aspectRatio.height)
				return aspectRatio.name;
		}
		return (w + ":" + h);
	}
}