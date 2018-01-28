package d0421p;

import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;
/**
  *
  * Description
  *
  * @version 1.0 from 7/30/2016
  * @author Tim Gathercole
  */
// import dxf12objects;

public class draw0421pcls extends dxf12objects {
  
  // start attributes
 
  public double[] dataArray = {0,0,0,0,0,0,0,0,0,0,0,0};

  public double length = 0;
  public double width = 0;
  public double depth = 0;
  public String style = "0421+";
  public String unit = "m";
  public String flute = "E";
  
  public double slot; // Width of Lock Slot
  public double dmain;
  public double dside; // Depth side attached to TOE
  public double dinr;  // Opposite side of Main Depth, covered by the lid 
  public double dblbend;   // Double Bend
  public double flpcb;    // Flap Cut Back in from the TOE bend
  public double lidOffset; // Lid Out set from base
  public double toeflap;
  public double lug;
  public double lmain;
  public double winner;
  public double wouter;
  public double odAlw; // BB to OD allowance (Caliper)
    
  public double slotw;
  public double sofst;
  public double lsgap = 200; // Lock Side Gap - rubbish figure to be over written   
  public double lsGapExtra = 0;

  public double lidtabD; // lock in tab on Lid width section
  public double frontFlap;
  public double lidTabAdd; // Increase of Lid Tab over Slot (x2)
//  public double baseArcCutOutSz = 10; // Opposite value, not Hypotinuse -hgt / Math.sin(Math.toRadians(45));  // for Std 10mm Opposite, this gives 14.14213562
  public double arcExtra = 2; // Base cutout Clearance value
  
  public double blks1; 
  public double blkn2;
  public double blkn2IntLk = 0;
  public double interlockblks1; // being decremented
  public double interlockblkn2;
  
  public double eliteFlexoS1 = 1055;
  public double eliteFlexoN2 = 1575;
  public int noUpS1 = 0;
  public int noUpN2 = 0;
  
  public String CUT;
  public String CREASE;
  public int col = 1; // Line Colour
  public String ltype = "CONTINUOUS"; // Line Type
  // end attributes
  
