import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ElementData
{
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

}//abstract class