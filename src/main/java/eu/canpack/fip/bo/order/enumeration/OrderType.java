package eu.canpack.fip.bo.order.enumeration;

/**
 * The OrderType enumeration.
 */
public enum OrderType {
    ESTIMATION("WYCENA","W"), EMERGENCY("AWARIA", "A"), PRODUCTION("PRODUKCJA", "P");

    private String plName;
    private String shortcut;

    OrderType(String plName,String shortcut){
        this.plName=plName;
        this.shortcut=shortcut;
    };
  public  String getPlName(){
        return this.plName;
    }
  public  String getShortcut(){
        return this.shortcut;
    }
}
