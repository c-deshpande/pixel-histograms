# pixel-histograms
Generate Histogram for each RGB color in the given dataset.

Project done as a part of CSE-6331 Cloud Computing course at UTA.

<a href="https://lambda.uta.edu/cse6331/spring20/project1.html">Project Description</a>

<p>A pixel in an image can be represented using 3 colors: red, green, and blue, where each color intensity is an integer between 0 and 255. In this project, you are asked to write a Map-Reduce program that derives a histogram for each color. For red, for example, the histogram will indicate how many pixels in the dataset have a green value equal to 0, equal to 1, etc (256 values). The pixel file is a text file that has one text line for each pixel.</p> 

<p>For example, the line</p>

<p>23,140,45 represents a pixel with red=23, green=140, and blue=45.</p>

<p>You should write one Map-Reduce job in the Java file src/main/java/Histogram.java. You should modify the Histogram.java only.</p>

The pseudo code:

```
class Color {
    public short type;       /* red=1, green=2, blue=3 */
    public short intensity;  /* between 0 and 255 */
}
map ( key, line ):
  read 3 numbers from the line and store them in the variables red, green, and blue. Each number is between 0 and 255.
  emit( Color(1,red), 1 )
  emit( Color(2,green), 1 )
  emit( Color(3,blue), 1 )

reduce ( color, values )
  sum = 0
  for ( v in values )
      sum += v
  emit( color, sum )
```

In your Java main program args[0] is the file with the pixels (pixels-small.txt or pixels-large.txt) and args[1] is the output directory.
