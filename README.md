# CS2920 - A4 - Segmenting Videos

You may work individually or in pairs for this assignment.

This assignment deals with Depth Images. A Depth image is a like a regular image except at each pixel position in the image instead of the color of the pixel being stored it is the distance of that pixel from the camera. Depth images make it easier to segment images into the real-world objects that are in the image because neighbouring pixels are close in distance from the camera. Whereas in typical images the RGB color of a pixel may have little to do with where the real-world object the pixel relates to is positioned in the world. 

See for example below the depth image on the left (visualized as a grayscale image) and a colored and segmented version of the same photo on the right:

![sample depth and colored image](image1.png)

In this assignment you'll process a simple format of a depth image (see the 'm' folder in the starter code) as follows, each file is of the form imX.xy, where X is the number of the image in a sequence of video:

The first two numbers in the file are the width and height of the image (in fact aside from the test image (t.xy), each image is 80wx60h). This is followed by hxw double values in a row major format, that is the first 80 double values are the first row of the image, followed by the next row and so on this repeats h times. 

In general your goal is to segment the depth image into it's individual components, where a component in the image is an interconnected group of pixels that are 'close' in distance to their neighbouring pixels. You will use a variant of the Connected Components algorithm from class where a neighbour pixel is any pixel that is adjacent to a given pixel (adjacent includes the diagonals, so each pixel aside from border pixels has 8 neighbours (N, S, E, W and diagonals: NE, NW, SE, SW). Two neighbouring pixels, p1 and p2 are considered part of the same component if abs(p1 - p2) < threshold. Where the threshold is set in the program and is configureable.   

You are expected to implement the the ImageProcessor interface in your class called DepthImageProcessor. The interface has the following methods that must be completed (a sample of some of the main methods there are a few others):


```java
interface ImageProcessor {

  /**
  Set the threshold which determines how close neighbouring pixels must be to be considered part of the same 
  blob. Any neighbouring pixels that are less than threshold in difference (between their values in the raw depth image), 
  are considered part of the same component (or blob, or segment). 
  **/
  void setThreshold(double threshold);


  /** Method to process a depth image file
      This method creates a color image based on a segmentation of the raw depth image 
      into its segments (AKA blobs). After this method is called it is possible to retrieve the Blobs
      or retrieve the color image. In the color image the pixels pertaining to the same blob should be
      colored the same color.
  **/
  void processFile(String fileName) ;
  
  /**
    This is called only after processFile and returns the colored color image. Where as much as possible each blob is 
    colored with it's own color. And pixels belonging to the same blob are colored the same.
  **/
  int[][] getColorImg();
  
  /**
  Any pixels that are closer than min distance to the camera should be ignored. This method sets that min distance. 
  These pixels are not colored in your color image and not included in any blobs.
  **/
  void setMinDist(double minDistance)
  
  /**
  Any pixels that are farther than max distance from the camera should be ignored. This method sets that max distance. 
  These pixels are not colored in your color image and not included in any blobs.
  **/
  void setMinDist(double minDistance)
  
  
  /**
  To facilitate the labeling of the blobs in the image, you should return an ArrayList of all of the blobs in the color image
  This is only called after processImage has been called. Whereas colors may change from frame to frame within an image. 
  As much as possible the label of the blobs should remain the same from one frame to the next. 
  The Interface for a blob is provided below.
  **/
  ArrayList<Blob> getBlobs();
  
  //there are a few other methods that need be implemented in this interface, not shown here
}
```

The Blob interface is as follows:

```java

interface Blob {

  /**
  Retrieve the row (y) and column (x) of the centroid for this blob.
  The centroid is the average x value and average y value (rounded down to nearest int).
  **/
  Point getCentroid();

  /**
  return the label for this blob
  **/
  int getLabel();

  /**
  set the label for this blob. Note that you may want to track the labels from one frame to the next 
  so you can best label the blobs to maintain label consistency
  **/
  void setLabel(int label);
}
```

See in the provided video some examples of how the code should work, you'll want to ensure you don't have any stack overflows. The threshold for determining if neighbouring pixels belong to the same blob is configurable using a slider in the GUI. The GUI will process the files in the order they appear. You should make an attempt to label the blobs consistently from frame to frame. You'll note this is a challenging computer science problem for a number of reasons. However a general rule of thumb maybe to label the blob using the blob with the closest centroid from the previous frame in the video. 

In the starter code you should complete the DepthImageProcessor class and then add other classes and methods as you see fit. Do not adjust any of the GUI classes - they are rather fragile. A few simple unittests are provided to see if your solution is on the correct track.

Video Link: 
https://youtu.be/-5jR8MuR29g

1st Deliverable: Monday March 30: 11:59pm 
Plan.pdf
A one paragraph pdf file checked into your repository outlining who is working together (if anyone) and how you will label the blobs from one frame in the video to the next.

2nd Deliverable: Friday April 3: 11:59pm. The remaining portion of the assignment.

Grading Scheme:

Plan: 1pt (due Monday March 30). 

Code Readability: 2pts -variable names, method names, method comments

Code Design: 3pts - appropriate separation of data and algorithms, appropriate methods, efficiency of design
 
Functionality: 4pts - does the program segment and color the depth images, does it respond to different inputs, does it crash, do all test pass. 