  // start methods
  public String Draw0421p() {
    
    this.allowanceSetup(); // Set up Allowances & Bend to Bend Sizes
    
    int lkflp = (int) Math.round((this.wouter / 3) * 1.33);
    if (this.lidtabD < 15) { // Lock in Flap on side of lid
      this.lidtabD = this.depth + this.dataArray[3];
      JOptionPane.showMessageDialog(null, "lidtabD < 15 - Fixing.");
    }
    double fg = (this.winner - lkflp) / 2; // lock flap side gap 
    
    //45 deg Flap vars
    double cutback = 11;
    if (this.dmain - cutback > (fg + this.lidTabAdd)) {
      cutback = this.dmain - (fg + this.lidTabAdd + this.flpcb); // Stop folded flaps fouling Flap slot ** Should be multiple Designs
    }
    double triTemp = Math.sqrt(Math.pow(cutback,2)  + Math.pow(cutback,2));
    double triTemp2 = triTemp / 2;
    //  double triTemp3 = Math.sqrt(Math.pow(cutback, 2)  + Math.pow(triTemp2, 2)); // Hypotenuse of Triangle we want
    double cutback45 = Math.sqrt(Math.pow(triTemp2, 2) / 2);
    double creCut = (int)Math.round(this.dmain / 3); // cut in panel to allow 180 deg bend
    
    double flaplen = this.dside - cutback;
    double flpMidBit = flaplen - this.dblbend - this.flpcb;  
    
    this.lsgap = this.toeflap - this.slot;
    this.lsGapExtra = this.dmain - this.dside;  // Extra reduction due to change of allowances
    
    // Set EXTENTS
    this.dxfymax = String.valueOf(this.wouter * 2 + (this.dmain * 3));
    this.dxfxmax = String.valueOf((this.dmain + this.dblbend + this.toeflap) * 2 + this.lmain);
    //    DimensionSizes(0.0, 0.0, Double.parseDouble(dxfxmax), Double.parseDouble(dxfymax));
    
    dxf += dxf_header12();
    
    // draw the main body starting main depth btm right
    Line(-lmain, 0, "MATRIX"); // Upper of Base bends
    
    relMove(0, dmain);
    // middle Main Depth Panel & Right Side Lock slot
    Line(lmain, 0, "MATRIX"); // Top main depth bend
    Line(0, -(lsgap + lsGapExtra), CREASE); // Lock Slot Gap - Not the same oa the other side
    
    this.lockSlot();    
 //   relMove(0, -slot);
    
    Line(0, -(dmain - toeflap - lsGapExtra), CREASE); // To Base
    
    /* *********** LOTS More Work!!! **********/  
    // Draw 45 deg Flap - Mid panel
    double creCut45 = (dside - flpcb - cutback45);
    double creCut453 = (int) (creCut45 / 3);  // Gives a decimal because we arent working on Hypotenuse
    
    mid45DegFlapT3(fg); //mid45DegFlapSTD(creCut, flpMidBit, cutback, creCut45, creCut453);
    
    // Move back to 0,0
    this.absMove(0, 0);
    Line(0, -winner, CREASE);
    mid45DegFlapT3S(fg);
    
    // Move back to 0,0
    this.absMove(0, 0);
    relMove(dside, 0);
    
    // Lock TOE Flap section
    TOE(fg, lkflp);
    
    // Move back to 0,0
    xabs = 0;
    yabs = 0;
    relMove(0, dmain);
    
    // Lid Main Body
    double flapBackCutAngle = 0;
    double hyp = (fg + this.lidTabAdd) + (lkflp - (this.lidTabAdd * 2));
    double flapFrontCutAngle = (int) ((fg + lkflp) - Math.sqrt(Math.pow(hyp, 2) - Math.pow(lidtabD, 2))) + 2;
    if (flapFrontCutAngle < 1) {  // stop problem with very long, short width flaps - Rad doesnt meet the edge
      flapFrontCutAngle = 0;
      JOptionPane.showMessageDialog(null, "flapFrontCutAngle = 0. Flap Angle cut not properly calculated.");
    }
    
    Line(lidOffset, lidOffset, CUT);
    Line(0, fg - lidOffset, CUT);
    
    Line(lidtabD, flapBackCutAngle, CUT);
    Line(0, lkflp - flapBackCutAngle - flapFrontCutAngle, CUT);
    Line(-lidtabD, flapFrontCutAngle, CUT);
    Line(0, -lkflp, CREASE);
    relMove(0, lkflp);
    Line(0, wouter - fg - lkflp, CUT);
    
    // Top Lid Crease
    Line(-lmain - (lidOffset * 2), 0, "MATRIX");
    relMove(lmain + (lidOffset * 2), 0);
    
    // Outer Lid P&S Panel  
    outerLid();
    
    // Are Ghost Creases Required?
    if (this.depth > (this.width / 1.9) ) {
      this.absMove(0, -this.winner);
      this.Line(-this.dinr, -this.dinr, this.CREASE);
    }    
    
    // dxf += dxf_footer12(); WONT PRINT!
    dxf +="  0\r\n";
    dxf +="ENDSEC\r\n";
    dxf +="  0\r\n";
    dxf +="EOF\r\n";
    
    return dxf; // dxf;
    
  }
  
  protected String DimensionSizes(double llx, double lly, double urx, double ury) {
    double x = Math.abs(llx - urx);
    double y = Math.abs(lly - ury);
    //    double sv = (x / 32); //Line Scale
    String err = ""; // Later version might return an error if necessary
    double txv = 1;
    
    if (y * 1.2 > x) {
      txv = y;
    } else {
      txv = x;
    }
    //(command "_LTSCALE" sv)
    dimasz = String.valueOf(txv / 40);  // Arrow Size
    dimtxt = String.valueOf(txv / 65);  // Text Size
    dimexe = String.valueOf(txv / 260); // Extension of witness line above dimension line
    
    dimexo = String.valueOf(txv / 100); // Extension line origin offset (was 260)
    dimgap = String.valueOf(txv / 70);  // Gap from dimension line to text (was 90)
    
    /*
    (command "_.LAYER" "_SET" "DIMS" ""
    "DIMASZ" (/ txv 40.0)  ; Arrow Size
    "DIMTXT" (/ txv 65.0)  ; Text Size
    "DIMEXE" (/ txv 260.0) ; Extension of witness line above dimension line
    "LUNITS" Unt           ; Drawing Units
    "DIMUNIT" DimUnt       ; Dim Units *Obsolete. Replaced by DIMLUNIT and DIMFRAC?
    "LUPREC" DecP          ; drawing decimal Precesion
    "DIMDEC" DimP          ; Dim decimal Precision
    "DIMZIN" 8             ; Suppresses trailing zeros in Decimals
    "DIMAZIN" 2            ; Suppresses trailing zeros in Decimals - v ICAD 6
    ; ----------------------------------------------------
    "DIMEXO" (/ txv 260.0) ; Extension line origin offset
    "DIMGAP" (/ txv 90.0)  ; Gap from dimension line to text
    )
    */
    return err;
  }
  

  
  
