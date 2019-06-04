# bitmap DCT2 Java
Compress a bitmap image applying Discrete Cosine Transform algorithm on it.

This software is completely written in Java language and the goal is to perform DCT technique on a raster (bitmap) image, in order to compress it as actually done by JPEG compression format.

The software is composed by six classes:
  - 1: DCTMain --> Main class
  - 2: DCTView --> This is actually the GUI class. Provides graphic components as buttons and panels, receives actions and give         the       control to the Controller class.
  - 3: DCTViewController --> The controller class. Take actions from the user (view) and manage operations.
  - 4: DCTImageController --> This class perform actions on the actual image, e.g. transform image in pixel matrix and vice versa, execute         compression etc.
  - 5: DCTGraph --> Displays a bar chart to compare execution time between custom DCT2 algorithm and Jtransform library
  - 6: CustomDCT2 --> This class implements a custom DCT2 algorithm. Note: custom DCT algorithm is not a FFT version (Fast Fourier                 Transform)
  
 User Guide
 
 This software is divided in three parts:
 
 - 1: Execute DCT2 Test --> The user enters 'N' as input parameter and presses 'Execute DCT2' button. This creates an NxN random matrix of doubles, then perform custom DCT and Jtranform DCT on that. Finally displays a bar chart to compare                execution time of both. Since the custom algorithm is not an FFT, its execution time should be close to N^3. Jtransform   DCT's              execution time should be close to N^2log(N) instead.
    
 - 2: Compress Image --> The user presses 'Select Image' button and choose a .bmp image from the file system. At this step the original        image is displayed on the left side of the window, while on the right side is displayed the original image after the execution of the Jtransform DCT2 algorithm on the whole image. 
Then the user enters 'F' and 'd' input parameters following those rules: 
      - 'F' represents the size of the blocks in which the image will be splitted. E.g. if the image's dimension is 200x300 pixels and F=25,       it will be splitted in 96 blocks of 25x25 pixels. Then F should be < of image's width/height. Note: leftover image parts will be           discarted.
      - 'd' represents the limit value over which frequencies will be resetted on each block. E.g. if block size is 25x25 and d=0 all             frequencies will be resetted, on the other hand if d=(2F-2) no frequency value will be resetted. Then 'd' should be >= 0 and <= (2F-        2).

      Once user has pressed 'Compress' button, the program will perform those operations:
      - a: transform the image in a matrix of doubles M (awt.image library). Each entry of the matrix represents the 'sample' of a                 specific pixel of the image.
      - b: split M on N blocks of FxF pixels and put them (with order) on a 3D array B. 
      - c: apply Jtransform DCT2 on each block of B
      - d: for each block of B reset frequency values over 'd' value
      - e: for each block of B apply Jtransform DCT2 Inverse 
      - f: overwrite M with ordered blocks of B
      - g: transform M in an image (awt.image library)
      - h: display the compressed image on the right side of the window
      
 - 3: Verify DCT2 --> The user presses 'Verify DCT2' button. Now the program evaluates both custom/Jtransform DCT2 scaling algorithm on the following matrix and vector:

matrix A -->

231 32 233 161 24 71 140 245
247 40 248 245 124 204 36 107
234 202 245 167 9 217 239 173
193 190 100 167 43 180 8 70
11 24 210 177 81 243 8 112
97 195 203 47 125 114 165 181
193 70 174 167 41 30 127 245
87 149 57 192 65 129 178 228

vector B -->

231 32 233 161 24 71 140 245

If scaling is correct, they should be transformed as:

matrix A -->

1.11e+03 4.40e+01 7.59e+01 -1.38e+02 3.50e+00 1.22e+02 1.95e+02 -1.01e+02
7.71e+01 1.14e+02 -2.18e+01 4.13e+01 8.77e+00 9.90e+01 1.38e+02 1.09e+01
4.48e+01 -6.27e+01 1.11e+02 -7.63e+01 1.24e+02 9.55e+01 -3.98e+01 5.85e+01
-6.99e+01 -4.02e+01 -2.34e+01 -7.67e+01 2.66e+01 -3.68e+01 6.61e+01 1.25e+02
-1.09e+02 -4.33e+01 -5.55e+01 8.17e+00 3.02e+01 -2.86e+01 2.44e+00 -9.41e+01
-5.38e+00 5.66e+01 1.73e+02 -3.54e+01 3.23e+01 3.34e+01 -5.81e+01 1.90e+01
7.88e+01 -6.45e+01 1.18e+02 -1.50e+01 -1.37e+02 -3.06e+01 -1.05e+02 3.98e+01
1.97e+01 -7.81e+01 9.72e-01 -7.23e+01 -2.15e+01 8.13e+01 6.37e+01 5.90e+00

vector B -->

4.01e+02 6.60e+00 1.09e+02 -1.12e+02 6.54e+01 1.21e+02 1.16e+02 2.88e+01



This is all. Feel free to use and share this software which is under MIT licence.
