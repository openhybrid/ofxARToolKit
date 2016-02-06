Read me for ARToolKit for Android.
==================================


Contents.
---------

About this archive.
Requirements.
Getting started.
Training new markers.
Release notes.
libKPM usage.
Next steps.


About this archive.
-------------------

This archive contains the ARToolKit libraries, utilities and examples for Android, version 5.3.1.

ARToolKit version 5.3.1 is released under the GNU Lesser General Public License version 3, with some additional permissions. Example code is generally released under a more permissive disclaimer; please read the file LICENSE.txt for more information.

If you intend to publish your app on Google's Play Store or any other commercial marketplace, you must use your own package name, and not the org.artoolkit package name.

ARToolKit is designed to build on Windows, Macintosh OS X, Linux, iOS and Android platforms.

This archive was assembled by:
    Philip Lamb
    http://www.artoolkit.org
    2015-10-05


Requirements.
-------------

Requirements:
 * Android SDK Tools r12 (for Android 2.2/API 8) or later required, r24.3.3 (June 2015) or later recommended.
 * Development of native ARToolKit for Android applications or requires Android NDK Revision 10 (July 2014) or later, revision 10e (May 2015) recommended.
 * Use of the Eclipse IDE is recommended, and Eclipse project files are supplied with the ARToolKit for Android SDK.
 * An Android device, running Android 2.2 or later. Testing is not possible using the Android Virtual Device system.
 * A printer to print the pattern PDF images.

ARToolKit is supplied as pre-built binaries for each platform, plus full source code for the SDK libraries, utilities, and examples, and documentation. If you wish to view the source for the desktop-only utilities, you will also need to use this Android release alongside ARToolKit v5.x for Mac OS X, Windows, or Linux.


Getting started.
----------------

For full instructions on using this SDK, please refer to the online user guide at http://www.artoolkit.org/documentation/#Android.


Training new markers.
---------------------

The utilities required to train new square and NFT markers are provide in the "bin" directory of the SDK. The utilities are command-line Windows/OS X executables which should be run from a Terminal environment.

Consult the ARToolKit documentation for more information:
    http://www.artoolkit.org/documentation/Creating_and_training_new_ARToolKit_markers
    http://www.artoolkit.org/documentation/ARToolKit_NFT

Usage (OS X):
    ./mk_patt
	./genTexData somejpegfile.jpg
	./dispImageSet somejpegfile.jpg
	./dispFeatureSet somejpegfile.jpg
	./checkResolution Data/camera_para.dat (change camera_para.dat to the correct camera calibration file for your device, camera and resolution).

Usage (Windows):
    mk_patt.exe
	genTexData.exe somejpegfile.jpg
	dispImageSet.exe somejpegfile.jpg
	dispFeatureSet.exe somejpegfile.jpg
	checkResolution.exe Data/camera_para.dat (change camera_para.dat to the correct camera calibration file for your device, camera and resolution).


Release notes.
--------------
This release contains ARToolKit v5.3.1 for Android.

This will be the last release to support Android OS versions 2.2 through 3.x. Devices running Android OS represent less than 5% of the total active base of Android devices. Future ARToolKit releases will support Android OS version 4.0 ("Ice Cream Sandwich") and later.

This will also be the last release to support development using Eclipse and ADT. As Android Studio now supports NDK-based projects, ARToolKit will move to providing Android Studio projects only in the next release. We think that this change will please many more developers than it displeases.

The major change in ARToolKit v5.3 was a new version of libKPM based on the FREAK detector framework, contributed by DAQRI. See "libKPM usage" below.

Please see the ChangeLog.txt for details of changes in this and earlier releases.

ARToolKit v5.2 was the first major release under an open source license in several years, and represented several years of commercial development of ARToolKit by ARToolworks during this time. It was a significant change to previous users of ARToolKit v2.x. Please see http://www.artoolkit.org/documentation/ARToolKit_feature_comparison for more information.

For users of ARToolKit Professional versions 4.0 through 5.1.7, ARToolKit v5.2 and later include a number of changes. Significantly, full source is now provided for the NFT libraries libAR2 and libKPM.

ARToolKit 5.2 and later for Android supports fetching of camera calibration data from an ARToolworks-provided server. On request, ARToolworks provides developers access to an on-device verson of calib_camera which submits calibration data to ARToolworks' server, making it available to all users of that device. The underlying changes to support this include a native version of libARvideo for Android which provides the functions to fetch camera calibration data (note that libARvideo on Android does not handle frame retrieval from the camera; that still happens on the Java side). Existing native apps which wish to use the functionality should follow the example usage from the ARNative example, and also link against libarvideo, and its additional dependencies libcurl, libssl and libcrypto.

Please see http://www.artoolkit.org/documentation/Using_automatic_online_camera_calibration_retrieval for details on this feature and the code changes required.


libKPM usage.
-------------

libKPM, which performs key-point matching for NFT page recognition and initialization now use a FREAK detector framework, contributed by DAQRI. Unlike the previous commercial version of libKPM which used SURF features, FREAK is not encumbered by patents. libKPM now joins the other core ARToolKit libraries under an LGPLv3 license. Additionally the new libKPM no longer has dependencies on OpenCV’s FLANN library, which should simply app builds and app distribution on all supported platforms.

Existing holders of a commercial license to ARToolKit Professional v5.x may use libKPM from ARToolKit v5.2 under the terms of their current license for the remainder of its duration. Please contact us via http://www.artoolkit.org/contact if you are an existing user of ARToolKit Professional with questions.


Next steps.
-----------

We have made a forum for discussion of ARToolKit for Android development available on our community website.

http://www.artoolkit.org/community/forums/viewforum.php?f=26

You are invited to join the forum and contribute your questions, answers and success stories.

ARToolKit consists of a full ecosystem of SDKs for desktop, web, mobile and in-app plugin augmented reality. Stay up to date with information and releases from artoolkit.org by joining our announcements mailing list.

http://www.artoolkit.org/community/lists/


Do you have a feature request, bug report, or other code you would like to contribute to ARToolKit? Access the complete source and issue tracker for ARToolKit at http://github.com/artoolkit/artoolkit5

--
EOF
