package d0421p;

public class LayerLineType {

 public String layer; 
  
  public String getLineColour() {
     // Return a colour for a layer from its Layer Name
      // Artios understands Colours only not Layers
      /* 
      AG CAD Mapping (Std/TG)       See ArtiosCAD: Defaults > Import Tuning Table > Artios DWG/DXF - Metric
      0 = Construction (Red/cyan)   0 = Glue assist  (DARK GREEN)
      1 = Dimension (Green/Blue)    1 = Cut (BLACK)
      2 = Crease (Yellow/Green)     2 = Crease (RED)
      3 = Half Cut (Blue)           3 = PERF 3x3
      4 = Serated (Magenta)         4 = Partial Cut (BLACK)
      5 = Cut (Cyan / red)          5 = PERF 6x6 
      6 = Matrix (Brown)            6 = 6 Cut/Cre
      7 = Annotation (Green)        7 = Glue assist  (DARK GREEN)
      8 = Front Edge (Red)          8 = Reverse Crease (RED WITH MARK)
      9 = Stripping (Light Green)   9 = Reverse Partial Cut (DARk RED? WITH MARK)
      10 = Die Board (Yellow)       10 = Print Registration (DARK GREEN?)
                            11 = Outside Bleed (LIGHT GREEN)
                            12 = 12 Cut/Cre
                            14 = SAFETY EDGE
                            15 = Matrix
                            20 = Annotation   (LIGHT GREEN)
      Default in CAD = Annotation   (LIGHT GREEN)
      Default in Java = CUT
      */
      String col = "1";
      switch (layer) {
        case "CUT" : 
	        col = "5"; // ACAD = Red 
	        break;
        case "CREASE" : 
	        col = "2"; // ACAD = Yellow
	        break;
        case "PERF3" : 
	        col = "3"; // 3 Cut, 3 Gap     ACAD = Green
	        break;
        case "PartialCut" : 
	        col = "3"; // Cut through all but outer facing     ACAD = Cyan
	        break;  
        case "PERF6" : 
	        col = "5"; // 6 Cut, 6 Gap     ACAD = Blue
	        break; 
        case "CUTCRE6" : 
	        col = "6"; // 6 Cut, 6 Crease    ACAD = Magenta 
	        break; 
        case "Annotation" : 
	        col = "7"; 
	        break;
        case "ReverseCrease" : 
        case "MATRIX" : 	
	        col = "6"; // 
	        break;
        case "ReversePartialCut" : 
	        col = "9"; 
	        break;    
        case "PrintRegistration" : 
	        col = "10"; 
	        break;  
        case "OutsideBleed" : 
	        col = "11"; 
	        break;   
        case "CUTCRE12" : 
	        col = "12";  // 12 Cut, 12 Crease     ACAD = Orange/Red
	        break;
        case "SAFETY" : 
	        col = "4"; // Safety Deckle Edge      ACAD = Brown
	        break;     
        case "Annotation2" : 
	        col = "20"; // ACAD = Red
	        break;       
        default: 
	        col = "1";
      } // end of switch
      return col;  
  } // getLineColour
  
  
} // LayerLineType
