/*
 *  FPSCounter.java
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

/**
 * Utility class to measure performance in frames per second. This can be useful for camera 
 * capture or graphics rendering.
 */
public class FPSCounter {

	/**
	 * The number of frames that have occurred in the current time period.
	 */
	private int frameCount;
	
	/**
	 * When the current time period started.
	 */
	private long periodStart;
	
	/**
	 * The last calculated FPS value.
	 */
	private float currentFPS;
	
	public FPSCounter() {
		reset();
	}
	
	public void reset() {
		frameCount = 0;
		periodStart = 0;
		currentFPS = 0.0f;
	}
	
	/**
	 * Call this function during each frame. The count of elapsed frames will be 
	 * increased and if a second has elapsed since the last FPS calculation, a new 
	 * FPS value will be calculated. If this happens, then the function will return
	 * true.
	 * @return true if a new FPS value has been calculated.
	 */
	public boolean frame() {
		
		frameCount++;
		
		long time = System.currentTimeMillis();        	
		if (periodStart <= 0) periodStart = time;
		
		long elapsed = time - periodStart;
		
    	if (elapsed >= 1000) {
    	
    		currentFPS = (1000 * frameCount) / (float)elapsed;    		
    		frameCount = 0;
    		
    		periodStart = time;  
    		
    		return true;
    		
    	}    	
    	
    	return false;
		
	}
	
	/**
	 * Returns the last calculated FPS value.
	 * @return The last calculated FPS value.
	 */
	public float getFPS() {
		return currentFPS;
	}
	
	/**
	 * Provides a string representation of the current FPS value.
	 * @return A string representation of the current FPS value.
	 */
	@Override
	public String toString() {
		return "FPS: " + currentFPS;
	}
	
}