  protected void lockSlot() {
  /* Side Locking Slot
   * Used in Draw0421p & mid45DegFlapT3 
   * slot = Length ofLock Slot
   * slotw = Width of Lock Slot
   * Starts @ "TOP" middle of Slot
   */
  double slotRad = 2;
  double sltBlg = 9 - slotw;
  
//    Line(-slotw, 0, CUT);
//    Line(0, -slot, CUT);
//    Line(slotw, 0, CUT);
//    arc2(xabs + 0, yabs + slotRad, slotRad, 270, 0, slotRad, slotRad, CUT);
//    Line(0, slot - 4, CUT);
//    arc2(xabs - slotRad, yabs + 0, slotRad, 0, 90, -slotRad, slotRad, CUT);
    
    Arc3ptRad oArc = new Arc3ptRad();
    oArc.pt1X = this.xabs; // Absolute Values
    oArc.pt1Y = this.yabs;
    oArc.pt2X = this.xabs + sltBlg;
    oArc.pt2Y = this.yabs - slot/ 2;
    oArc.pt3X = this.xabs;
    oArc.pt3Y = this.yabs - slot;
    oArc.FindCtrRad3PtOnArc();
    
    double aDat[] = oArc.ArcCenter(xabs, this.yabs - slot, xabs, yabs, oArc.radius, 3, "");
    this.arc2(oArc.ctrX, oArc.ctrY, oArc.radius, oArc.EndAngle, oArc.StartAngle, oArc.pt3X, oArc.pt3Y, CUT);
    
    this.absMove(oArc.pt1X, oArc.pt1Y );
    Line(-slotw + slotRad, 0, CUT);
    arc2(xabs + 0, yabs - slotRad, slotRad, 90, 180, -slotRad, -slotRad, CUT);
    Line(0, -slot + (slotRad * 2), CUT);
    
    arc2(xabs + slotRad, yabs, slotRad, 180, 270, slotRad, -slotRad, CUT);
    Line(slotw - slotRad, 0, CUT);    
  //  this.relMove(-slot, 0);
  }
  

//  protected void baseArcCutOut(double hgt, int upDwn) {
//    /* Assumes starting point is the base corner 
//    *   hgt : Verticle hit point on the 45 deg Bend
//    * upDwn : 1 = Arc goes UP / -1 = Arc goes DOWN
//    * This Version FIXED SIZE
//    */
//  double tmpX = this.xabs, tmpY = this.yabs;  
//  double radius = 14.14214;
//  this.Line(radius, 0, this.CUT); 
//  double strAng = 0;
//  double endAng = 44.58157;
//  if (upDwn == 1) {
//    this.arc2(tmpX, tmpY, radius, strAng, endAng, tmpX, tmpY, this.CUT); // Main Arc
//    radius = 2;
//    strAng = 44.58157;
//    endAng = 44.58157 + 93.08314; 
//    this.arc2(tmpX + 8.64826, tmpY + 8.52286, radius, strAng, endAng, tmpX + 7.16983, tmpY + 9.86979, this.CUT); // Main Arc
//    this.xabs = tmpX + 7.16983;
//    this.yabs = tmpY + 9.86979;
//    this.Line(-6.6483,  -7.2973, this.CUT); // Long Line
//
//    endAng = 180;
//    strAng = endAng - 42.33529;
//    this.arc2(tmpX + radius, tmpY + 1.22555, radius, strAng, endAng, tmpX, tmpY + radius, this.CUT);
//  } else {
//    strAng = 360 - 44.581570;
//    endAng = 0;
//    this.arc2(tmpX, tmpY, radius, strAng, endAng, tmpX, tmpY, this.CUT); // Main Arc 
//    
//    radius = 2;
//    strAng = 360 - (44.58157 + 93.08314);
//    endAng = 360 - 44.58157;  
//    this.arc2(tmpX + 8.64826, tmpY - 8.52286, radius, strAng, endAng, tmpX + 7.16983, tmpY + 9.86979, this.CUT); // Main Arc
//    this.xabs = tmpX + 7.16983;
//    this.yabs = tmpY - 9.86979;
//    this.Line(-6.6483,  7.2973, this.CUT); // Long Line
//    
//    endAng = 180 + 42.33529;;
//    strAng = 180;
//    this.arc2(tmpX + radius, tmpY - 1.22555, radius, strAng, endAng, tmpX, tmpY + radius, this.CUT);   
//  }
//
//  this.absMove(tmpX, tmpY);
//  this.Line(0, 1.2256 * upDwn, this.CUT);
//  
//  this.absMove(tmpX, tmpY); // Move back to the Corner of the Folds / Cut-out   
//  } // baseArcCutOut
  
  
  protected void mid45DegFlapT3(double glFlap)
  {
    /* Main Depth attached 45 degree Webbed Flap 
    * glFlap = lock flap side gap 
    *** GLOBALS ************
    flpcb          - Flap Cut Back in from the TOE bend  
    lsgap          - Lock Side Gap
    */ 
    double flpcbMain = this.dmain - (this.dside - this.flpcb); // Not same as flpcb  
//    double creCutArcSz = this.baseArcCutOutSz / Math.sin(Math.toRadians(45));  // for Std 10mm Opposite, this gives 14.14213562
    double creCut = (int)Math.round(this.dmain  / 3);
    double angCutBack = this.dmain - glFlap - this.lidtabD - this.flpcb;
    //    double creCut45 = (Math.sqrt((glFlap*glFlap) * 2) - baseArcCutOutSz) * Math.sin(Math.toRadians(45)); //(this.dmain - baseArcCutOutSz)- this.lidtabD - this.flpcb - (angCutBack / 2); // total value of 45 deg bend
    double creCut45 = glFlap ; // We want X&Y not angled line size
    double creCut453rd = (int) (creCut45 / 3);  // 1/3 of 45 deg bend
    double inCut = dside - flpcb - glFlap;
    double tmpX = 0;
    double tmpY = 0;
    double chamfer = 5;
    
    this.relMove(0, 0);
    // Line(this.dside - creCutArcSz, 0, CREASE);
    Line(creCut, 0, CREASE);
    Line(creCut, 0, CUT);
    Line(this.dside - (creCut * 2), 0, CREASE);
    // From the TOE side
    Line(-flpcb, dblbend, CUT); // VERTICLE  6mm - Silly reuse of dblbend for chamfer
    if (dside - flpcbMain < glFlap) {//
      creCut45 = (dside - flpcbMain); // Find out WHY 4.25 some time!
      creCut453rd = (int) (creCut45 / 3);
      Line(0, (dside - flpcbMain - this.dblbend - chamfer), CUT);
    } else {
      chamfer = 2;
      Line(0, (glFlap - this.dblbend), CUT);  
      Line(-inCut + chamfer, 0, CUT);
    }//
    // Line(-this.lidtabD, 0, CUT);
    tmpX = this.xabs; // store current point
    tmpY = this.yabs;
    this.absMove(0, this.dmain);
    
    Line(this.dblbend, -flpcbMain, CUT);
    if (dside - flpcbMain < glFlap) {
      Line(dside - flpcbMain - this.dblbend - chamfer, 0, CUT);
    } else {
      Line(glFlap - this.dblbend, 0, CUT);
      Line(0, -inCut + chamfer, CUT);
    }
    Line(tmpX - this.xabs, tmpY - this.yabs, CUT);
    
    this.absMove(0, 0);
    // 45 degree bend
//    this.baseArcCutOut(baseArcCutOutSz, 1);
    
//    this.relMove(baseArcCutOutSz, baseArcCutOutSz);
    Line(creCut453rd, creCut453rd, CREASE);
    Line(creCut453rd, creCut453rd, CUT);
    Line(creCut45 - (creCut453rd * 2) + (chamfer / 2), creCut45 - (creCut453rd * 2) + (chamfer / 2), CREASE);   
  } // mid45DegFlapT3 
  
  
  
  
  protected boolean outerLid() {
    double ripTabR = 8.5;
    double ripTabL = 30;
    double ripTabW = 12;
    //    double ripTabAng = (ripTabR * 2) - ripTabW;
    double lflapX = ripTabR + 0.5;
    double lflapY = 15;
    double peelSealW = 20;
    double topRad = 12;
    int CW = 1;
    //  Line(-lidtabD, flapFrontCutAngle);
    Line(-lflapX, lflapY, CUT);
    Line(0, frontFlap - (lflapY + peelSealW + (ripTabR * 2)), CUT);
    Line(-ripTabL, (ripTabR * 2) - ripTabW, "SAFETY");
    relMove(0, ripTabW);
    Line(ripTabL, 0, "SAFETY");
    
    relMove(0, -ripTabR * 2); // Arc will only go 1 way & has to be Semi-Circle?
    arc(0, ripTabR * 2, ripTabR, "SAFETY", 1, CW);
    
    //Line(0, peelSealW);
    Line(0, peelSealW - topRad, CUT);
    arc2(xabs - topRad, yabs + 0, topRad, 0, 90, -topRad, topRad, CUT);
    
    double psTopLine = -(lmain + (flpcb * 4)) + (lflapX * 2) + (topRad * 2);
    Line(psTopLine, 0, CUT);
    
    // Peel & Seal
    relMove(0, -6);
    Line(-psTopLine, 0, "Annotation");
    relMove(0, -12);
    Line(psTopLine, 0, "Annotation");
    relMove(0, -6);
    Line(-psTopLine, 0, "Annotation");
    relMove(0, -6);
    Line(psTopLine, 0, "Annotation");  
    
    TextInsert(-psTopLine / 2, 15, "Peel & Seal", "Annotation");
    TextInsert(-psTopLine / 2, 3, "Rippa Tape", "Annotation");
    
    // Last 2 Main Bends
    // Move back to 0,0
    xabs = 0;
    yabs = 0;
    relMove(0, -winner);
    Line(-lmain, 0, "MATRIX");  // Btm Base bend
    relMove(0, -dinr);
    Line(lmain, 0, CUT);
    relMove(-(lmain / 2), 0);
    // MIRROR LINE
    Line(0, 500, CUT);
    
    return true;
  }
  
