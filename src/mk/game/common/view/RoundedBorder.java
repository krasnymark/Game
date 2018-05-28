package mk.game.common.view;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/13/12
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundedBorder implements Border
{
    private int radius;

    public RoundedBorder(int radius)
    {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c)
    {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }


    public boolean isBorderOpaque()
    {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}