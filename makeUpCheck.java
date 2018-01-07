package d0421p;

import javax.swing.JOptionPane;

public class makeUpCheck {

	public double l = 0; // not used on d0421p
	public double w = 0;
	public double d = 0;
	public double lidTab = 0; // d0421p
	
	public void d0421pMakeUpCheck() {

	      // **** WARNINGS ****
	      if (d - 3 > w) {
	        JOptionPane.showMessageDialog(null, "Depth > Width Error. \nTHIS FOLDER WILL NOT GLUE UP", "Make-up ERROR", JOptionPane.ERROR_MESSAGE);
	      }
	      if (d > w / 1.9) {
	        JOptionPane.showMessageDialog(null, "Depth > Width / 2. \nGHOST CREASES MAY BE NECESSARY", "Warning", JOptionPane.WARNING_MESSAGE);
	      }
	      int mindepth = 69;
	      if (d < mindepth) {
	        JOptionPane.showMessageDialog(null, "Depth < " + Integer.toString(mindepth) + "mm. \nConsider TOE lock.", "Warning", JOptionPane.ERROR_MESSAGE);
	      }    
	      
	      // * *********************************
	      int lkflp = (int) Math.round((w / 3) * 1.33);
	      //    if d < 15) {  Lock in Flap on side of lid
	      //      this.lidtabD = this.depth + this.dataArray[3];
	      //      JOptionPane.showMessageDialog(null, "lidtabD < 15 - Fixing.");
	      //    }
	      double fg = (w - lkflp) / 2;
	      if (d + fg > w) {
	        JOptionPane.showMessageDialog(null, "Make-up Warning: D + Glue Flap > W - Wont Glue on Machine", "Make-up ERROR", JOptionPane.ERROR_MESSAGE);
	      } // end of if
	      if (lidTab > d) {
	        JOptionPane.showMessageDialog(null, "** WARNING - CHECK LID TAB > DEPTH **", "ERROR", JOptionPane.ERROR_MESSAGE);
	      }
	      // ************************************ *
		
	} // d0421pMakeUpCheck
	
}