  /**
  * Turn Over End side lock
  * @param fg
  * @param lkflp
  * @return
  */
  protected void TOE(double fg, double lkflp) {
    double FlpSlotRad = dblbend / 2; // the 90 deg radius part of the lock slot  
    double lugAng = 2;
    
    Line(dblbend + lsgap, 0, CUT);
    Line(lugAng, lug, CUT);
    Line(slot - 2, 0, CUT);
    Line(0, -winner - (lug * 2), CUT);
    Line(-slot + 2, 0, CUT);
    Line(-lugAng, lug, CUT);
    Line(-(lsgap + dblbend), 0, CUT);  
    
//    // Double Bend & Top Flap Lock Slot
//    Line(0, fg + this.lidTabAdd, "MATRIX");
//    Line(dblbend, 0, CUT);
//    relMove(-dblbend, 0);
//    Line(0, lkflp - (this.lidTabAdd * 2), CUT);
//    Line(dblbend, 0, CUT);
//    relMove(-dblbend, 0);
//    Line(0, fg + this.lidTabAdd, "MATRIX");
//    
//    relMove(dblbend, 0);
//    Line(0, -(fg + this.lidTabAdd), CREASE);

    // Double Bend & Top Flap Lock Slot
    Line(0, fg + this.lidTabAdd + FlpSlotRad, "MATRIX");
    this.arc2(xabs + FlpSlotRad, yabs + 0, FlpSlotRad, 180, 270, FlpSlotRad, -FlpSlotRad, CUT);
    Line(dblbend - FlpSlotRad, 0, CUT);
    relMove(-dblbend, FlpSlotRad);
    Line(0, lkflp - (this.lidTabAdd * 2) - (FlpSlotRad * 2), CUT);
    this.arc2(xabs + FlpSlotRad, yabs + 0, FlpSlotRad, 90, 180, FlpSlotRad, FlpSlotRad, CUT);
    Line(dblbend - FlpSlotRad, 0, CUT);
    relMove(-dblbend, -FlpSlotRad);
    Line(0, fg + this.lidTabAdd + FlpSlotRad, "MATRIX");
        
    relMove(dblbend, 0);
    Line(0, -(fg + this.lidTabAdd), CREASE);

    
    Arc3ptRad oArc = new Arc3ptRad();
    oArc.pt1X = this.xabs;
    oArc.pt1Y = this.yabs;
    oArc.pt2X = this.xabs + dblbend;
    oArc.pt2Y = this.yabs - lkflp/ 2 + this.lidTabAdd;
    oArc.pt3X = this.xabs;
    oArc.pt3Y = this.yabs - lkflp + (this.lidTabAdd * 2);
    oArc.FindCtrRad3PtOnArc();
    
    double aDat[] = oArc.ArcCenter(xabs, this.yabs - lkflp + (this.lidTabAdd * 2), xabs, yabs, oArc.radius, 3, "");
    this.arc2(oArc.ctrX, oArc.ctrY, oArc.radius, oArc.EndAngle, oArc.StartAngle, oArc.pt3X, oArc.pt3Y, CUT);
    
    relMove(-(dside + dblbend), fg + this.lidTabAdd); // shouldnt be necessary
    
    Line(0, -(fg + this.lidTabAdd), CREASE);
  }
  
  
  protected void mid45DegFlapT3S(double glFlap)
  {
    /* Bottom Depth attached 45 degree Webbed Flap 
    * glFlap = lock flap side gap 
    *** GLOBALS ************
    flpcb          - Flap Cut Back in from the TOE bend  
    lsgap          - Lock Side Gap
    */ 
    double xTmp = this.xabs;
    double yTmp = this.yabs;  
    double flpcbMain = this.dmain - (this.dside - this.flpcb); // Not same as flpcb  
//    double creCutArcSz = this.baseArcCutOutSz / Math.sin(Math.toRadians(45));  // for Std 10mm Opposite, this gives 14.14213562
    double creCut = (int)Math.round(this.dmain / 3);
    double angCutBack = this.dmain - glFlap - this.lidtabD - this.flpcb;
    double creCut45 = glFlap; // We want X&Y not angled line size
    double creCut453rd = (int) (creCut45 / 3);  // 1/3 of 45 deg bend
    double inCut = dside - flpcb - glFlap;
    double tmpX = 0;
    double tmpY = 0;
    double chamfer = 5;
    
    this.relMove(0, 0);
    // Line(this.dside - creCutArcSz, 0, CREASE);
    Line(creCut, 0, CREASE);
    Line(creCut, 0, CUT);
    Line(this.dside - (creCut * 2), 0, CREASE);
    // From the TOE side
    
    Line(-flpcb, -dblbend, CUT); // VERTICLE  6mm - Silly reuse of dblbend for chamfer
    if (dside - flpcbMain < glFlap) {//
      creCut45 = (dside - flpcbMain); // Find out WHY 4.25 some time!
      creCut453rd = (int) (creCut45 / 3);
      Line(0, -(dside - flpcbMain - this.dblbend - chamfer), CUT);
    } else {
      chamfer = 2;
      Line(0, -(glFlap - this.dblbend), CUT);  
      Line(-inCut + chamfer, 0, CUT);
    }//
    
    tmpX = this.xabs; // store current point
    tmpY = this.yabs;
    
    this.absMove(xTmp, yTmp - this.dinr);
    
    if (dside - flpcbMain < glFlap) {
      Line((dside - flpcbMain - chamfer), 0, CUT);
    } else {
      Line(glFlap, 0, CUT);
      Line(0, -(-inCut + chamfer), CUT);
    }
    Line(tmpX - this.xabs, tmpY - this.yabs, CUT);
    
    this.absMove(xTmp, yTmp);
    // 45 degree bend
//    this.baseArcCutOut(baseArcCutOutSz, -1);
    
//    this.relMove(baseArcCutOutSz, -baseArcCutOutSz);
    Line(creCut453rd, -creCut453rd, CREASE);
    Line(creCut453rd, -creCut453rd, CUT);
    Line((creCut45 - (creCut453rd * 2) + (chamfer / 2)), -(creCut45 - (creCut453rd * 2) + (chamfer / 2)), CREASE);
    
    this.absMove(xTmp, yTmp - arcExtra);
    
    Line(0, -dinr + (lsgap - flpcb) + slot + arcExtra, CREASE);

    this.lockSlot();
  //  relMove(0, -slot);
    
    Line(0, -(lsgap - flpcb), CREASE);
    
    
  } // mid45DegFlapT3 
  
  
  
  
  
  
  
  
  public void allowanceSetup() {
    // System.exit(0);
    // JOptionPane.showMessageDialog(null, "Saved: " );
    
    if (this.flute == "E") {
      this.dataArray[0] = 2; // Main Depth allowance
      this.dataArray[1] = -0.5; // Inner Depth allowance
      this.dataArray[2] = 0; // Front Flap Allowance
      this.dataArray[3] = 0.5; // lidtabD Allowance (Used directly in program)
      this.dataArray[4] = 12;  // Length Allowance
      this.dataArray[5] = 2;   // Width Inner Allowance
      this.dataArray[6] = 3.5; // Width Outer (LID) Allowance
      this.dataArray[7] = 1;   // flpcb Flap Cut Back from TOE Depth
      this.dataArray[8] = 4;   // Slot Width
      this.dataArray[9] = 6;   // dblbend
      this.dataArray[10] = 0.5;  // Depth Side allowance / TOE Depth 
      this.dataArray[11] = 2; // lidOffset - Outset of the Lid section from the Base
      this.odAlw = 2;
    }
    else if (this.flute == "C") {
      this.dataArray[0] = 5;
      this.dataArray[1] = 2;
      this.dataArray[2] = 2;
      this.dataArray[3] = 2;
      this.dataArray[4] = 30;
      this.dataArray[5] = 5;
      this.dataArray[6] = 9;
      this.dataArray[7] = 2; // flpcb 
      this.dataArray[8] = 12;
      this.dataArray[9] = 15;
      this.dataArray[10] = 5; 
      this.dataArray[11] = 2; // lidOffset - Outset of the Lid section from the Base
      this.odAlw = 5;
    }   
    else { // "B"
      this.dataArray[0] = 3;
      this.dataArray[1] = 1;
      this.dataArray[2] = 0;
      this.dataArray[3] = 0.5;
      this.dataArray[4] = 16;
      this.dataArray[5] = 3;
      this.dataArray[6] = 5;
      this.dataArray[7] = 1; // flpcb
      this.dataArray[8] = 6;
      this.dataArray[9] = 8;  
      this.dataArray[10] = 1;
      this.dataArray[11] = 2; // lidOffset
      this.odAlw = 3;
    }   
    
    if (this.frontFlap < 35) { // Peal & Seal Flap size
      this.frontFlap = this.depth + this.dataArray[2];
    }
    this.dmain = this.depth + this.dataArray[0];
    this.dside = this.depth + this.dataArray[10]; // Depth Side allowance / Outer TOE 
    this.dinr = this.depth + this.dataArray[1];
    this.dblbend = this.dataArray[9];
    this.winner = this.width + this.dataArray[5];
    this.wouter = this.width + this.dataArray[6];
    this.lmain = this.length + this.dataArray[4];
    this.slotw = this.dataArray[8];
    this.flpcb = this.dataArray[7]; // flpcb Flap Cut Back from TOE Depth
    this.lidOffset = this.dataArray[11];
    
    // Machine Size
    this.eliteFlexoS1 = 1055 - 25;
    this.eliteFlexoN2 = 1575 - 20;
    
    this.blks1 = this.dinr + this.winner + this.dmain + this.wouter + this.frontFlap;
    this.blkn2 = this.toeflap + this.dblbend + this.dside + this.lmain + this.dside + this.dblbend + this.toeflap;
    blkn2IntLk = (this.toeflap + this.dblbend + this.dside + this.lmain + this.lidOffset + this.lidtabD); // this.blkn2 * 2;
    
    this.noUpS1 = (int) (this.eliteFlexoS1 / this.blks1);
    this.noUpN2 = 1;
    if (this.eliteFlexoN2 - this.blkn2 > 0) {
      double tmp = (this.eliteFlexoN2 - this.blkn2);
      while (tmp >= blkn2IntLk) {
        //System.out.println(rrn2 + " " + this.noUpN2 + " " + tmp);
        tmp = tmp - blkn2IntLk;
        this.noUpN2++;
      } //while
    } // if   
    
    this.interlockblks1 = this.blks1 * this.noUpS1;
    this.interlockblkn2 = this.blkn2 + (blkn2IntLk * (this.noUpN2 - 1));  // (this.toeflap + this.dblbend + this.dside + this.lmain + this.lidOffset + this.lidtabD) + blkn2; 
    
  }
  
  // end methods
} // end of draw0421pcls
  