import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ElementData
{
    public enum DataType
    {
        CATEGORY,
<<<<<<< HEAD:code/elementData/ElementData.java
        INDEX,
        NOTE;
    }//DataType

=======
        FILE,
        NOTE;
    }
    
>>>>>>> defe8ccac325a0a0b19f61b6358799210d12ee96:code/elementData/ElementData.java
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
<<<<<<< HEAD:code/elementData/ElementData.java
    public abstract Element toIndexElement(Document doc);

=======
    
>>>>>>> defe8ccac325a0a0b19f61b6358799210d12ee96:code/elementData/ElementData.java
    public abstract String getTime();

}//abstract class