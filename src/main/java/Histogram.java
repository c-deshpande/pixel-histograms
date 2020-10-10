import java.io.*;
import java.util.Scanner;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

/* single color intensity */
class Color implements WritableComparable<Color> {

    public short type;       /* red=1, green=2, blue=3 */
    public short intensity;  /* between 0 and 255 */

    //Init Method
    Color() {

    }

    /* need class constructors, toString, write, readFields, and compareTo methods */

    //Color class constructor
    Color(short t, short i) {

        type = t;
        intensity = i;
    }

    //toString method for returning output
    @Override
    public String toString() {

        return this.type + " " + this.intensity;
    }

    //Writing data to the text file
    @Override
    public void write(DataOutput dataOutput) throws IOException {

        dataOutput.writeShort(type);
        dataOutput.writeShort(intensity);
    }

    //Reading data from the file
    @Override
    public void readFields(DataInput dataInput) throws IOException {

        type = dataInput.readShort();
        intensity = dataInput.readShort();
    }

    //Comparing each color type and intensity
    @Override
    public int compareTo(Color color) {

        //If color type is similar, then check for intensity
        if (this.type == color.type) {

            if (this.intensity > color.intensity) {

                return 1;
            }
            else if (this.intensity == color.intensity) {

                return 0;
            }
            else {

                return -1;
            }
        }
        else {

            if (this.type > color.type) {

                return 1;
            }
            else {

                return -1;
            }
        }
    }
}

public class Histogram {

    public static class HistogramMapper extends Mapper<Object, Text, Color, IntWritable> {
        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            /* write your mapper code */

            //Reading data from the file
            Scanner s = new Scanner(value.toString()).useDelimiter(",");
            Color red = new Color((short) 1, s.nextShort());
            Color green = new Color((short) 2, s.nextShort());
            Color blue = new Color((short) 3, s.nextShort());

            context.write(red, new IntWritable(1));
            context.write(green, new IntWritable(1));
            context.write(blue, new IntWritable(1));

            s.close();
        }
    }

    public static class HistogramReducer extends Reducer<Color, IntWritable, Color, LongWritable> {
        @Override
        public void reduce(Color key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            /* write your reducer code */

            //Initializing sum to zero
            int sum = 0;

            //Incrementing sum at each occurence of a particular color type and intensity
            for (IntWritable v : values) {

                sum += v.get();
            }
            context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {

        /* write your main program code */
        Job job = Job.getInstance();
        job.setJobName("Histogram");
        job.setJarByClass(Histogram.class);
        job.setOutputKeyClass(Color.class);
        job.setOutputValueClass(LongWritable.class);
        job.setMapOutputKeyClass(Color.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setMapperClass(HistogramMapper.class);
        job.setReducerClass(HistogramReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.waitForCompletion(true);
    }
}