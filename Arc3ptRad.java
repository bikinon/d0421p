package d0421p;
import java.awt.*;



public class Arc3ptRad {

    static final double TOL = 0.0000001;
    public static double pt1X;
    public static double pt1Y;
    public static double pt2X;
    public static double pt2Y;
    public static double pt3X;
    public static double pt3Y;
    public static double radius;
    public static double ctrX;
    public static double ctrY;
    public static double StartAngle;
    public static double EndAngle;
    
    public static void FindCtrRad3PtOnArc()
    {
      Point p1 = new Point(pt1X, pt1Y);
      Point p2 = new Point(pt2X, pt2Y);
      Point p3 = new Point(pt3X, pt3Y);
      Circle c = circleFromPoints(p1, p2, p3);
//      System.out.println(c);
//      System.out.println(c.radius);
//      System.out.println(c.center.x);

      radius = c.radius;
      ctrX = c.center.x;
      ctrY = c.center.y;
    }


    public static Circle circleFromPoints(final Point p1, final Point p2, final Point p3)
    {
      final double offset = Math.pow(p2.x,2) + Math.pow(p2.y,2);
      final double bc =   ( Math.pow(p1.x,2) + Math.pow(p1.y,2) - offset )/2.0;
      final double cd =   (offset - Math.pow(p3.x, 2) - Math.pow(p3.y, 2))/2.0;
      final double det =  (p1.x - p2.x) * (p2.y - p3.y) - (p2.x - p3.x)* (p1.y - p2.y); 

      if (Math.abs(det) < TOL) { throw new IllegalArgumentException("Yeah, lazy."); }

      final double idet = 1/det;

      final double centerx =  (bc * (p2.y - p3.y) - cd * (p1.y - p2.y)) * idet;
      final double centery =  (cd * (p1.x - p2.x) - bc * (p2.x - p3.x)) * idet;
      final double radius = 
         Math.sqrt( Math.pow(p2.x - centerx,2) + Math.pow(p2.y-centery,2));

      return new Circle(new Point(centerx,centery),radius);
    } // circleFromPoints
    

    static class Circle {
      final Point center;
      final double radius;
      public Circle(Point center, double radius)
      {
        this.center = center; this.radius = radius;
      }
      @Override 
      public String toString()
      {
        return new StringBuilder().append("Center= ").append(center).append(", r=").append(radius).toString();
      }
    } // Circle

    static class Point {
      final double x,y;

      public Point(double x, double y)
      {
        this.x = x; this.y = y;
      }
      @Override
      public String toString()
      {
        return "("+x+","+y+")";
      }

    } // Point

    
    /**
     * 
     * @param P1x Start X
     * @param P1y Start Y
     * @param P2x End X
     * @param P2y End Y
     * @param Radius
     * @param Direction 2=CW, 3=CC
     * @param ErrorDescription String
     * @return ArcCntrX ArcCntrY StartAngle EndAngle
     */
     protected double[] ArcCenter(double P1x, double P1y, double P2x, double P2y, double Radius, int Direction, String ErrorDescription)
     {
       /* returns arc center based on start and end points, radius and arc direction (2=CW, 3=CCW)
       ** Radius can be negative (for arcs over 180 degrees)
       ** Added StartAngle & EndAngle to returned values - Improved, need a bit more work
       */
       double Angle = 0, AdditionalAngle = 0, L1 = 0, L2 = 0, Diff=0;
       double AllowedError = 0.002;
       double CenterX = 0;
       double CenterY = 0;
       ErrorDescription = "";
       double T1x;
       double T1y;
       double T2x;
       double T2y;
       
       StartAngle = 0;
       EndAngle = 0;
       double[] rtnAry = {0,0,0,0}; // = new double[4];
       
       // Sort points depending of direction
       if (Direction == 2)
       {
         T1x = P2x;
         T1y = P2y;
         T2x = P1x;
         T2y = P1y;
       }
       else // 03
       {
         T1x = P1x;
         T1y = P1y;
         T2x = P2x;
         T2y = P2y;
       }
       
       // find angle arc covers
       Angle = CalculateAngle(T1x, T1y, T2x, T2y);
       
       L1 = PointDistance(T1x, T1y, T2x, T2y) / 2;
       Diff = L1 - Math.abs(Radius);
       
       if (Math.abs(Radius) < L1 && Diff > AllowedError)
       {
         ErrorDescription = "Error - wrong radius";
         //  return  String.valueOf(CenterX) + "," + String.valueOf(CenterY);
         rtnAry[0] = CenterX;
         rtnAry[1] = CenterY;
         rtnAry[2] = StartAngle;
         rtnAry[3] = EndAngle;
         return rtnAry;
       }
       
       L2 = Math.sqrt(Math.abs(Math.pow(Radius,2) - Math.pow(L1,2)));
       
       if (L1 == 0)
       AdditionalAngle = Math.PI / 2;
       else
       AdditionalAngle = Math.atan(L2 / L1);
       
       // Add or subtract from angle (depending of radius sign)
       if (Radius < 0)
       Angle -= AdditionalAngle;
       else
       Angle += AdditionalAngle;
       
       // calculate center (from T1)
       CenterX = (double) (T1x + Math.abs(Radius) * Math.cos(Angle)); 
       CenterY = (double) (T1y + Math.abs(Radius) * Math.sin(Angle));
       
       //double StartAngle = Math.atan2(P1y-CenterY, P1x - CenterX);
       StartAngle = Math.toDegrees(Math.atan2(P2y - CenterY, P2x - CenterX));
       EndAngle = Math.toDegrees(Math.atan2(P1y - CenterY, P1x - CenterX)); // ArcTan2(ey-cy, ex-cx)
       
       if(StartAngle < 0) {
         StartAngle = 360 + StartAngle;
       }
       if(EndAngle < 0) {
         EndAngle = 360 + EndAngle;
       }       
       
       //return String.format("%.9f",CenterX) + "," + String.format("%.9f",CenterY) + "**" + String.valueOf(StartAngle + "**" + String.valueOf(EndAngle));
       rtnAry[0] = CenterX;
       rtnAry[1] = CenterY;
       rtnAry[2] = StartAngle;
       rtnAry[3] = EndAngle;
       return rtnAry;
     } // ArcCenter
     
     
     protected double CalculateAngle(double P1x, double P1y, double P2x, double P2y)
     {
       /* Part of ArcCenter */
       // returns Angle of line between 2 points and X axis (according to quadrants)
       double Angle = 0;
       
       if ((P1x == P2x) && (P1y == P2y))// same points
       return 0;
       else if (P1x == P2x) // 90 or 270
       {
         Angle = Math.PI / 2;
         if (P1y > P2y) Angle += Math.PI;
       }
       else if (P1y == P2y) // 0 or 180
       {
         Angle = 0;
         if (P1x > P2x) Angle += Math.PI;
       }
       else
       {
         Angle = Math.atan(Math.abs((P2y - P1y) / (P2x - P1x))); // 1. quadrant
         if (P1x > P2x && P1y < P2y) // 2. quadrant
         Angle = Math.PI - Angle;
         else if (P1x > P2x && P1y > P2y) // 3. quadrant
         Angle += Math.PI;
         else if (P1x < P2x && P1y > P2y) // 4. quadrant
         Angle = 2 * Math.PI - Angle;
       }
       return Angle;
     } // CalculateAngle
     
     protected double PointDistance(double P1x, double P1y, double P2x, double P2y)
     {
       /* Part of ArcCenter */
       return Math.sqrt(Math.pow((P2x - P1x), 2) + Math.pow((P2y - P1y), 2));
     }  // PointDistance
    
     
}  // class
