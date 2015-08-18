/**
 * Created by NevilleVH on 2015-08-13.
 */
public class Multiplier extends Cell{
    public enum MultiplierType {WRD, LTR}
    private MultiplierType multiplierType;
    private int multiplierValue;

    public Multiplier(MultiplierType pMultiplierType, int pMultiplierValue){
        multiplierType = pMultiplierType;
        multiplierValue = pMultiplierValue;
    }

    public MultiplierType getMultiplierType() {
        return multiplierType;
    }

    public int getMultiplierValue() {
        return multiplierValue;
    }

    public String[] getConcatable(){
        String[] result = new String[4];
        result[0] = result[3] = "───";
        result[1] = ""+ multiplierType.name() +"│";
        result[2] = String.format("*%d*│",multiplierValue);
        return result;
    }
}
