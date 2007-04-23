package statistics;

import java.awt.*;

import ui.GUI;
import javax.swing.*;


//Class to hold a value for a slice
public class PieChart extends JComponent {
    double value;
    Color color;

    public PieChart(double value, Color color) {
        this.value = value;
        this.color = color;
    }


// slices is an array of values that represent the size of each slice.
public static void drawPie(Graphics g, Rectangle area, PieChart[] slices) {
    // Get total value of all slices
    double total = 0.0D;
    for (int i=0; i<slices.length; i++) {
        total += slices[i].value;
    }

    // Draw each pie slice
    double curValue = 0.0D;
    int startAngle = 0;
    for (int i=0; i<slices.length; i++) {
        // Compute the start and stop angles
        startAngle = (int)(curValue * 360 / total);
        int arcAngle = (int)(slices[i].value * 360 / total);

        // Ensure that rounding errors do not leave a gap between the first and last slice
        if (i == slices.length-1) {
            arcAngle = 360 - startAngle;
        }

        // Set the color and draw a filled arc
        g.setColor(slices[i].color);
        g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

        curValue += slices[i].value;
    }
}

}

