package d0421p;

public class MachineSz {

    // Machine Size
    String diecutter = "Elite";
    double eliteFlexoS1 = 1055 - 25;
    double eliteFlexoN2 = 1575 - 20;
    double ibericaS1 = 1000 - 30;
    double ibericaN2 = 1000 - 20;
    double dcS1 = 0;
    double dcN2 = 0;
    double dcRevS1 = 0;
    double dcRevN2 = 0;
    
    double blkS1 = 0;
    double blkN2 = 0;
    double blkInsetS1 = 0;
    double blkInsetN2 = 0;
    
    double rule2ruleS1 = 0;
    double rule2ruleN2 = 0;
    
    int noUpS1 = 0;
    int noUpN2 = 0;
    int noUpRevS1 = 0;
    int noUpRevN2 = 0;   
    
    public void CalcR2R() {
    	dcS1 = eliteFlexoS1;
    	dcN2 = eliteFlexoN2;
    	
	    this.noUpS1 = (int) (this.dcS1 / this.blkS1);
	    this.noUpN2 = (int) (this.dcN2 / this.blkN2);
	    this.noUpRevS1 = (int) (this.dcS1 / this.blkN2);
	    this.noUpRevN2 = (int) (this.dcN2 / this.blkS1);

//	    this.noUpN2 = 1;	    
//	    if (this.dcN2 - this.blkN2 > 0) {
//	      double tmp = (this.dcN2 - this.blkN2);
//	      while (tmp >= blkInsetN2) {
//	        //System.out.println(rrn2 + " " + this.noUpN2 + " " + tmp);
//	        tmp = tmp - blkInsetN2;
//	        this.noUpN2++;
//	      } //while
//	    } // if   	
    	
	    
	    
    } // CalcR2R
    
    
} // MachineSz
