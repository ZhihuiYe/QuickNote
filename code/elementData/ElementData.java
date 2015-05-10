import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ElementData
{
    public enum DataType
    {
        CATEGORY,
        INDEX,
        NOTE;
    }//DataType

    private DataType dataType;

    public ElementData(DataType givenDataType)
    {
        dataType = givenDataType;
    }//constructor

    public DataType getDataType()
    {
        return dataType;
    }//getDataType

    public abstract Element toElement(Document doc);
    public abstract Element toIndexElement(Document doc);

    public abstract String getTime();

}//abstract class