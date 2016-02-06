# ofxARToolKit
This is a new attempt for an OF ARToolKit 5 Add-On

API Discussion:


void setOrientation(OFX_ORIENTATION)
void setMaxNumOfMarkers(int)
void addMarker(string)
void autoFocusOn()
void autoFocusOff()
void setup()

void update()
void drawBackground()
void addExtraMarker(string);


void resume()
void pause()
void stop()

int numOfMarkersFound()
marker getMarker(int)
  vector<ofMatrix4x4> marker.modelViewMatrix
  string marker.markerName

vector<ofMatrix4x4> getProjectionMatrix()

void exit()

